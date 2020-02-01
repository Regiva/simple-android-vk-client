package com.regiva.simple_vk_client.entity

data class PostModel(
    val source_id: Long,
    val date: Long,
    val text: String,
    val attachments: List<Attachment>,
    val comments: Comments?,
    val likes: Likes?,
    val post_id: Long
)