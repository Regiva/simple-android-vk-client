package com.regiva.simple_vk_client.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.responses.newsfeed.Attachment
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        photo: Attachment,
        openPhotosClick: (List<Attachment>) -> Unit,
        photos: List<Attachment>
    ) {
        itemView.setOnClickListener { openPhotosClick(photos) }
        Glide.with(itemView.context)
            .load(photo.photo?.photo_604 ?: photo.photo?.photo_130 ?: photo.photo?.photo_807 ?: photo.photo?.photo_1280 ?: photo.photo?.photo_75)
            .placeholder(R.drawable.ic_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(itemView.iv_photo)
    }
}