package com.regiva.simple_vk_client.ui.home.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.modelWatcher
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.Screens
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.model.data.feature.PostsFeature
import com.regiva.simple_vk_client.model.system.FlowRouter
import com.regiva.simple_vk_client.ui.base.MviFragment
import com.regiva.simple_vk_client.ui.home.list.adapter.PostsAdapter
import com.regiva.simple_vk_client.util.ErrorHandler
import com.regiva.simple_vk_client.util.setGone
import com.regiva.simple_vk_client.util.setLoadingState
import com.regiva.simple_vk_client.util.setVisible
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : MviFragment<HomeFragment.ViewModel, HomeFragment.UiEvents>() {

    override val layoutRes: Int
        get() = R.layout.fragment_home

    private val flowRouter: FlowRouter by scope()
    private val feature: PostsFeature by scope()
    private val errorHandler: ErrorHandler by scope()
    private val adapter: PostsAdapter by lazy {
        PostsAdapter(
            listOf(),
            { onNext(UiEvents.OnLikeClicked(it.isLiked, it.source.id, it.post_id)) },
            { flowRouter.navigateTo(Screens.DetailedPost(it)) }
        )
    }

    private var isLoadingMore: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBindings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        onNext(UiEvents.OnGetNewsFeed)
    }

    private fun setUpBindings() {
        object : AndroidBindings<HomeFragment>(this) {
            override fun setup(view: HomeFragment) {
                binder.bind(view to feature using { event ->
                    when (event) {
                        is UiEvents.OnGetNewsFeed -> PostsFeature.Wish.GetAllPosts
                        is UiEvents.OnLikeClicked -> PostsFeature.Wish.LikePost(event.isLiked, event.owner_id, event.item_id)
                    }
                })
                binder.bind(feature to view using { state ->
                    ViewModel(
                        state.isLoading,
                        state.posts
                    )
                })
                binder.bind(feature.news to Consumer { news ->
                    when (news) {
                        is PostsFeature.News.GetAllPostsFailure -> errorHandler.proceed(news.throwable) { view.showError(it) }
                        is PostsFeature.News.LikePostFailure -> errorHandler.proceed(news.throwable) { view.showError(it) }
                    }
                })
            }
        }.setup(this)
    }

    private fun initRecycler() {
        rv_posts.layoutManager = LinearLayoutManager(activity)
        rv_posts.adapter = adapter
        rv_posts.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = (recyclerView.layoutManager as LinearLayoutManager).itemCount
                val lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (isLoadingMore != true && lastVisibleItem == totalItemCount - 1) {
                    onNext(UiEvents.OnGetNewsFeed)
                    isLoadingMore = true
                }
            }

        })
    }

    private fun showPosts(posts: List<PostModel>) {
        if (posts.isNotEmpty()) {
            adapter.updateList(posts)
            rl_posts_not_empty?.setVisible()
            rl_posts_empty?.setGone()
        }
        else {
            rl_posts_empty?.setVisible()
            rl_posts_not_empty?.setGone()
        }
    }

    override fun accept(vm: ViewModel) {
        modelWatcher<ViewModel> {
            watch(ViewModel::isLoading) { pb_loading?.setLoadingState(it) }
            watch(ViewModel::posts) {
                it?.let { showPosts(it) }
                isLoadingMore = false
            }
        }.invoke(vm)
    }

    sealed class UiEvents {
        object OnGetNewsFeed : UiEvents()
        data class OnLikeClicked(val isLiked: Boolean, val owner_id: Long, val item_id: Long) : UiEvents()
    }

    class ViewModel(
        val isLoading: Boolean,
        val posts: List<PostModel>?
    )
}