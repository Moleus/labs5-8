package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import ru.moleus.kollector.domain.communication.Authenticator
import common.exceptions.InvalidCredentialsException
import java.io.IOException

class Login(private val authenticator: Authenticator) : AbstractCommand(
    CommandInfo.of(
        "login",
        "Log into existing account",
        true,
        0,
        CommandType.AUTHENTICATION,
        ExecutionMode.CLIENT
    )
) {
    override fun execute(payload: ExecutionPayload): ExecutionResult {
        val user = payload.user
        try {
            authenticator.login(user)
        } catch (e: InvalidCredentialsException) {
            return ExecutionResult.valueOf(false, e.message)
        } catch (e: IOException) {
            return ExecutionResult.valueOf(false, e.message)
        }
        return ExecutionResult.valueOf(true, "Success. You are logged in.")
    }
}