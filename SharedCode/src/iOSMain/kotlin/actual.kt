package io.github.landerlyoung.kotlin.mpp


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.Foundation.NSRunLoop
import platform.Foundation.performBlock
import platform.UIKit.UIDevice
import kotlin.coroutines.CoroutineContext

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
