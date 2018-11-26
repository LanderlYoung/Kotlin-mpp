package io.github.landerlyoung.kotlin.mpp

import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import io.github.landerlyoung.kotlin.mpp.zhihudaily.LatestStories
import io.github.landerlyoung.kotlin.mpp.zhihudaily.Story
import io.github.landerlyoung.kotlin.mpp.zhihudaily.presenter.LatestStoryPresenter
import kotlinx.android.synthetic.main.fragment_item.view.*

class ZhihuDailyLatestStoriesFragment : Fragment() {

    private val adapter = LatestStoriesRecyclerViewAdapter()
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

    private val presenter = LatestStoryPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        presenter.onLoadingStatusChange = {
            if (it) {
                loadingProgress.show()
            } else {
                loadingProgress.hide()
            }
        }

        presenter.onLoadData = {
            adapter.setLatestStories(it)
        }

        presenter.onError = { e ->
            Snackbar.make(loadingProgress,
                    e.javaClass.simpleName + " " + e.message,
                    Snackbar.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false) as ViewGroup
        view.findViewById<RecyclerView>(R.id.list).let {
            it.adapter = adapter
            it.addItemDecoration(object : RecyclerView.ItemDecoration() {
                val margin = resources.getDimensionPixelSize(R.dimen.card_margin)
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.top = margin
                    outRect.bottom = margin
                }
            })
        }
        loadingProgress = view.findViewById(R.id.loading_progress_bar)
        return view
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
            actionBar.title = getString(R.string.app_name)
            actionBar.setDisplayHomeAsUpEnabled(false)
        }
    }

    inner class LatestStoriesRecyclerViewAdapter :
            RecyclerView.Adapter<LatestStoriesRecyclerViewAdapter.ViewHolder>() {

        private val latestStories = mutableListOf<Story>()

        fun setLatestStories(data: LatestStories) {
            latestStories.clear()
            latestStories.addAll(data.stories)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.story = latestStories[position]
        }

        override fun getItemCount(): Int = latestStories.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val cover: ImageView = view.cover
            private val title: TextView = view.title

            init {
                view.setOnClickListener {
                    story?.let { story ->
                        activity!!.supportFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                        R.anim.enter,
                                        R.anim.exit,
                                        R.anim.pop_enter,
                                        R.anim.pop_exit)
                                .replace(this@ZhihuDailyLatestStoriesFragment.id,
                                        StoryContentFragment.newInstance(story))
                                .addToBackStack(null)
                                .commit()
                    }
                }
            }

            var story: Story? = null
                set(value) {
                    field = value
                    title.text = value?.title
                    Glide.with(cover)
                            .load(value?.let { Uri.parse(it.image) })
                            .into(cover)
                }
        }
    }
}
