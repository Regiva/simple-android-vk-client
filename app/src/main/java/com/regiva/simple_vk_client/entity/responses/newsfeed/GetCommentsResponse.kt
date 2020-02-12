package com.regiva.simple_vk_client.entity.responses.newsfeed

data class GetCommentsResponse(
    val items: List<CommentResponseModel>
)

data class CommentResponseModel(
    val id: Long,
    val date: Long,
    val text: String,
    val attachments: List<Attachment>?,
    val thread: Thread?
)

data class Thread(
    val items: List<CommentResponseModel>
)