package io.github.landerlyoung.kotlin.mpp

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily.ZhihuDailyRepository
import io.github.landerlyoung.kotlin.mpp.zhihudaily.LatestStories
import io.github.landerlyoung.kotlin.mpp.zhihudaily.Story
import kotlinx.android.synthetic.main.fragment_item.view.cover
import kotlinx.android.synthetic.main.fragment_item.view.title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false) as ViewGroup
        view.findViewById<RecyclerView>(R.id.list).adapter = adapter
        loadingProgress = view.findViewById(R.id.loading_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope.launch(Dispatchers.IO) {
            var latestStories: LatestStories? = null
            loading = true
            try {
                latestStories = ZhihuDailyRepository.getLatestStories()
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    loading = false
                    Snackbar.make(loadingProgress, e.message ?: e.javaClass.simpleName, Snackbar.LENGTH_SHORT)
                            .show()
                }
            }
            if (latestStories != null) {
                withContext(Dispatchers.Main) {
                    loading = false
                    adapter.setLatestStories(latestStories)
                }
            }
        }

    }

    class LatestStoriesRecyclerViewAdapter :
            RecyclerView.Adapter<LatestStoriesRecyclerViewAdapter.ViewHolder>() {

        private val latestStories = mutableListOf<Story>()

        fun setLatestStories(data: LatestStories) {
            latestStories.clear()
            latestStories.addAll(data.top_stories)
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

            var story: Story? = null
                set(value) {
                    title.text = value?.title
                    Glide.with(cover)
                            .load(value?.let { Uri.parse(it.image) })
                            .into(cover)
                }
        }
    }
}
