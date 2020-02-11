package com.regiva.simple_vk_client.ui.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.util.applyDiff

class PostsAdapter(
    private var list: List<PostModel>,
    private val likeClick: (PostModel) -> Unit,
    private val goToDetailedPost: (PostModel) -> Unit
) : RecyclerView.Adapter<PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_post,
                parent,
                false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PostHolder, position: Int) =
        holder.bind(
            list[position],
            likeClick,
            goToDetailedPost
        )

    fun updateList(newList: List<PostModel>) {
        applyDiff(
            oldList = list,
            newList = newList,
            areItemsTheSame = { old, new -> old.post_id == new.post_id },
            areContentsTheSame = { old, new -> (old == new) && (old.isLiked == new.isLiked) }
        )
        this.list = newList
    }
}