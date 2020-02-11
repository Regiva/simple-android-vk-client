package com.regiva.simple_vk_client.model.data.feature

import android.util.Log
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.entity.newsfeed.PostSourceModel
import com.regiva.simple_vk_client.entity.responses.newsfeed.ProfileModel
import com.regiva.simple_vk_client.model.data.feature.PostsFeature.*
import com.regiva.simple_vk_client.model.interactors.PostsInteractor
import com.regiva.simple_vk_client.util.delayEach
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PostsFeature @Inject constructor(
    postsInteractor: PostsInteractor
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    actor = ActorImpl(
        postsInteractor
    ),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val isLoading: Boolean = false,
        val posts: List<PostModel> = listOf(),
        val profiles: List<ProfileModel>? = null,
        val start_from: String? = null
    )

    sealed class Wish {
        object GetAllPosts : Wish()
        data class LikePost(val isLiked: Boolean, val owner_id: Long, val item_id: Long) : Wish()
    }

    sealed class Effect {
        object GetAllPostsStart : Effect()
        data class GetAllPostsSuccess(val posts: List<PostModel>, val start_from: String) : Effect()
        data class GetAllPostsFailure(val throwable: Throwable) : Effect()

        object LikePostStart : Effect()
        data class LikePostSuccess(val item_id: Long, val like_count: Int) : Effect()
        data class LikePostFailure(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class GetAllPostsFailure(val throwable: Throwable) : News()
        data class LikePostFailure(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val postsInteractor: PostsInteractor
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.GetAllPosts ->
                postsInteractor.getNewsfeed(state.start_from)
                    .flatMapIterable {
                        state.copy(start_from = it.response.next_from)
                        Log.d("rere", "start_from ${state.start_from}")
                        Log.d("rere", "next_from ${it.response.next_from}")
                        it.response.items
                            .filter { !it.text.isNullOrBlank() && !it.attachments.isNullOrEmpty() }
                            .map { post ->
                                Triple(post, it.response.groups.find { it.id == -post.source_id }, it.response.next_from)
                            }
                    }
                    .delayEach(400L, TimeUnit.MILLISECONDS)
                    .flatMap { postResponse ->
                        postsInteractor.checkIsLiked(postResponse.first.source_id, postResponse.first.post_id)
                            .map {
                                val post = PostModel(
                                    source = PostSourceModel(postResponse.second?.id ?: 0, postResponse.second?.name ?: "name", postResponse.second?.photo_100 ?: ""),//PostSourceModel(postResponse.first.source_id, "LOL", ""),
                                    date = postResponse.first.date,
                                    text = postResponse.first.text,
                                    attachments = postResponse.first.attachments,
                                    comment_count = postResponse.first.comments?.count ?: 0,
                                    likes_count = postResponse.first.likes?.count ?: 0,
                                    post_id = postResponse.first.post_id
                                )
                                post.isLiked = if (it.response.liked == 0) false else true
                                post to postResponse.third
                            }
                            .doOnError { it.printStackTrace() }
                    }
                    .map {
                        it
                    }
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        Effect.GetAllPostsSuccess(it.map { it.first }, it.first().second) as Effect
                    }
                    .toObservable()
                    .startWith(Effect.GetAllPostsStart)
                    .onErrorReturn { Effect.GetAllPostsFailure(it) }


            is Wish.LikePost ->
                postsInteractor.likePost(wish.isLiked, wish.owner_id, wish.item_id)
                    .map { Effect.LikePostSuccess(wish.item_id, it.response.likes) as Effect }
                    .startWith(Effect.LikePostStart)
                    .onErrorReturn { Effect.LikePostFailure(it) }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.GetAllPostsStart -> state.copy(isLoading = true)
            is Effect.GetAllPostsSuccess -> state.copy(isLoading = false, posts = state.posts + effect.posts, start_from = effect.start_from)
            is Effect.GetAllPostsFailure -> state.copy(isLoading = false)

            is Effect.LikePostStart -> state //todo
            is Effect.LikePostSuccess -> state.copy(
                posts = state.posts.map {
                    if (it.post_id == effect.item_id)
                        it.copy(isLiked = !it.isLiked, likes_count = effect.like_count)
                    else
                        it
                }
            )
            is Effect.LikePostFailure -> state
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.GetAllPostsFailure -> News.GetAllPostsFailure(effect.throwable)
            //todo
            else -> null
        }
    }
}

/*.map { response ->
                        val posts = response.response.items
                            .filter { !it.text.isNullOrBlank() && !it.attachments.isNullOrEmpty() }
                            .take(5)
                            .map { postResponse ->
                                val postSource =
                                    if (postResponse.source_id > 0) {
                                        with(response.response.profiles.find { it.id == postResponse.source_id }) {
                                            PostSourceModel(
                                                id = this?.id ?: 0,
                                                name = this?.getName() ?: "",
                                                photo = this?.photo_100 ?: ""
                                            )
                                        }
                                    } else {
                                        with(response.response.groups.find { it.id == -postResponse.source_id }) {
                                            PostSourceModel(
                                                id = this?.id ?: 0,
                                                name = this?.name ?: "",
                                                photo = this?.photo_100 ?: ""
                                            )
                                        }
                                    }
                                Log.d("rere", "ZAEBALSYAAAAAÀ")
                                val post = PostModel(
                                    source = postSource,
                                    date = postResponse.date,
                                    text = postResponse.text,
                                    attachments = postResponse.attachments,
                                    comment_count = postResponse.comments?.likes ?: 0,
                                    likes_count = postResponse.likes?.likes ?: 0,
                                    post_id = postResponse.post_id
                                )
                                postsInteractor.checkIsLiked(post.source.id, post.post_id)
                                    .map {
                                        Log.d("rere", "${post.text} \n is liked = ${it.response.liked}")
                                        post.isLiked = it.response.liked

                                    }
//                                    .doOnSuccess {
//                                        Log.d("rere", "${post.text} \n is liked = ${it.response.liked}")
//                                        post.isLiked = it.response.liked
//                                    }
//                                    .delayEach(1000, TimeUnit.MILLISECONDS)
                                post
                            }
                        Effect.GetAllPostsSuccess(posts) as Effect
                        *//*Observable.fromArray(posts)
                            .flatMapIterable { it }
                            .flatMap { post ->
                                Log.d("rere", "post_id = ${post.post_id}")


                            }
                            .map {
                                Log.d("rere", "map yopta")
                                Effect.GetAllPostsSuccess(posts) as Effect
                            }
                            .startWith(Effect.GetAllPostsStart)
                            .onErrorReturn { Effect.GetAllPostsFailure(it) }*//*

                    }
*/
// //                   .startWith(Effect.GetAllPostsStart)
// //                   .onErrorReturn { Effect.GetAllPostsFailure(it) }

/*.map { response ->
    val posts = response.response.items
        .flatMap { postResponse ->
            val postSource =
                if (postResponse.source_id > 0) {
                    with (response.response.profiles.find { it.id == postResponse.source_id }) {
                        PostSourceModel(
                            id = this?.id ?: 0,
                            name = this?.getName() ?: "",
                            photo = this?.photo_100 ?: ""
                        )
                    }
                }
                else {
                    with (response.response.groups.find { it.id == -postResponse.source_id }) {
                        PostSourceModel(
                            id = this?.id ?: 0,
                            name = this?.name ?: "",
                            photo = this?.photo_100 ?: ""
                        )
                    }
                }
            val post = PostModel(
                source = postSource,
                date = postResponse.date,
                text = postResponse.text,
                attachments = postResponse.attachments,
                comment_count = postResponse.comments?.likes ?: 0,
                likes_count = postResponse.likes?.likes ?: 0,
                post_id = postResponse.post_id
            )
            postsInteractor.checkIsLiked(post.source.id, post.post_id)
                .map {
                    post.isLiked = it.response.liked
                }
        }
        .startWith(Effect.GetAllPostsStart)
        .onErrorReturn { Effect.GetAllPostsFailure(it) }
    Effect.GetAllPostsSuccess(posts.filter { !it.text.isNullOrBlank() && !it.attachments.isNullOrEmpty() }) as Effect
}*/