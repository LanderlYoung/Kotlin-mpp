package io.github.landerlyoung.kotlin.mpp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun platformName(): String {
    return "Android"
}

actual object MyDispatchers {
    actual val Main: CoroutineDispatcher = Dispatchers.Main
    actual val Worker: CoroutineDispatcher = Dispatchers.IO
}
