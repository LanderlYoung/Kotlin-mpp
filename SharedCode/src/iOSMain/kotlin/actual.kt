package io.github.landerlyoung.kotlin.mpp


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.Foundation.NSRunLoop
import platform.Foundation.performBlock
import platform.UIKit.UIDevice
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.TransferMode

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

    actual val Worker: CoroutineDispatcher = object : CoroutineDispatcher() {
        val worker = kotlin.native.concurrent.Worker.start()

        override fun dispatch(context: CoroutineContext, block: Runnable) {
            // https://github.com/shakurocom/kotlin_multiplatform/blob/master/platform-ios/src/main/kotlin/com/multiplatform/coroutines/AsyncDispatcher.kt
            // https://github.com/JetBrains/kotlin-native/blob/master/samples/workers/src/workersMain/kotlin/Workers.kt
            this.worker.execute(TransferMode.UNSAFE, { block }) {
                it.run()
            }
        }
    }
}
