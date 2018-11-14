package io.github.landerlyoung.kotlin.mpp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.runBlocking
import platform.Foundation.NSDate
import platform.Foundation.NSRunLoop
import platform.Foundation.addTimeInterval
import platform.Foundation.runUntilDate
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.TransferMode

/**
 * <pre>
 * Author: landerlyoung@gmail.com
 * Date:   2018-11-14
 * Time:   15:46
 * Life with Passion, Code with Creativity.
 * </pre>
 */
actual object MyDispatchers {
    actual val Main: CoroutineDispatcher = /*object : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            NSRunLoop.mainRunLoop().performBlock {
                block.run()
            }
        }
    }*/ Dispatchers.Main

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

fun runBlocking(block: () -> Int): Int {
    return runBlocking(MyDispatchers.Main) {
        block()
    }
}


class Expectation<T> {
    private var waiting = true
    private var result: T? = null

    fun fulfill(result: T?) {
        waiting = false
        this.result = result
    }

    fun wait(): T? {
        while (waiting) {
            advanceRunLoop()
        }

        return result
    }
}

private fun advanceRunLoop() {
    val date = NSDate().addTimeInterval(1.0) as NSDate
    NSRunLoop.mainRunLoop.runUntilDate(date)
}