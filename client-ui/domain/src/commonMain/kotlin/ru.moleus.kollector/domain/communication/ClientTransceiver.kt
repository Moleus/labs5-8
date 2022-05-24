package ru.moleus.kollector.domain.communication

import common.context.Session
import common.exceptions.InvalidCredentialsException
import communication.RequestPurpose
import communication.ResponseCode
import communication.packaging.BaseRequest
import communication.packaging.Request
import communication.packaging.Response
import exceptions.ReceivedInvalidObjectException
import user.User
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ClientTransceiver(private val session: Session) : Transceiver {
    private val requestSize: Int = 2048
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

    private fun withReconnect(doRequest: () -> Response): Response {
        try {
            return doRequest()
        } catch (e: IOException) {
            Thread.sleep(100)
            session.connect()
            throw e
        }
    }

    private fun sendAndGet(request: Request): Response {
        send(request)
        return receive()
    }

    private fun send(request: Request) {
        val fixedSizeBuffer = serializeWithFixedSize(request)
        println(fixedSizeBuffer.size)
        session.getOutputStream().write(fixedSizeBuffer)
    }

    private fun serializeWithFixedSize(data: Any): ByteArray {
        val fixedByteArray = ByteArray(requestSize)
        val bytesOut = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bytesOut)
        oos.writeObject(data)
        oos.flush()
        bytesOut.toByteArray()

        return bytesOut.toByteArray().copyInto(fixedByteArray)
    }

    private fun receive(): Response = ObjectInputStream(session.getInputStream()).readObject() as Response

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