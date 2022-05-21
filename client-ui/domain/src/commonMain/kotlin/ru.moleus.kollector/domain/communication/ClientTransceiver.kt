package ru.moleus.kollector.domain.communication

import common.context.Session
import common.exceptions.InvalidCredentialsException
import communication.MessagingUtil
import communication.RequestPurpose
import communication.ResponseCode
import communication.packaging.BaseRequest
import communication.packaging.Request
import communication.packaging.Response
import exceptions.ReceivedInvalidObjectException
import user.User
import java.io.IOException
import java.nio.ByteBuffer

class ClientTransceiver(private val session: Session) : Transceiver {
    override var user: User? = null

    /**
     * @throws InvalidCredentialsException - if wrong login/password are passed.
     * @throws ReceivedInvalidObjectException - if response from server is unexpected.
     * @throws java.io.IOException - if connection is broken.
     */
    override fun requestAndRead(purpose: RequestPurpose, payload: Any?): Any {
        synchronized(this) {
            val request: Request = BaseRequest.of(purpose, payload, user)
            val response = withReconnect { sendAndGet(request) }
            return readPayloadWithCheck(response)
        }
    }

    private fun withReconnect(repeat: Boolean = true, doRequest: () -> Response): Response {
        try {
            return doRequest()
        } catch (e: Exception) {
            if (repeat) {
                session.connect()
                return withReconnect(false) { doRequest() }
            }
            throw IOException("Server is not reachable.")
        }
    }

    private fun sendAndGet(request: Request): Response {
        send(request)
        return receive()
    }

    private fun send(request: Request) {
        val message = MessagingUtil.serialize(request)
        session.getSocketChannel().write(message)
    }

    private fun receive(): Response {
        val response = MessagingUtil.deserialize(getResponseBytes())
        return MessagingUtil.castResponseWithCheck(response)
    }

    private fun getResponseBytes(): ByteBuffer =
        ByteBuffer.allocate(65536)
            .apply {
                session.getSocketChannel().read(this)
            }

    private fun readPayloadWithCheck(response: Response): Any {
        when (response.responseCode) {
            ResponseCode.SUCCESS -> {}
            ResponseCode.AUTH_FAILED -> throw InvalidCredentialsException()
            else -> throw ReceivedInvalidObjectException(Any::class.java, "Empty or Invalid response received")
        }
        return response.payload.orElseThrow {
            ReceivedInvalidObjectException(
                Any::class.java,
                "empty payload received"
            )
        }
    }
}