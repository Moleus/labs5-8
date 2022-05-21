package common.context

import collection.CollectionChangelist
import collection.CollectionWrapper
import commands.CommandNameToInfo
import commands.ExecutionPayload
import commands.ExecutionResult
import common.exceptions.InvalidCredentialsException
import model.data.Model
import user.User

interface Exchanger {
    fun requestFullCollection()
    fun requestCollectionChanges(currentVersion: Long)
    fun requestCommandExecution(payload: ExecutionPayload)
    fun requestAccessibleCommandsInfo()

    @Throws(java.io.IOException::class, InvalidCredentialsException::class)
    fun <T : Model> receiveCollectionChanges(): CollectionChangelist<T>
    fun requestLogin(user: User?)
    fun requestRegister(user: User?)

    @Throws(java.io.IOException::class, InvalidCredentialsException::class)
    fun <T : Model> receiveFullCollection(): CollectionWrapper<T>

    @Throws(java.io.IOException::class, InvalidCredentialsException::class)
    fun receiveExecutionResult(): ExecutionResult

    @Throws(java.io.IOException::class, InvalidCredentialsException::class)
    fun receiveAccessibleCommandsInfo(): CommandNameToInfo

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    fun validateLogin()

    @Throws(InvalidCredentialsException::class, java.io.IOException::class)
    fun validateRegister()
}