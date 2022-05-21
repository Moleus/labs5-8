package common.exceptions

open class InvalidCredentialsException : Exception {
    constructor() : super("Invalid login or password") {}
    constructor(message: String) : super(message) {}
}