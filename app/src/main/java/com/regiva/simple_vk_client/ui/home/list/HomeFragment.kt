package com.regiva.simple_vk_client.ui.home.list

import android.os.Bundle
import android.util.Log
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
//    private val errorHandler: ErrorHandler by scope()
//    private val messageHandler: MessageHandler by scope()
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
        setOnClickListeners()
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
                        is PostsFeature.News.GetAllPostsFailure -> {} //errorHandler.proceed(news.throwable) { view.showError(it) }
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
                Log.d("rere", "scroll")
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = (recyclerView.layoutManager as LinearLayoutManager).itemCount
                val lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (isLoadingMore != true && lastVisibleItem == totalItemCount - 1) {
                    Log.d("rere", "scroll!!!!!!!!")
                    onNext(UiEvents.OnGetNewsFeed)
                    isLoadingMore = true
                }
            }

        })
//        rv_posts.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    private fun setOnClickListeners() {
        //todo
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
            watch(ViewModel::posts) { it?.let { showPosts(it) } }
        }.invoke(vm)
//        pb_loading?.setLoadingState(vm.isLoading)
//        vm.posts?.let {
//            TODO
//            Log.d("rere", "ZDES YOPTA")
//            showPosts(it)
//        }
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

/* todo
* {
        "can_doubt_category": false,
        "can_set_category": false,
        "type": "post",
        "source_id": 17216517,
        "date": 1580424823,
        "post_type": "post",
        "text": "Секунда юмора😂",
        "attachments": [
          {
            "type": "video",
            "video": {
              "access_key": "d725016445e11736d8",
              "can_comment": 1,
              "can_like": 1,
              "can_repost": 1,
              "can_subscribe": 1,
              "can_add_to_faves": 1,
              "can_add": 1,
              "comments": 0,
              "date": 1580424812,
              "description": "",
              "duration": 20,
              "photo_130": "https://sun9-55.userapi.com/c853620/v853620367/1e5388/WmZB3pnD74E.jpg",
              "photo_320": "https://sun9-70.userapi.com/c853620/v853620367/1e5386/wmcowvY0EqE.jpg",
              "photo_800": "https://sun9-19.userapi.com/c853620/v853620367/1e5380/HfleKRw1Ls0.jpg",
              "photo_1280": "https://sun9-42.userapi.com/c853620/v853620367/1e5381/EuAeqMDY17k.jpg",
              "first_frame_130": "https://sun9-67.userapi.com/c854224/v854224367/1e6562/Q0lkYy6dBoE.jpg",
              "first_frame_160": "https://sun9-18.userapi.com/c854224/v854224367/1e6561/_sqM8ShTzQk.jpg",
              "first_frame_320": "https://sun9-66.userapi.com/c854224/v854224367/1e6560/Y2PDdrzIjUo.jpg",
              "first_frame_800": "https://sun9-52.userapi.com/c854224/v854224367/1e655a/FIOcnOq7STI.jpg",
              "first_frame_1280": "https://sun9-27.userapi.com/c854224/v854224367/1e655b/tYjEaz8QAX8.jpg",
              "width": 1280,
              "height": 592,
              "id": 456239081,
              "owner_id": 17216517,
              "title": "Без названия",
              "track_code": "video_0542af46I0AvuxF88CIaUdKiI2QZPQgQ5aA7ydJMqzT_Zcj4F9EQZUG9E3ryLsz4F1UQUisOMSA",
              "views": 28
            }
          }
        ],
        "post_source": {
          "type": "api",
          "platform": "iphone"
        },
        "comments": {
          "likes": 0,
          "can_post": 1,
          "groups_can_post": true
        },
        "likes": {
          "likes": 0,
          "user_likes": 0,
          "can_like": 1,
          "can_publish": 1
        },
        "reposts": {
          "likes": 0,
          "user_reposted": 0
        },
        "post_id": 10749
      }*/
