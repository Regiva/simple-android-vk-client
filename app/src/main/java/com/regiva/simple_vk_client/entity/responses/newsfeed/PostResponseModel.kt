package com.regiva.simple_vk_client.entity.responses.newsfeed

data class PostResponseModel(
    val source_id: Long,
    val date: Long,
    val text: String?,
    val attachments: List<Attachment>?,
    val comments: Comments?,
    val likes: Likes?,
    val post_id: Long
)

data class Likes(
    val count: Int?
)

data class Comments(
    val count: Int
)