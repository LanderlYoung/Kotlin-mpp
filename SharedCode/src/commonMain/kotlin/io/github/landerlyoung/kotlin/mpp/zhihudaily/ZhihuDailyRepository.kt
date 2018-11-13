package io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily

import io.github.landerlyoung.kotlin.mpp.MyDispatchers
import io.github.landerlyoung.kotlin.mpp.zhihudaily.LatestStories
import io.github.landerlyoung.kotlin.mpp.zhihudaily.StoryContent
import io.github.landerlyoung.kotlin.mpp.zhihudaily.httpGet
import kotlinx.coroutines.withContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JSON

/**
 * <pre>
 * Author: landerlyoung@gmail.com
 * Date:   2018-11-10
 * Time:   15:30
 * Life with Passion, Code with Creativity.
 * </pre>
 */
object ZhihuDailyRepository {
    private val json = JSON(strictMode = false)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> String.toJson(serializer: DeserializationStrategy<T>): T =
            json.parse(serializer, this)

    private suspend fun doHttpGet(url: String) =
            withContext(MyDispatchers.Worker) {
                println("doHttpGetAsync")
                httpGet(url)
            }

    suspend fun getLatestStories(): LatestStories {
        println("getLatestStories")
        return doHttpGet("https://news-at.zhihu.com/api/4/news/latest")
                .toJson(LatestStories.serializer())
    }

    suspend fun getStoryContent(newsId: Long): StoryContent {
        return doHttpGet("https://news-at.zhihu.com/api/4/news/$newsId")
                .toJson(StoryContent.serializer())
    }
}