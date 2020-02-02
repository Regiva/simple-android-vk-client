package com.regiva.simple_vk_client.entity.responses.newsfeed

data class GetNewsfeedResponse(
    val items: List<PostResponseModel>,
    val profiles: List<ProfileModel>,
    val groups: List<GroupModel>,
    val next_from: String
)

data class ProfileModel(
    val id: Long,
    val first_name: String,
    val last_name: String,
    val photo_50: String,
    val photo_100: String
) {
    fun getName() = "$first_name $last_name"
}

data class GroupModel(
    val id: Long,
    val name: String,
    val photo_50: String,
    val photo_100: String
)