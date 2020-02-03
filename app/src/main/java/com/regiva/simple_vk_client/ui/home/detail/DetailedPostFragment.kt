package com.regiva.simple_vk_client.ui.home.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.model.system.FlowRouter
import com.regiva.simple_vk_client.ui.base.BaseFragment
import com.regiva.simple_vk_client.ui.home.list.adapter.PhotosAdapter
import com.regiva.simple_vk_client.util.convertToDateFormat
import kotlinx.android.synthetic.main.fragment_detailed_post.*

class DetailedPostFragment : BaseFragment()/*MviFragment<DetailedPostFragment.ViewModel, DetailedPostFragment.UiEvents>()*/ {

    companion object {
        fun create(post: PostModel) =
            DetailedPostFragment().apply {
                arguments = bundleOf(
                    EXTRA_POST to post
                )
            }

        private const val EXTRA_POST = "post"
    }

    override val layoutRes: Int
        get() = R.layout.fragment_detailed_post

    private val flowRouter: FlowRouter by scope()
//    private val feature: PostsFeature by scope()
    //    private val errorHandler: ErrorHandler by scope()
//    private val messageHandler: MessageHandler by scope()
    private val adapter: PhotosAdapter by lazy {
        PhotosAdapter(post?.attachments?.filter { it.type == "photo" } ?: listOf())
    }

    private var post: PostModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(EXTRA_POST) == true) {
            arguments?.getParcelable<PostModel>(EXTRA_POST)?.let { post = it }
        }
//        setUpBindings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        initRecycler()
        post?.let { setUpScreen(it) }
    }

    private fun setUpScreen(post: PostModel) {
        tv_name.text = post.source.name
        tv_date.text = post.date.convertToDateFormat()
        tv_post_text.text = post.text
        tv_likes_count.text = post.likes_count.toString()
        tv_comments_count.text = post.comment_count.toString()

        Glide.with(context!!)
            .load(post.source.photo)
            //todo placeholder
            .apply(RequestOptions().circleCrop())
            .into(iv_avatar)
    }

    /*private fun setUpBindings() {
        object : AndroidBindings<DetailedPostFragment>(this) {
            override fun setup(view: HomeFragment) {
                binder.bind(view to feature using { event ->
                    when (event) {
                        is UiEvents.OnStart -> PostsFeature.Wish.GetAllPosts
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
    }*/

    private fun initRecycler() {
        val spanCount = if (post?.attachments?.size == 1) 1 else 3
        rv_photos.layoutManager = GridLayoutManager(context, spanCount)
        rv_photos.adapter = adapter
    }

    private fun setOnClickListeners() {
        toolbar.setNavigationOnClickListener { flowRouter.exit() }
    }

//    override fun accept(vm: ViewModel) {
//        pb_loading?.setLoadingState(vm.isLoading)
//        vm.posts?.let { showPosts(it) }
//    }

    sealed class UiEvents {
        object OnStart : UiEvents()
    }

    class ViewModel(
        val isLoading: Boolean
    )
}