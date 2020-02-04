package com.regiva.simple_vk_client.ui.home.detail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.regiva.simple_vk_client.entity.responses.newsfeed.CommentResponseModel
import com.regiva.simple_vk_client.util.convertToDateFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        data: CommentResponseModel
    ) {
//        data.attachments?.let { initPhotos(it) }
        //todo containerView.tv_name.text = data.source.name
        containerView.tv_date.text = data.date.convertToDateFormat()
        containerView.tv_comment_text.text = data.text
        /*if (data.comment.isNotBlank())
            tv_doc_desription.text = data.comment
        else
            tv_doc_desription?.visibility = View.GONE*/

//        Glide.with(containerView.context)
//            .load(data.)
            ////todo placeholder
//            .apply(RequestOptions().circleCrop())
//            .into(itemView.iv_avatar)
    }

    /*private fun initPhotos(attachments: List<Attachment>) {
        with(attachments.filter { it.type == "photo" }) {
            Log.d("rere", "size = ${this.size}")
            *//* if (this.isNullOrEmpty()) {
                 containerView.rv_photos.setGone()
             }
             else *//*
            if (this.size == 1) {
            //todo
            Log.d("rere", "blya ya zdes!!!")
//                containerView.rv_photos.recycledViewPool.setMaxRecycledViews(0, 0)
            containerView.rv_photos.layoutManager = GridLayoutManager(containerView.context, 1)
            containerView.rv_photos.adapter = PhotosAdapter(attachments.filter { it.type == "photo" })
//                Glide.with(containerView.context)
//                    .load(this[0].photo?.photo_604 ?: this[0].photo?.photo_807 ?: this[0].photo?.photo_130)
//                    .into(containerView.iv_single_photo)
            }
            else {
                containerView.rv_photos.layoutManager = GridLayoutManager(containerView.context, 3)
                containerView.rv_photos.adapter = PhotosAdapter(attachments.filter { it.type == "photo" })
            }
        }
    }*/
}