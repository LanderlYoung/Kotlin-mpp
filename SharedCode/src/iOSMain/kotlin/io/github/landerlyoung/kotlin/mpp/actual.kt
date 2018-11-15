package io.github.landerlyoung.kotlin.mpp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlinx.io.IOException
import platform.Foundation.NSRunLoop
import platform.Foundation.performBlock
import platform.UIKit.UIDevice
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze

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

interface IosHttpGetAgent {
    interface Callback {
        fun onGetResult(url: String, result: String?, error: String)
    }

    fun httpGet(url: String)
}

private val iosHttpGetAgent = AtomicReference<IosHttpGetAgent?>(null)

fun setIosHttpGetAgent(agent: IosHttpGetAgent) {
    iosHttpGetAgent.value = agent.freeze()
}

private val requestingHashMap = hashMapOf<String, IosHttpGetAgent.Callback>()

fun notifyHttpGetResponse(url: String, result: String?, error: String) {
    requestingHashMap.remove(url)?.onGetResult(url, result, error)
}

@Throws(IOException::class)
actual suspend fun httpGet(url: String): String {
    return suspendCoroutine { continuation ->
        val cb = object : IosHttpGetAgent.Callback {
            override fun onGetResult(url: String, result: String?, error: String) {
                if (result != null) {
                    continuation.resume(result)
                } else {
                    continuation.resumeWith(Result.failure(IOException(error)))
                }
            }
        }
        requestingHashMap[url] = cb
        iosHttpGetAgent.value!!.httpGet(url)
    }

    //  NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    //    [request setHTTPMethod:@"GET"];
    //    [request setURL:[NSURL URLWithString:url]];
    //
    //    NSError *error = nil;
    //    NSHTTPURLResponse *responseCode = nil;
    //
    //    NSData *oResponseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&responseCode error:&error];
    //
    //    if([responseCode statusCode] != 200){
    //        NSLog(@"Error getting %@, HTTP status code %i", url, [responseCode statusCode]);
    //        return nil;
    //    }
    //
    //    return [[NSString alloc] initWithData:oResponseData encoding:NSUTF8StringEncoding];

    /*
    // kotlin/native can't use multi threaded coroutine now.
    // do it on the swift side...
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
            throw IOException(error.localizedDescription())
        }
        if (response is NSHTTPURLResponse) {
            if (response.statusCode != 200L) {
                throw IOException("invalid statusCode ${response.statusCode}")
            }
        }

        return responseData?.bytes?.reinterpret<ByteVar>()?.toKString() ?: "null"
    }
    */
}

