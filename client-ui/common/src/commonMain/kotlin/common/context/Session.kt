package common.context

import java.io.InputStream
import java.io.OutputStream

interface Session {
    val isConnected: Boolean

    fun connect(host: String, port: Int)

    fun connect(): Boolean

    fun disconnect()

    fun getInputStream(): InputStream

    fun getOutputStream(): OutputStream
}