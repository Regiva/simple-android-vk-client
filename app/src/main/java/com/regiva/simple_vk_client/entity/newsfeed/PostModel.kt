package com.regiva.simple_vk_client.entity.newsfeed

import android.os.Parcelable
import com.regiva.simple_vk_client.entity.responses.newsfeed.Attachment
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class PostModel(
    val source: @RawValue PostSourceModel,
    val date: Long,
    val text: String?,
    val attachments: @RawValue List<Attachment>?,
    val comment_count: Int,
    val likes_count: Int,
    val post_id: Long,
    var isLiked: Boolean = false
) : Parcelable

data class PostSourceModel(
    val id: Long,
    val name: String,
    val photo: String
)