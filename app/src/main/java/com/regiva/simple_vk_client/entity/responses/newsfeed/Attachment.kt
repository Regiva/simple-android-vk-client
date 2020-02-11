package com.regiva.simple_vk_client.entity.responses.newsfeed

data class Attachment(
    val type: String,
    val video: Type.Video?,
    val photo: Type.Photo?
)

sealed class Type {
    data class Photo(
        val id: String,
//        val album_id": -7,
//        val owner_id": -9884911,
//        val user_id": 100,
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
//        val can_comment": 1,
//        val can_like": 1,
//        val can_repost": 1,
//        val can_subscribe": 1,
//        val can_add_to_faves": 1,
//        val can_add": 1,
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
//        val views: Long,
//        val local_views": 10,
        val platform: String
    ) : Type()
}

//todo
/*"audio": {
              "artist": "Аффинаж",
              "id": 456243003,
              "owner_id": 2000410230,
              "title": "Солнце",
              "duration": 25,
              "url": "https://vk.com/mp3/audio_api_unavailable.mp3",
              "date": 1580671310
            }*/