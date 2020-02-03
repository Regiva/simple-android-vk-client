package com.regiva.simple_vk_client.entity.newsfeed

import com.regiva.simple_vk_client.entity.responses.newsfeed.Attachment

data class PostModel(
    val source: PostSourceModel,
    val date: Long,
    val text: String?,
    val attachments: List<Attachment>?,
    val comment_count: Int,
    val likes_count: Int,
    val post_id: Long
)

data class PostSourceModel(
    val id: Long,
    val name: String,
    val photo: String
)