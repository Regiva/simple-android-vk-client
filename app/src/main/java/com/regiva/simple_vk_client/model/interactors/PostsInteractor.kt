package com.regiva.simple_vk_client.model.interactors

import com.regiva.simple_vk_client.model.repositories.PostsRepository
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postsRepository: PostsRepository
) {
    fun getNewsfeed() = postsRepository.getNewsfeed()

    fun getComments(owner_id: Long, post_id: Long) = postsRepository.getComments(owner_id, post_id)
}