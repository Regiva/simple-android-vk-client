package com.regiva.simple_vk_client.ui.home.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.responses.newsfeed.CommentResponseModel
import com.regiva.simple_vk_client.util.applyDiff

class CommentsAdapter(
    private var list: List<CommentResponseModel>
) : RecyclerView.Adapter<CommentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CommentHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment,
                parent,
                false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CommentHolder, position: Int) = holder.bind(list[position])

    fun updateList(newList: List<CommentResponseModel>) {
        applyDiff(
            oldList = list,
            newList = newList,
            areItemsTheSame = { old, new -> old.id == new.id },
            areContentsTheSame = { old, new -> old == new }
        )
        this.list = newList
    }
}