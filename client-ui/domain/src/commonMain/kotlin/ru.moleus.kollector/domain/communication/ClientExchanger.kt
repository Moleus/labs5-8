package ru.moleus.kollector.domain.communication

import collection.CollectionChangelist
import collection.CollectionWrapper
import commands.CommandNameToInfo
import commands.ExecutionPayload
import commands.ExecutionResult
import common.context.Exchanger
import common.exceptions.InvalidCredentialsException
import communication.RequestPurpose
import communication.ResponseCode
import communication.Transceiver
import communication.packaging.BaseRequest
import communication.packaging.Request
import communication.packaging.Response
import exceptions.ReceivedInvalidObjectException
import ru.moleus.kollector.domain.exceptions.ReconnectionTimeoutException
import model.data.Model
import user.User
import java.util.Optional

class ClientExchanger(private val transceiver: Transceiver, clientSession: Session) : Exchanger {
    private val clientSession: Session
    private var user: User? = null
    private var lastRequestPurpose: RequestPurpose? = null

    init {
        this.clientSession = clientSession
    }

    override fun requestAccessibleCommandsInfo() {
        makeNewRequest(RequestPurpose.GET_COMMANDS, null)
    }

    override fun requestFullCollection() {
        makeNewRequest(RequestPurpose.INIT_COLLECTION, null)
    }

    override fun requestCollectionChanges(currentVersion: Long) {
        makeNewRequest(RequestPurpose.GET_CHANGELIST, currentVersion)
    }

    override fun requestCommandExecution(payload: ExecutionPayload) {
        makeNewRequest(RequestPurpose.CHANGE_COLLECTION, payload)
    }

    override fun requestLogin(user: User?) {
        this.user = user
        makeNewRequest(RequestPurpose.LOGIN, null)
    }

    override fun requestRegister(user: User?) {
        this.user = user
        makeNewRequest(RequestPurpose.REGISTER, null)
    }

    private fun makeNewRequest(purpose: RequestPurpose, payload: Any?) {
        lastRequestPurpose = purpose
        val request: Request = BaseRequest.of(purpose, payload, user)
        sendWithReconnect(request)
    }

    private fun sendWithReconnect(request: Request) {
        try {
            transceiver.send(request)
            return
        } catch (e: java.io.IOException) {
            reconnectOrThrowTimeout()
        }
        sendWithReconnect(request)
    }

    private fun reconnectOrThrowTimeout() {
        val RECONNECTION_SECONDS = 15
        if (!clientSession.reconnect(RECONNECTION_SECONDS)) {
            println("Failed to connect to server")
            throw ReconnectionTimeoutException()
        }
        transceiver.newSocketChannel(clientSession.getSocketChannel())
    }

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    override fun <T : Model> receiveFullCollection(): CollectionWrapper<T> {
        checkPurposeMatch(RequestPurpose.INIT_COLLECTION)
        val response = receiveAndCheckResponse()
        val payload = readPayload(response)
        return castObjTo(payload, CollectionWrapper::class.java) as CollectionWrapper<T>
    }

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    override fun <T : Model> receiveCollectionChanges(): CollectionChangelist<T> {
        checkPurposeMatch(RequestPurpose.GET_CHANGELIST)
        val response = receiveAndCheckResponse()
        val payload = readPayload(response)
        return castObjTo(payload, CollectionChangelist::class.java) as CollectionChangelist<T>
    }

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    override fun receiveAccessibleCommandsInfo(): CommandNameToInfo {
        checkPurposeMatch(RequestPurpose.GET_COMMANDS)
        val response = receiveAndCheckResponse()
        val payload = readPayload(response)
        return castObjTo(payload, CommandNameToInfo::class.java)
    }

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    override fun receiveExecutionResult(): ExecutionResult {
        checkPurposeMatch(RequestPurpose.CHANGE_COLLECTION)
        val response = receiveAndCheckResponse()
        val payload = readPayload(response)
        return castObjTo(payload, ExecutionResult::class.java)
    }

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    override fun validateLogin() {
        checkPurposeMatch(RequestPurpose.LOGIN)
        receiveAndCheckResponse()
    }

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    override fun validateRegister() {
        checkPurposeMatch(RequestPurpose.REGISTER)
        receiveAndCheckResponse()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <U> castObjTo(obj: Any, toClass: Class<U>): U {
        if (!toClass.isInstance(obj)) {
            throw ReceivedInvalidObjectException(toClass, obj.javaClass)
        }
        return obj as U
    }

    private fun checkPurposeMatch(expected: RequestPurpose) {
        if (lastRequestPurpose != expected) {
            throw IllegalArgumentException("Previous request purpose should be: $expected")
        }
    }

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    private fun receiveAndCheckResponse(): Response {
        val response: Response = receiveWithReconnect().orElseThrow {
            ReceivedInvalidObjectException(
                Response::class.java, "Empty response"
            )
        }
        checkResponseStatus(response)
        return response
    }

    @Throws(java.io.IOException::class)
    private fun receiveWithReconnect(): Optional<Response> {
        try {
            return transceiver.receive().map { obj: Any -> Response::class.java.cast(obj) }
        } catch (e: java.io.IOException) {
            // failed to read. Usually, because of server shutdown.
            reconnectOrThrowTimeout()
        }
        throw java.io.IOException("Server was unavailable. Please, repeat the request")
    }

    @Throws(InvalidCredentialsException::class)
    private fun checkResponseStatus(response: Response) {
        when (response.responseCode) {
            ResponseCode.INVALID_PAYLOAD -> throw java.lang.IllegalArgumentException("Invalid payload provided to server")
            ResponseCode.AUTH_FAILED -> throw InvalidCredentialsException()
            else -> {}
        }
    }

    private fun readPayload(response: Response): Any {
        return response.payload.orElseThrow {
            ReceivedInvalidObjectException(
                Any::class.java, "empty payload"
            )
        }
    }
}