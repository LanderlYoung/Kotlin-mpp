package io.github.landerlyoung.kotlin.mpp.zhihudaily

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * <pre>
 * Author: landerlyoung@gmail.com
 * Date:   2018-11-10
 * Time:   15:46
 * Life with Passion, Code with Creativity.
 * </pre>
 */

@Serializable
data class LatestStories(
        @Optional
        val date: Long = 0,
        @Optional
        val stories: List<Story> = emptyList(),
        @Optional
        val top_stories: List<Story> = emptyList()
)

@Serializable
data class Story(
        @Optional
        val id: Long = 0,
        @Optional
        val title: String = "",
        @Optional
        val type: Int = 0,
        @Optional
        val ga_prefix: Int = 0,
        @Optional
        @SerialName("image")
        private val _image: String? = null,
        @Optional
        val images: List<String> = emptyList()
) {
    @Transient
    val image: String?
        get() = _image ?: images.firstOrNull()
}

@Serializable
data class StoryContent(
        @Optional
        val id: Long = 0,
        @Optional
        val type: Int = 0,
        @Optional
        val title: String = "",
        @Optional
        val body: String = "",
        @Optional
        val js: List<String> = emptyList(),
        @Optional
        val css: List<String> = emptyList(),
        @Optional
        val share_url: String = "",
        @Optional
        val recommenders: List<String> = emptyList(),
        @Optional
        val ga_prefix: Int = 0,
        @Optional
        val image_source: String = "",
        @Optional
        @SerialName("image")
        private val _image: String? = null,
        @Optional
        val images: List<String> = emptyList()
) {
    @Transient
    val image: String?
        get() = _image ?: images.firstOrNull()
}
