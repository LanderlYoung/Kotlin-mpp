package io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily

import io.github.landerlyoung.kotlin.mpp.zhihudaily.httpGet

/**
 * <pre>
 * Author: landerlyoung@gmail.com
 * Date:   2018-11-10
 * Time:   15:30
 * Life with Passion, Code with Creativity.
 * </pre>
 */
object ZhihuDailyRepository {
    fun getLatestNews(): String {
        return httpGet("https://news-at.zhihu.com/api/4/news/latest")
    }

    fun getNewsContent(newsId: Long): String {
        return httpGet("https://news-at.zhihu.com/api/4/news/$newsId")
    }
}