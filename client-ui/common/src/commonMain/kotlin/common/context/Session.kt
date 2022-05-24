package common.context

import java.nio.channels.SocketChannel

interface Session {
    val isConnected: Boolean

    fun connect(host: String, port: Int)

    fun connect(): Boolean

    fun disconnect()

    fun getSocketChannel(): SocketChannel
}