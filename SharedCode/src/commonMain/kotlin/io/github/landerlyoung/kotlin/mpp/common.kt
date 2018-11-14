package io.github.landerlyoung.kotlin.mpp

import kotlinx.coroutines.CoroutineDispatcher

expect fun platformName(): String

expect object MyDispatchers {
    val Main: CoroutineDispatcher
    val Worker: CoroutineDispatcher
}

fun createApplicationScreenMessage() : String {
    return "Kotlin Rocks on ${platformName()}"
}

// http request
expect suspend fun httpGet(url: String): String