package com.regiva.simple_vk_client.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.regiva.simple_vk_client.entity.PostModel
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
//        initPhotos(data.photos)
        containerView.tv_name.text = data.source_id.toString()
        containerView.tv_date.text = data.date.convertToDateFormat()
        containerView.tv_post_text.text = data.text
        containerView.tv_likes_count.text = data.likes?.count.toString()
        containerView.tv_comments_count.text = data.comments?.count.toString()
//        cb_like.isChecked = data.favorite
//        cb_like.setOnClickListener { likeClick(data) }
        /*if (data.comment.isNotBlank())
            tv_doc_desription.text = data.comment
        else
            tv_doc_desription?.visibility = View.GONE*/

        /*Glide.with(containerView.context)
            .load(Constants.Api.BASE_URL_PHOTOS + data.person.photo)
            .placeholder(R.drawable.ic_person_avatar_placeholder)
            .apply(RequestOptions().circleCrop())
            .into(itemView.iv_avatar)*/
    }

    /*private fun initPhotos(photos: List<SingleIdModel>) {
        if (photos.size == 1) tl_doc_photos_tabs.setInvisible()

        val docPhotosAdapter = DocPhotosAdapter(photos)
        vp_doc_photos.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vp_doc_photos.adapter = docPhotosAdapter

        TabLayoutMediator(tl_doc_photos_tabs, vp_doc_photos,
            TabLayoutMediator.TabConfigurationStrategy { _, _ -> }).attach()
    }*/
}