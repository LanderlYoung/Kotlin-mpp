package io.github.landerlyoung.kotlin.mpp.zhihudaily.presenter

import io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily.StoryContentRenderer
import io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily.ZhihuDailyRepository
import io.github.landerlyoung.kotlin.mpp.zhihudaily.LatestStories
import io.github.landerlyoung.kotlin.mpp.zhihudaily.StoryContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

/**
 * <pre>
 * Author: landerlyoung@gmail.com
 * Date:   2018-11-13
 * Time:   16:04
 * Life with Passion, Code with Creativity.
 * </pre>
 */
open class LifeCyclePresenter<T> {
    private val job = Job()
    val coroutineScope: CoroutineScope = CoroutineScope(job)

    var onLoadingStatusChange: ((Boolean) -> Unit)? = null

    var onLoadData: ((data: T) -> Unit)? = null

    var onError: ((error: Throwable) -> Unit)? = null

    var loading: Boolean = false
        set(value) {
            onLoadingStatusChange?.invoke(value)
            field = loading
        }

    var data: T? = null
        set(value) {
            if (value != null) {
                onLoadData?.invoke(value)
            }
            field = value
        }

    var error: Throwable? = null
        set(value) {
            if (value != null) {
                onError?.invoke(value)
            }
            field = value
        }

    open fun onCreate() {
    }

    open fun onDestroy() {
        job.cancel()
    }
}

class LatestStoryPresenter : LifeCyclePresenter<LatestStories>() {
    override fun onCreate() {
        super.onCreate()

        coroutineScope.launch(Dispatchers.Default) {
            var latestStories: LatestStories? = null
            loading = true
            try {
                latestStories = ZhihuDailyRepository.getLatestStories()
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    loading = false
                    error = e
                }
            }
            if (latestStories != null) {
                withContext(Dispatchers.Main) {
                    loading = false
                    data = latestStories
                }
            }
        }
    }
}

class StoryContentPresenter(private val storyId: Long) : LifeCyclePresenter<Pair<StoryContent, String>>() {
    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch(Dispatchers.Default) {
            var detail: StoryContent? = null
            try {
                detail = ZhihuDailyRepository.getStoryContent(storyId)
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    loading = false
                    error = e
                }
            }
            if (detail != null) {
                withContext(Dispatchers.Main) {
                    loading = false
                    data = detail to StoryContentRenderer.makeHtml(detail)
                }
            }
        }
    }
}