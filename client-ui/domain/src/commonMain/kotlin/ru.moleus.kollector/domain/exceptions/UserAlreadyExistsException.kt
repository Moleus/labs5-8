package ru.moleus.kollector.domain.exceptions

import common.exceptions.InvalidCredentialsException

class UserAlreadyExistsException(login: String) :
    InvalidCredentialsException("User with login [$login] already exists.")