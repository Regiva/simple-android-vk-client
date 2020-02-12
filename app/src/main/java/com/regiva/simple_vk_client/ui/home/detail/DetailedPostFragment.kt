package com.regiva.simple_vk_client.ui.home.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.modelWatcher
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.entity.responses.newsfeed.CommentResponseModel
import com.regiva.simple_vk_client.model.data.feature.DetailedPostFeature
import com.regiva.simple_vk_client.model.system.FlowRouter
import com.regiva.simple_vk_client.ui.base.MviFragment
import com.regiva.simple_vk_client.ui.home.detail.adapter.CommentsAdapter
import com.regiva.simple_vk_client.ui.home.list.adapter.PhotosAdapter
import com.regiva.simple_vk_client.util.ErrorHandler
import com.regiva.simple_vk_client.util.convertToDateFormat
import com.regiva.simple_vk_client.util.setLoadingState
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_detailed_post.*

class DetailedPostFragment : MviFragment<DetailedPostFragment.ViewModel, DetailedPostFragment.UiEvents>() {

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

    private val flowRouter: FlowRouter? by scope()
    private val feature: DetailedPostFeature by scope()
    private val errorHandler: ErrorHandler by scope()
    private val photosAdapter: PhotosAdapter by lazy {
        PhotosAdapter(post?.attachments?.filter { it.type == "photo" } ?: listOf())
    }

    private val commentsAdapter: CommentsAdapter by lazy {
        CommentsAdapter(listOf())
    }

    private var post: PostModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(EXTRA_POST) == true) {
            arguments?.getParcelable<PostModel>(EXTRA_POST)?.let { post = it }
            setUpBindings()
        }
        else showError(getString(R.string.error_was_occurred))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        initPhotosRecycler()
        initCommentsRecycler()
        post?.let { setUpScreen(it) }
    }

    override fun onResume() {
        super.onResume()
        onNext(UiEvents.OnStart)
    }

    private fun setUpScreen(post: PostModel) {
        tv_name.text = post.source.name
        tv_date.text = post.date.convertToDateFormat()
        tv_post_text.text = post.text
        tv_likes_count.text = post.likes_count.toString()
        tv_comments_count.text = with (post.comment_count) {
            if (this == 1) getString(R.string.single_comment)
            else "$this comments"
        }

        Glide.with(context!!)
            .load(post.source.photo)
            .placeholder(R.mipmap.ic_placeholder_user)
            .apply(RequestOptions().circleCrop())
            .into(iv_avatar)
    }

    private fun setUpBindings() {
        object : AndroidBindings<DetailedPostFragment>(this) {
            override fun setup(view: DetailedPostFragment) {
                binder.bind(view to feature using { event ->
                    when (event) {
                        is UiEvents.OnStart -> DetailedPostFeature.Wish.GetComments(post!!)
                    }
                })
                binder.bind(feature to view using { state ->
                    ViewModel(
                        state.post,
                        state.isLoading,
                        state.comments
                    )
                })
                binder.bind(feature.news to Consumer { news ->
                    when (news) {
                        is DetailedPostFeature.News.GetCommentsFailure -> errorHandler.proceed(news.throwable) { view.showError(it) }
                    }
                })
            }
        }.setup(this)
    }

    private fun initPhotosRecycler() {
        val spanCount = if (post?.attachments?.size == 1) 1 else 3
        rv_photos.layoutManager = GridLayoutManager(context, spanCount)
        rv_photos.adapter = photosAdapter
    }

    private fun initCommentsRecycler() {
        rv_comments.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_comments.adapter = commentsAdapter
    }

    private fun setOnClickListeners() {
        toolbar.setNavigationOnClickListener { flowRouter?.exit() }
    }

    private fun showComments(comments: List<CommentResponseModel>) {
        commentsAdapter.updateList(comments)
    }

    override fun accept(vm: ViewModel) {
        modelWatcher<ViewModel> {
            watch(ViewModel::isLoading) { pb_loading?.setLoadingState(it) }
            watch(ViewModel::comments) { it?.let { showComments(it) } }
            watch(ViewModel::post) { it?.let { post = it } }
        }.invoke(vm)
    }

    sealed class UiEvents {
        object OnStart : UiEvents()
    }

    class ViewModel(
        val post: PostModel?,
        val isLoading: Boolean,
        val comments: List<CommentResponseModel>?
    )
}