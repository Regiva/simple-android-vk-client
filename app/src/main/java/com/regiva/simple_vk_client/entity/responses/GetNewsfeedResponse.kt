package com.regiva.simple_vk_client.entity.responses

import com.regiva.simple_vk_client.entity.PostModel

data class GetNewsfeedResponse(
    val items: List<PostModel>,
    val next_from: String
)