package ru.moleus.kollector.domain.communication

import common.context.Exchanger
import user.User
import common.exceptions.InvalidCredentialsException
import ru.moleus.kollector.domain.exceptions.UserAlreadyExistsException
import java.io.IOException

class ClientAuthenticator(private val exchanger: Exchanger) : Authenticator {

    @Throws(UserAlreadyExistsException::class, IOException::class)
    override fun register(user: User) {
        exchanger.requestRegister(user)
        try {
            exchanger.validateRegister()
        } catch (e: InvalidCredentialsException) {
            throw UserAlreadyExistsException(user.login)
        }
    }

    @Throws(InvalidCredentialsException::class, IOException::class)
    override fun login(user: User) {
        exchanger.requestLogin(user)
        exchanger.validateLogin()
    }
}