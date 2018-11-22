package io.github.landerlyoung.kotlin.mpp.zhihudaily.presenter

import io.github.landerlyoung.kotlin.mpp.MyDispatchers
import io.github.landerlyoung.kotlin.mpp.zhihudaily.LatestStories
import io.github.landerlyoung.kotlin.mpp.zhihudaily.StoryContent
import io.github.landerlyoung.kotlin.mpp.zhihudaily.StoryContentRenderer
import io.github.landerlyoung.kotlin.mpp.zhihudaily.ZhihuDailyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    val coroutineScope: CoroutineScope = CoroutineScope(job + MyDispatchers.Main)

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

    open fun onActivate() {
    }

    open fun onDeactivate() {
        job.cancel()
    }
}

class LatestStoryPresenter : LifeCyclePresenter<LatestStories>() {
    override fun onActivate() {
        super.onActivate()

        coroutineScope.launch {
            loading = true
            try {
                val latestStories = ZhihuDailyRepository.getLatestStories()
                withContext(MyDispatchers.Main) {
                    loading = false
                    data = latestStories
                }
            } catch (e: Exception) {
                withContext(MyDispatchers.Main) {
                    loading = false
                    error = e
                }
            }
        }
    }
}

class StoryContentPresenter(private val storyId: Long) : LifeCyclePresenter<Pair<StoryContent, String>>() {
    override fun onActivate() {
        super.onActivate()
        coroutineScope.launch {
            loading = true
            try {
                val detail = ZhihuDailyRepository.getStoryContent(storyId)
                withContext(MyDispatchers.Main) {
                    loading = false
                    data = detail to StoryContentRenderer.makeHtml(detail)
                }
            } catch (e: Exception) {
                withContext(MyDispatchers.Main) {
                    loading = false
                    error = e
                }
            }
        }
    }
}