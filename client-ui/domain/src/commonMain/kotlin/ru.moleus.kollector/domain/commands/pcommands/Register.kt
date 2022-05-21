package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import ru.moleus.kollector.domain.communication.Authenticator
import common.exceptions.InvalidCredentialsException
import user.User

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
        } catch (e: InvalidCredentialsException) {
            return ExecutionResult.valueOf(false, e.message)
        } catch (e: java.io.IOException) {
            return ExecutionResult.valueOf(false, e.message)
        }
        return ExecutionResult.valueOf(true, "Registration completed successfully")
    }
}