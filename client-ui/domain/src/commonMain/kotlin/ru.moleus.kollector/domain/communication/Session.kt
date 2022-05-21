package ru.moleus.kollector.domain.communication

import java.io.IOException
import java.nio.channels.SocketChannel

interface Session {
    val isConnected: Boolean

    fun connect(): Boolean
    fun reconnect(countDownSeconds: Int): Boolean

    @Throws(IOException::class)
    fun disconnect()

    fun getSocketChannel(): SocketChannel
}