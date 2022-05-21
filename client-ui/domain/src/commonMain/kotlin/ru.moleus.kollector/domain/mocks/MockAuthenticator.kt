package ru.moleus.kollector.domain.mocks

import common.context.auth.Authenticator
import common.context.auth.RegistrationStatus

class MockAuthenticator: Authenticator {
    override fun login(username: String, password: String): RegistrationStatus {
//        System.err.println("login request for name: $username and password: $password")
        return RegistrationStatus.SUCCESS
    }

    override fun register(username: String, password: String): RegistrationStatus {
//        System.err.println("Register request for name: $username and password: $password")
        return RegistrationStatus.SUCCESS
    }
}