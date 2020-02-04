package com.regiva.simple_vk_client.model.data.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.entity.newsfeed.PostSourceModel
import com.regiva.simple_vk_client.entity.responses.newsfeed.ProfileModel
import com.regiva.simple_vk_client.model.data.feature.PostsFeature.*
import com.regiva.simple_vk_client.model.interactors.PostsInteractor
import io.reactivex.Observable
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
        val posts: List<PostModel>? = null,
        val profiles: List<ProfileModel>? = null
    )

    sealed class Wish {
        object GetAllPosts : Wish()
        //todo
//        data class DeleteDoc(val docId: String) : Wish()
//        data class ChangeFavoriteStatus(val docId: String) : Wish()
    }

    sealed class Effect {
        object GetAllPostsStart : Effect()
        data class GetAllPostsSuccess(val posts: List<PostModel>) : Effect()
        data class GetAllPostsFailure(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class GetAllPostsFailure(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val postsInteractor: PostsInteractor
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.GetAllPosts ->
                postsInteractor.getNewsfeed()
                    .map { response ->
                        val posts = response.response.items
                            .map { postResponse ->
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
                                //todo
//                            Log.d("rere", "map $postSource")
                            PostModel(
                                source = postSource,
                                date = postResponse.date,
                                text = postResponse.text,
                                attachments = postResponse.attachments,
                                comment_count = postResponse.comments?.count ?: 0,
                                likes_count = postResponse.likes?.count ?: 0,
                                post_id = postResponse.post_id
                            )
                        }
//                        posts.forEachIndexed { index, postModel ->
//                            Log.d("rere $index", "$postModel \n")
//                        }
//                        Log.d("rere", "map blyad ${comments.map { "\n $it" }}")
                        Effect.GetAllPostsSuccess(posts.filter { !it.text.isNullOrBlank() && !it.attachments.isNullOrEmpty() }) as Effect
                    }
                    .startWith(Effect.GetAllPostsStart)
                    .onErrorReturn { Effect.GetAllPostsFailure(it) }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.GetAllPostsStart -> state.copy(isLoading = true)
            is Effect.GetAllPostsSuccess -> state.copy(isLoading = false, posts = effect.posts)
            is Effect.GetAllPostsFailure -> state.copy(isLoading = false)
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.GetAllPostsFailure -> News.GetAllPostsFailure(effect.throwable)

            else -> null
        }
    }
}