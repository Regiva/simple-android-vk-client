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
                        Log.d("rere", "map yopta")
                        Log.d("rere", "${response.response.items.map { "\n $it" }}")
//                        Log.d("rere", "${response.response.profiles.map { it.id }}")
//                        Log.d("rere", "${response.response.groups.map { it.id }}")
                        val postss = arrayListOf<PostModel>()
                        response.response.items.forEachIndexed { index, postResponse  ->
                            Log.d("rere", "index = $index")
                            val postSource =
                                if (postResponse.source_id > 0) {
                                    response.response.profiles.find { it.id == -postResponse.source_id }?.let {
                                        PostSourceModel(
                                            id = it?.id ?: 0,
                                            name = it?.getName() ?: "",
                                            photo = it?.photo_100 ?: ""
                                        )
                                    }
                                }
                                else {
                                    response.response.groups.find { it.id == -postResponse.source_id }?.let {
                                        PostSourceModel(
                                            id = it?.id ?: 0,
                                            name = it?.name ?: "",
                                            photo = it?.photo_100 ?: ""
                                        )
                                    }
                                }
                            Log.d("rere", "map $postResponse")
                            postSource?.let {
                                postss.add(
                                    PostModel(
                                        source = it,
                                        date = postResponse.date,
                                        text = postResponse.text,
                                        comment_count = postResponse.comments?.count ?: 0,
                                        likes_count = postResponse.likes?.count ?: 0,
                                        post_id = postResponse.post_id
                                    )
                                )
                            }
                        }
                        /*val posts = response.response.items
                            .filter { postResponse ->
                                val postSource: PostSourceModel? =
                                    if (postResponse.source_id > 0) {
                                        response.response.profiles.find { it.id == postResponse.source_id }?.let {
                                            PostSourceModel(
                                                id = it?.id ?: 0,
                                                name = it?.getName() ?: "",
                                                photo = it?.photo_100 ?: ""
                                            )
                                        }
                                        *//*with (response.response.profiles.find { it.id == postResponse.source_id }) {
                                            PostSourceModel(
                                                id = this?.id ?: 0,
                                                name = this?.getName() ?: "",
                                                photo = this?.photo_100 ?: ""
                                            )
                                        }*//*
                                    }
                                    else {
                                        response.response.groups.find { it.id == -postResponse.source_id }?.let {
                                            PostSourceModel(
                                                id = it?.id ?: 0,
                                                name = it?.name ?: "",
                                                photo = it?.photo_100 ?: ""
                                            )
                                        }
                                        *//*with (response.response.groups.find { it.id == -postResponse.source_id }) {
                                            PostSourceModel(
                                                id = this?.id ?: 0,
                                                name = this?.name ?: "",
                                                photo = this?.photo_100 ?: ""
                                            )
                                        }*//*
                                    }
                                postSource != null
                            }
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
                            Log.d("rere", "map $postSource")
                            PostModel(
                                source = postSource,
                                date = postResponse.date,
                                text = postResponse.text,
                                comment_count = postResponse.comments?.count ?: 0,
                                likes_count = postResponse.likes?.count ?: 0,
                                post_id = postResponse.post_id
                            )
                        }*/
                        Log.d("rere", "map blyad $postss")
                        Effect.GetAllPostsSuccess(postss) as Effect
                    }
                        //todo
                    /*.flatMap { person ->
                        docsInteractor.getAllDocsForPerson(person.id)
                            .map { Effect.GetAllPostsSuccess(it.data) as Effect }
                            .startWith(Effect.GetAllPostsStart)
                            .onErrorReturn { Effect.GetAllPostsFailure(it) }
                    }*/
                    .startWith(Effect.GetAllPostsStart)
                    //.doOnError { it.printStackTrace() }
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