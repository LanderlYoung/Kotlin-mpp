package io.github.landerlyoung.kotlin.mpp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.github.landerlyoung.kotlin.mpp.zhihudaily.Story
import io.github.landerlyoung.kotlin.mpp.zhihudaily.presenter.StoryContentPresenter

class StoryContentFragment : Fragment() {

    private var storyId: Long = 0
    private var storyTitle: String? = null

    private lateinit var webView: WebView
    private lateinit var loadingProgress: ContentLoadingProgressBar

    private lateinit var presenter: StoryContentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyId = arguments?.getLong(KEL_STORY_ID) ?: throw IllegalArgumentException()
        storyTitle = arguments?.getString(KEL_STORY_TITLE)
        setHasOptionsMenu(true)

        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        presenter = StoryContentPresenter(storyId)

        presenter.onLoadingStatusChange = {
            if (it) {
                loadingProgress.show()
            } else {
                loadingProgress.hide()
            }
        }

        presenter.onLoadData = { (storyContent, html) ->
            webView.loadDataWithBaseURL(null,
                    html,
                    "text/html ",
                    "UTF-8",
                    null)

            storyTitle = storyContent.title
            activity?.invalidateOptionsMenu()
        }

        presenter.onError = { e ->
            Snackbar.make(loadingProgress,
                    e.javaClass.simpleName + " " + e.message,
                    Snackbar.LENGTH_SHORT)
                    .show()
            Log.i("young", "error", e)
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
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onActivate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDeactivate()
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
