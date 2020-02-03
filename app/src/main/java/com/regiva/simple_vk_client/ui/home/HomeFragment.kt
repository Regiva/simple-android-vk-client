package com.regiva.simple_vk_client.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.entity.responses.newsfeed.PostResponseModel
import com.regiva.simple_vk_client.model.data.feature.PostsFeature
import com.regiva.simple_vk_client.model.system.FlowRouter
import com.regiva.simple_vk_client.ui.base.MviFragment
import com.regiva.simple_vk_client.ui.home.adapter.PostsAdapter
import com.regiva.simple_vk_client.util.setGone
import com.regiva.simple_vk_client.util.setLoadingState
import com.regiva.simple_vk_client.util.setVisible
import com.stfalcon.frescoimageviewer.ImageViewer
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
            {  },
            {
                ImageViewer.Builder(context, it.map { it.photo })
                    .setStartPosition(0)
                    .show()
            }
        )
    }

    private var posts: List<PostResponseModel>? = null
//    private var person: PersonResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBindings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        initRecycler()
    }

    override fun onResume() {
        super.onResume()
        Log.d("rere", "onresume")
        onNext(UiEvents.OnStart)
    }

    private fun setUpBindings() {
        object : AndroidBindings<HomeFragment>(this) {
            override fun setup(view: HomeFragment) {
                binder.bind(view to feature using { event ->
                    when (event) {
                        is UiEvents.OnStart -> {
                            Log.d("rere", "event")
                            PostsFeature.Wish.GetAllPosts
                        }
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
        pb_loading?.setLoadingState(vm.isLoading)
        vm.posts?.let { posts ->
//            docs.forEach { it.favorite = person?.favourite_document_ids?.contains(it.id) ?: false }
            Log.d("rere", "accept")
            showPosts(posts)
        }
    }

    sealed class UiEvents {
        object OnStart : UiEvents()
    }

    class ViewModel(
        val isLoading: Boolean,
        val posts: List<PostModel>?
    )
}

/*
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
          "count": 0,
          "can_post": 1,
          "groups_can_post": true
        },
        "likes": {
          "count": 0,
          "user_likes": 0,
          "can_like": 1,
          "can_publish": 1
        },
        "reposts": {
          "count": 0,
          "user_reposted": 0
        },
        "post_id": 10749
      }*/
