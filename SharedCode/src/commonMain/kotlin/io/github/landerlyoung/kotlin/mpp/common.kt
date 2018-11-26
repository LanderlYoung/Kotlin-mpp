package io.github.landerlyoung.kotlin.mpp

import kotlinx.coroutines.CoroutineDispatcher

expect fun platformName(): String

expect object MyDispatchers {
    val Main: CoroutineDispatcher
    val Worker: CoroutineDispatcher
}

// http request
expect suspend fun httpGet(url: String): String

fun createApplicationScreenMessage() : String {
    return "Kotlin Rocks on ${platformName()}"
}
