package common.context.auth

interface Authenticator {
    fun login(username: String, password: String) : RegistrationStatus
    fun register(username: String, password: String) : RegistrationStatus
}