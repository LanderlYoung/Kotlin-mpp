package io.github.landerlyoung.kotlin.mpp.zhihudaily

import io.github.landerlyoung.kotlin.mpp.httpGet
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
    private inline fun <T> String.toJson(serializer: DeserializationStrategy<T>): T {
        println("toJson $this")
        return json.parse(serializer, this)
    }

    suspend fun getLatestStories(): LatestStories {
        println("getLatestStories")
        return httpGet("https://news-at.zhihu.com/api/4/news/latest")
                .toJson(LatestStories.serializer())
    }

    suspend fun getStoryContent(newsId: Long): StoryContent {
        return httpGet("https://news-at.zhihu.com/api/4/news/$newsId")
                .toJson(StoryContent.serializer())
    }
}