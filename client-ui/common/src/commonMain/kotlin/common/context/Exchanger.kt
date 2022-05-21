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
    fun <T : Model> requestFullCollection(): CollectionWrapper<T>
    fun <T : Model> requestCollectionChanges(currentVersion: Long): CollectionChangelist<T>
    fun requestCommandExecution(payload: ExecutionPayload): ExecutionResult
    fun requestAccessibleCommandsInfo(): CommandNameToInfo

    fun requestLogin(user: User)
    fun requestRegister(user: User)
}