package com.regiva.simple_vk_client.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.responses.newsfeed.Attachment
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        position: Int,
        photo: Attachment,
        photos: List<Attachment>
    ) {
        containerView.setOnClickListener {
            ImageViewer.Builder(containerView.context, photos.map { it.photo?.photo_604 })
                .setStartPosition(position)
                .show()
        }
        Glide.with(containerView.context)
            .load(photo.photo?.photo_604 ?: photo.photo?.photo_130 ?: photo.photo?.photo_807 ?: photo.photo?.photo_1280 ?: photo.photo?.photo_75)
            .placeholder(R.drawable.ic_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(containerView.iv_photo)
    }
}