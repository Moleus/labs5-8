package ru.moleus.kollector.domain.communication

import java.nio.channels.SocketChannel

interface Session {
    val isConnected: Boolean

    fun connect(): Boolean

    fun disconnect()

    fun getSocketChannel(): SocketChannel
}