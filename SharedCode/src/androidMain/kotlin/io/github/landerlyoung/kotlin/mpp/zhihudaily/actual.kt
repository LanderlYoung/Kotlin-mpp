package io.github.landerlyoung.kotlin.mpp.zhihudaily

import java.io.IOException
import java.net.URL

@Throws(IOException::class)
actual fun httpGet(url: String): String {
    val httpConn = URL(url).openConnection()
    httpConn.connect()
    return httpConn.getInputStream().buffered().readBytes().toString(Charsets.UTF_8)
}
