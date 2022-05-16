package root.app

import auth.Authenticator
import auth.RegistrationStatus
import auth.UserDto

class MockAuthenticator: Authenticator {
    override fun login(username: String, password: String): RegistrationStatus {
//        System.err.println("login request for name: $username and password: $password")
        return RegistrationStatus.SUCCESS
    }

    override fun login(user: UserDto) {
//        System.err.println("Login request for user: $user")
    }

    override fun register(username: String, password: String): RegistrationStatus {
//        System.err.println("Register request for name: $username and password: $password")
        return RegistrationStatus.SUCCESS
    }

    override fun register(user: UserDto) {
//        System.err.println("Register request for user: $user")
    }
}