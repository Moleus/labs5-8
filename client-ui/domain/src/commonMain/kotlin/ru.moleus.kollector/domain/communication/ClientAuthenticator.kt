package ru.moleus.kollector.domain.communication

import common.context.Exchanger
import common.exceptions.InvalidCredentialsException
import ru.moleus.kollector.domain.exceptions.UserAlreadyExistsException
import user.User
import java.io.IOException

class ClientAuthenticator(private val exchanger: Exchanger) : Authenticator {

    @Throws(UserAlreadyExistsException::class, IOException::class)
    override fun register(user: User) {
        try {
            exchanger.requestRegister(user)
        } catch (e: InvalidCredentialsException) {
            throw UserAlreadyExistsException(user.login)
        }
    }

    @Throws(InvalidCredentialsException::class, IOException::class)
    override fun login(user: User) {
        exchanger.requestLogin(user)
    }
}