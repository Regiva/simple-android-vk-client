package com.regiva.simple_vk_client.entity.responses.newsfeed

data class Attachment(
    val type: String,
    val video: Type.Video?,
    val photo: Type.Photo?
)

sealed class Type {
    object Video : Type()
    object Photo : Type()
}