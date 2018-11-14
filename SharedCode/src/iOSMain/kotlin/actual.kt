package io.github.landerlyoung.kotlin.mpp


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

fun testCoroutine() {
    runBlocking {
        launch(MyDispatchers.Main) {
            println("Hello")
            withContext(MyDispatchers.Worker) {
                delay(10L)
                println("World!")
            }
        }
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
            println("dispatch block $block")
            // https://github.com/shakurocom/kotlin_multiplatform/blob/master/platform-ios/src/main/kotlin/com/multiplatform/coroutines/AsyncDispatcher.kt
            // https://github.com/JetBrains/kotlin-native/blob/master/samples/workers/src/workersMain/kotlin/Workers.kt
            this.worker.execute(TransferMode.SAFE, { block }) {
                println("run dispatched block $it")
                it.run()
            }
        }
    }
}
