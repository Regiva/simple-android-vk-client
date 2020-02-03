package com.regiva.simple_vk_client.ui.home.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.entity.responses.newsfeed.Attachment
import com.regiva.simple_vk_client.util.convertToDateFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_post.view.*

class PostHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        data: PostModel,
        likeClick: (PostModel) -> Unit
    ) {
        data.attachments?.let { initPhotos(it) }
        containerView.tv_name.text = data.source.name
        containerView.tv_date.text = data.date.convertToDateFormat()
        containerView.tv_post_text.text = data.text
        containerView.tv_likes_count.text = data.likes_count.toString()
        containerView.tv_comments_count.text = data.comment_count.toString()
//        cb_like.isChecked = data.favorite
//        cb_like.setOnClickListener { likeClick(data) }
        /*if (data.comment.isNotBlank())
            tv_doc_desription.text = data.comment
        else
            tv_doc_desription?.visibility = View.GONE*/

        Glide.with(containerView.context)
            .load(data.source.photo)
            //todo placeholder
            .apply(RequestOptions().circleCrop())
            .into(itemView.iv_avatar)

//        Glide.with(containerView.context)
//            .load(data.attachments?.first())
//            .into(itemView.iv_avatar)
    }

    private fun initPhotos(photos: List<Attachment>) {
        with(photos.filter { it.type == "photo" }) {
            Log.d("rere", "size = ${this.size}")
           /* if (this.isNullOrEmpty()) {
                containerView.rv_photos.setGone()
            }
            else */if (this.size == 1) {
            //todo
                Log.d("rere", "blya ya zdes!!!")
//                containerView.rv_photos.recycledViewPool.setMaxRecycledViews(0, 0)
                containerView.rv_photos.layoutManager = GridLayoutManager(containerView.context, 1)
                containerView.rv_photos.adapter = PhotosAdapter(photos.filter { it.type == "photo" })
//                Glide.with(containerView.context)
//                    .load(this[0].photo?.photo_604 ?: this[0].photo?.photo_807 ?: this[0].photo?.photo_130)
//                    .into(containerView.iv_single_photo)
            }
            else {
//                containerView.iv_single_photo.setGone()
//                containerView.rv_photos.recycledViewPool.setMaxRecycledViews(0, 0)
                containerView.rv_photos.layoutManager = GridLayoutManager(containerView.context, 3)
                containerView.rv_photos.adapter = PhotosAdapter(photos.filter { it.type == "photo" })
            }
        }
        /*if (photos.any { it.type == "photo" }) {
            containerView.rv_photos.layoutManager = GridLayoutManager(containerView.context, 2)
            containerView.rv_photos.adapter = PhotosAdapter(photos.filter { it.type == "photo" }) {}
        }
        else {
            containerView.rv_photos.visibility = View.GONE
        }*/
    }
}