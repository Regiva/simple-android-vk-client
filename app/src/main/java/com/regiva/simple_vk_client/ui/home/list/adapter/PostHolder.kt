package com.regiva.simple_vk_client.ui.home.list.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.entity.responses.newsfeed.Attachment
import com.regiva.simple_vk_client.util.convertToDateFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_post.*

class PostHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        data: PostModel,
        likeClick: (PostModel) -> Unit,
        goToDetailedPost: (PostModel) -> Unit
    ) {
        data.attachments?.let { initPhotos(it) }
        containerView.setOnClickListener { goToDetailedPost(data) }
        tv_name.text = data.source.name
        tv_date.text = data.date.convertToDateFormat()
        tv_post_text.text = data.text
        tv_likes_count.text = data.likes_count.toString()
        tv_comments_count.text = data.comment_count.toString()
        iv_like.setOnClickListener { likeClick(data) }
        Glide.with(containerView.context)
            .load(if (data.isLiked) {
                Log.d("rere", "liked")
                R.drawable.ic_like_active
            }
                else R.drawable.ic_like
            )
            .into(iv_like)
        /*if (data.comment.isNotBlank())
            tv_doc_desription.text = data.comment
        else
            tv_doc_desription?.visibility = View.GONE*/

        Glide.with(containerView.context)
            .load(data.source.photo)
            //todo placeholder
            .apply(RequestOptions().circleCrop())
            .into(iv_avatar)

//        Glide.with(containerView.context)
//            .load(data.attachments?.first())
//            .into(itemView.iv_avatar)
    }

    private fun initPhotos(attachments: List<Attachment>) {
        with(attachments.filter { it.type == "photo" }) {
            if (this.size == 1) {
                rv_photos.layoutManager = GridLayoutManager(containerView.context, 1)
                rv_photos.adapter = PhotosAdapter(attachments.filter { it.type == "photo" })
            }
            else {
                rv_photos.layoutManager = GridLayoutManager(containerView.context, 3)
                rv_photos.adapter = PhotosAdapter(attachments.filter { it.type == "photo" })
            }
        }
    }
}