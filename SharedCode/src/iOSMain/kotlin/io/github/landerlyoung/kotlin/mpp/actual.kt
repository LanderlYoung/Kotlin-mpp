@file:Suppress("NOTHING_TO_INLINE")

package io.github.landerlyoung.kotlin.mpp

import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlinx.io.IOException
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSRunLoop
import platform.Foundation.NSURL
import platform.Foundation.NSURLConnection
import platform.Foundation.NSURLResponse
import platform.Foundation.performBlock
import platform.Foundation.sendSynchronousRequest
import platform.Foundation.setHTTPMethod
import platform.UIKit.UIDevice
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual fun platformName(): String {
    return UIDevice.currentDevice.let {
        it.systemName() + " " + it.systemVersion
    }
}

actual object MyDispatchers {
    actual val Main: CoroutineDispatcher = object : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            NSRunLoop.mainRunLoop().performBlock {
                block.run()
            }
        }
    }

    actual val Worker: CoroutineDispatcher
        get() = throw IllegalArgumentException("""\
                |Kotlin/Native doesn't support multi threaded coroutines now(2018/11/14)
                |and the Dispatchers.Main can't work on ios as well
                |
                |https://github.com/ktorio/ktor/issues/678
                |
                |The following issues are try to fix Dispatchers.Main issue:
                |https://github.com/Kotlin/kotlinx.coroutines/issues/470
                |https://github.com/Kotlin/kotlinx.coroutines/issues/770
            """.trimMargin()
        )
}

fun blockingHttpGetOnIos(url: String): Pair<String?, String> {
    val request = NSMutableURLRequest()
    request.setHTTPMethod("GET")
    request.setURL(NSURL(string = url))

    memScoped {
        val responsePtr = alloc<ObjCObjectVar<NSURLResponse?>>()
        val errorPtr = alloc<ObjCObjectVar<NSError?>>()

        val responseData = NSURLConnection.sendSynchronousRequest(
                request, responsePtr.ptr, errorPtr.ptr)

        val response = responsePtr.value
        val error = errorPtr.value
        if (error != null) {
            return null to error.localizedDescription()
        }
        if (response is NSHTTPURLResponse) {
            if (response.statusCode != 200L) {
                return null to "invalid statusCode ${response.statusCode}"
            }
        }

        return responseData?.toKString() to "unknown error"
    }
}

inline fun NSData.toKString(): String? {
    val bytes = bytes?.readBytes(length.toInt())
    return bytes?.stringFromUtf8()
}

@Throws(IOException::class)
actual suspend fun httpGet(url: String): String =
        suspendCoroutine { continuation ->
            AsyncRunner.doAsyncWork({
                blockingHttpGetOnIos(url)
            }) { result ->
                println("httpGet on mainThread $url result ${result.first}")
                if (result.first != null) {
                    continuation.resume(result.first!!)
                } else {
                    continuation.resumeWith(Result.failure(IOException(result.second)))
                }
            }
        }
