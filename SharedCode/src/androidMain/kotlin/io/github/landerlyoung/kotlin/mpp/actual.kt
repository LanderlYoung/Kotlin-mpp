package io.github.landerlyoung.kotlin.mpp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import java.net.HttpURLConnection
import java.net.URL

actual fun platformName(): String {
    return "Android"
}

actual object MyDispatchers {
    actual val Main: CoroutineDispatcher = Dispatchers.Main
    actual val Worker: CoroutineDispatcher = Dispatchers.IO
}

@Throws(IOException::class)
actual suspend fun httpGet(url: String): String {
    return withContext(MyDispatchers.Worker) {
        try {
            var conn: HttpURLConnection? = null
            try {
                val httpConn = URL(url).openConnection() as HttpURLConnection
                conn = httpConn
                httpConn.connect()
                httpConn.inputStream.buffered().readBytes()
                        .toString(Charsets.UTF_8)
            } finally {
                conn?.disconnect()
            }
        } catch (e: kotlinx.io.IOException) {
            throw  e
        } catch (t: Throwable) {
            throw IOException(t.message, t)
        }
    }
}
