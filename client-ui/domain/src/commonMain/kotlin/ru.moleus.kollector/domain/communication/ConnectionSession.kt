package ru.moleus.kollector.domain.communication

import java.io.IOException
import java.nio.channels.SocketChannel

class ConnectionSession(host: String?, port: Int) : Session {
    private val remoteAddress: java.net.InetSocketAddress
    private lateinit var socketChannel: SocketChannel

    init {
        remoteAddress = java.net.InetSocketAddress(host, port)
    }

    override fun connect(): Boolean {
        try {
            socketChannel = SocketChannel.open(remoteAddress)
            socketChannel.configureBlocking(true)
        } catch (e: IOException) {
            return false
        }
        return isConnected
    }

    override fun reconnect(countDownSeconds: Int): Boolean {
        var connectionTriesCounter = 0
        try {
            disconnect()
        } catch (ignore: IOException) {
        }
        while (connectionTriesCounter++ < countDownSeconds) {
            try {
                if (!connect()) {
                    Thread.sleep(1000)
                    println("Reconnecting to server")
                } else {
                    println("Connected")
                    return true
                }
            } catch (ignored: InterruptedException) {
            }
        }
        return false
    }

    @Throws(IOException::class)
    override fun disconnect() {
        socketChannel.close()
    }

    override val isConnected: Boolean
        get() = socketChannel.isConnected

    override fun getSocketChannel(): SocketChannel {
        return socketChannel
    }
}