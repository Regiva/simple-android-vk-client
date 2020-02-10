package com.regiva.simple_vk_client.model.interactors

import com.regiva.simple_vk_client.model.repositories.LikesRepository
import com.regiva.simple_vk_client.model.repositories.PostsRepository
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postsRepository: PostsRepository,
    private val likesRepository: LikesRepository
) {
    fun getNewsfeed() = postsRepository.getNewsfeed()

    fun getComments(owner_id: Long, post_id: Long) = postsRepository.getComments(owner_id, post_id)

    fun likePost(isLiked: Boolean, owner_id: Long, item_id: Long) =
        if (isLiked) likesRepository.unlike("post", owner_id, item_id)
        else likesRepository.like("post", owner_id, item_id)

    fun checkIsLiked(owner_id: Long, item_id: Long) = likesRepository.checkIsLiked("post", owner_id, item_id)
}