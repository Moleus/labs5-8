package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import common.exceptions.InvalidCredentialsException
import exceptions.ReceivedInvalidObjectException
import ru.moleus.kollector.domain.communication.Authenticator
import user.User
import java.io.IOException

class Register(private val authenticator: Authenticator) : AbstractCommand(
    CommandInfo.of(
        "register",
        "Create new user account",
        true,
        0,
        CommandType.AUTHENTICATION,
        ExecutionMode.CLIENT
    )
) {
    override fun execute(payload: ExecutionPayload): ExecutionResult {
        val user: User = payload.user
        try {
            authenticator.register(user)
        } catch (e: Exception) {
            return when (e) {
                is IOException -> ExecutionResult.valueOf(false, "Connection error. Repeat your request")
                is InvalidCredentialsException -> ExecutionResult.valueOf(false, e.message)
                is ReceivedInvalidObjectException -> ExecutionResult.valueOf(false, e.message)
                else -> ExecutionResult.valueOf(false, e.message)
            }
        }
        return ExecutionResult.valueOf(true, "Registration completed successfully")
    }
}