package io.github.landerlyoung.kotlin.mpp

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.staticCFunction
import platform.darwin.dispatch_async_f
import platform.darwin.dispatch_get_main_queue
import kotlin.native.concurrent.AtomicInt
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.attach
import kotlin.native.concurrent.freeze

/**
 * <pre>
 * Author: landerlyoung@gmail.com
 * Date:   2018-11-22
 * Time:   16:38
 * Life with Passion, Code with Creativity.
 * </pre>
 */

internal object AsyncRunner {
    private val worker = Worker.start()

    fun <T> doAsyncWork(backJob: () -> T, mainJob: (T) -> Unit) {

        worker.execute(TransferMode.SAFE,
                { (backJob to ThreadLocalValue(mainJob)).freeze() }) {

            // execute background job
            val result = it.first()

            val cPointer = DetachedObjectGraph {
                (result to it.second).freeze()
            }.asCPointer()

            val cFun = staticCFunction { context: COpaquePointer? ->
                val pair = DetachedObjectGraph<Pair<T, ThreadLocalValue<(T) -> Unit>>>(
                        context).attach()

                pair.second.value(pair.first)
            }

            // post to main thread
            dispatch_async_f(dispatch_get_main_queue(), cPointer, cFun)
        }
    }
}

internal class ThreadLocalValue<T>(_value: T) {
    private val index: Int = indexCounter.addAndGet(1)

    var value: T
        get() = ThreadLocalHolder[index]!!
        set(_value) {
            ThreadLocalHolder[index] = _value
        }

    init {
        // freeze by default
        freeze()
        value = _value
    }

    companion object {
        val indexCounter = AtomicInt(0)
    }

    @ThreadLocal
    private object ThreadLocalHolder {
        private val map = mutableMapOf<Int, Any>()

        operator fun <T> get(key: Int): T? =
                map[key] as T?

        operator fun set(key: Int, value: Any?) {
            if (value == null) {
                map.remove(key)
            } else {
                map[key] = value
            }
        }
    }
}
