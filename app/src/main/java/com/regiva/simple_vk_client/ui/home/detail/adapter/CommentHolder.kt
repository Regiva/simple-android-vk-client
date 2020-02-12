package com.regiva.simple_vk_client.ui.home.detail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.responses.newsfeed.CommentResponseModel
import com.regiva.simple_vk_client.util.convertToDateFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_comment.*

class CommentHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        data: CommentResponseModel
    ) {
        //todo containerView.tv_name.text = data.source.name
        tv_date.text = data.date.convertToDateFormat()
        tv_comment_text.text = data.text

        Glide.with(iv_avatar)
            .load(R.mipmap.ic_placeholder_user)
            .apply(RequestOptions().circleCrop())
            .into(iv_avatar)
    }
}