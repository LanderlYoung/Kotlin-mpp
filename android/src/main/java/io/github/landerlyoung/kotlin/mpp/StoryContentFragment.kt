package io.github.landerlyoung.kotlin.mpp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.webkit.WebView
import io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily.StoryContentRenderer
import io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily.ZhihuDailyRepository
import io.github.landerlyoung.kotlin.mpp.zhihudaily.Story
import io.github.landerlyoung.kotlin.mpp.zhihudaily.StoryContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

class StoryContentFragment : Fragment() {

    private var storyId: Long = 0
    private var storyTitle: String? = null

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
        storyTitle = arguments?.getString(KEL_STORY_TITLE)
        setHasOptionsMenu(true)

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
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress >= 100) {
                        loading = false
                    }
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
                    webView.loadDataWithBaseURL(null,
                            StoryContentRenderer.makeHtml(detail),
                            "text/html ",
                            "UTF-8",
                            null)

                    storyTitle = detail.title
                    activity?.invalidateOptionsMenu()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        (activity as AppCompatActivity?)?.supportActionBar?.let { actionBar ->
            actionBar.title = storyTitle
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    companion object {
        private const val KEL_STORY_ID = "STORY_ID"
        private const val KEL_STORY_TITLE = "STORY_ID_TITLE"

        @JvmStatic
        fun newInstance(storyId: Long) =
                StoryContentFragment().apply {
                    arguments = Bundle().apply {
                        putLong(KEL_STORY_ID, storyId)
                    }
                }

        @JvmStatic
        fun newInstance(story: Story) =
                StoryContentFragment().apply {
                    arguments = Bundle().apply {
                        putLong(KEL_STORY_ID, story.id)
                        putString(KEL_STORY_TITLE, story.title)
                    }
                }
    }
}
