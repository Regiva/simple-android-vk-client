package com.regiva.simple_vk_client.entity.responses.newsfeed

data class GetCommentsResponse(
    val items: List<CommentResponseModel>//todo
//    val profiles: List<ProfileModel>,
//    val groups: List<GroupModel>,
//    val next_from: String
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