package auth

interface Authenticator {
    fun login(username: String, password: String) : RegistrationStatus
    fun register(username: String, password: String) : RegistrationStatus

    fun login(user: UserDto)
    fun register(user: UserDto)
}