package ru.moleus.kollector.domain.communication

import communication.RequestPurpose
import user.User

interface Transceiver {
    var user: User?
    fun requestAndRead(purpose: RequestPurpose, payload: Any?): Any
}