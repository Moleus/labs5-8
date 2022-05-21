package ru.moleus.kollector.domain.communication

import collection.CollectionChangelist
import collection.CollectionWrapper
import commands.CommandNameToInfo
import commands.ExecutionPayload
import commands.ExecutionResult
import common.context.Exchanger
import communication.RequestPurpose.*
import exceptions.ReceivedInvalidObjectException
import model.data.Model
import user.User

class ClientExchanger(
    private val transceiver: Transceiver,
) : Exchanger {
    override fun <T : Model> requestFullCollection(): CollectionWrapper<T> {
        val responsePayload: Any = transceiver.requestAndRead(INIT_COLLECTION, null)
        return castOrThrow<Any, CollectionWrapper<T>>(responsePayload)
    }

    override fun requestAccessibleCommandsInfo(): CommandNameToInfo {
        val responsePayload: Any = transceiver.requestAndRead(GET_COMMANDS, null)
        return castOrThrow<Any, CommandNameToInfo>(responsePayload)
    }

    override fun <T: Model> requestCollectionChanges(currentVersion: Long): CollectionChangelist<T> {
        val responsePayload: Any = transceiver.requestAndRead(GET_CHANGELIST, currentVersion)
        return castOrThrow<Any, CollectionChangelist<T>>(responsePayload)
    }

    override fun requestCommandExecution(payload: ExecutionPayload): ExecutionResult {
        val responsePayload: Any = transceiver.requestAndRead(CHANGE_COLLECTION, payload)
        return castOrThrow<Any, ExecutionResult>(responsePayload)
    }

    override fun requestLogin(user: User) {
        transceiver.user = user
        transceiver.requestAndRead(LOGIN, null)
    }

    override fun requestRegister(user: User) {
        transceiver.user = user
        transceiver.requestAndRead(REGISTER, null)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T, R> castOrThrow(obj: Any): R =
        (obj as? R) ?: throw ReceivedInvalidObjectException(T::class.java, obj::class.java)
}