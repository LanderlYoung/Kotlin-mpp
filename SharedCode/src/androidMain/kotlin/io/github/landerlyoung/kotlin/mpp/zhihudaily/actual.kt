package io.github.landerlyoung.kotlin.mpp.zhihudaily

import kotlinx.io.IOException
import java.net.URL

@Throws(IOException::class)
actual fun httpGet(url: String): String {
    try {
        val httpConn = URL(url).openConnection()
        httpConn.connect()
        return httpConn.getInputStream().buffered().readBytes().toString(Charsets.UTF_8)
    } catch (e: kotlinx.io.IOException) {
        throw  e
    } catch (t: Throwable) {
        throw IOException(t.message, t)
    }
}
