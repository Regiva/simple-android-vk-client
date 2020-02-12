package com.regiva.simple_vk_client.entity.responses.newsfeed

data class Attachment(
    val type: String,
    val video: Type.Video?,
    val photo: Type.Photo?
)

sealed class Type {
    data class Photo(
        val id: String,
        val photo_75: String?,
        val photo_130: String?,
        val photo_604: String?,
        val photo_807: String?,
        val photo_1280: String?,
        val width: Int,
        val height: Int,
        val text: String,
        val date: Long,
        val access_key: String
    ) : Type()
    data class Video(
        val access_key: String,
        val comments: Int,
        val date: Long,
        val description: String,
        val duration: Int,
        val photo_130: String,
        val photo_320: String,
        val photo_640: String,
        val photo_800: String,
        val id: Long,
        val owner_id: Long,
        val title: String,
        val track_code: String,
        val platform: String
    ) : Type()
}