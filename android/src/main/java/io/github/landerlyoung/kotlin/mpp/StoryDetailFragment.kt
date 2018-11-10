package io.github.landerlyoung.kotlin.mpp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.webkit.WebView
import io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily.ZhihuDailyRepository
import io.github.landerlyoung.kotlin.mpp.zhihudaily.StoryContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

class StoryDetailFragment : Fragment() {

    private var storyId: Long = 0

    private lateinit var webView: WebView
    private lateinit var loadingProgress: ContentLoadingProgressBar

    private var loading: Boolean = false
        set(value) {
            if (value) {
                loadingProgress.show()
            } else {
                loadingProgress.hide()
            }
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyId = arguments?.getLong(KEL_STORY_ID) ?: throw IllegalArgumentException()
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_story_detail, container, false).also {
            loadingProgress = it.findViewById(R.id.loading_progress_bar)
            webView = it.findViewById(R.id.story_detail_webview)
            webView.settings.apply {
                javaScriptEnabled = true
                allowFileAccess = false
                allowContentAccess = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = true
        coroutineScope.launch(Dispatchers.Default) {
            var detail: StoryContent? = null
            try {
                detail = ZhihuDailyRepository.getStoryContent(storyId)
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    loading = false
                    Snackbar.make(loadingProgress, e.message ?: e.javaClass.simpleName, Snackbar.LENGTH_SHORT)
                            .show()
                }
            }
            if (detail != null) {
                withContext(Dispatchers.Main) {
                    loading = false
                    webView.loadDataWithBaseURL("x-data://base",
                            ZhihuDailyRepository.makeHtml(detail)
                                    .replace("<div class=\"img-place-holder\">", ""),
                            "text/html ",
                            "UTF-8",
                            null)
                }
            }
        }
    }

    companion object {
        private const val KEL_STORY_ID = "STORY_ID"

        @JvmStatic
        fun newInstance(storyId: Long) =
                StoryDetailFragment().apply {
                    arguments = Bundle().apply {
                        putLong(KEL_STORY_ID, storyId)
                    }
                }
    }
}
