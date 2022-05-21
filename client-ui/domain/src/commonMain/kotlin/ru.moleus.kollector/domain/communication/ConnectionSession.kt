package ru.moleus.kollector.domain.communication

import common.context.Session
import java.nio.channels.SocketChannel

class ConnectionSession() : Session {
    private lateinit var remoteAddress: java.net.InetSocketAddress
    private var socketChannel: SocketChannel = SocketChannel.open()

    override fun connect(host: String, port: Int) {
        remoteAddress = java.net.InetSocketAddress(host, port)
        connect()
    }

    override fun connect(): Boolean {
        disconnect()
        socketChannel = SocketChannel.open(remoteAddress)
        socketChannel.configureBlocking(true)
        return isConnected
    }

    override fun disconnect() {
        socketChannel.close()
    }

    override val isConnected: Boolean
        get() = socketChannel.isConnected

    override fun getSocketChannel(): SocketChannel {
        return socketChannel
    }
}