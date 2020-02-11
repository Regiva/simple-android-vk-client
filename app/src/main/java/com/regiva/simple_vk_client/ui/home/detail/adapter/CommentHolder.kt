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
        //todo containerView.tv_name.text = data.source.name
        containerView.tv_date.text = data.date.convertToDateFormat()
        containerView.tv_comment_text.text = data.text
    }
}