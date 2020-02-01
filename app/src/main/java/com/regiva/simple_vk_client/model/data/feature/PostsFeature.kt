package com.regiva.simple_vk_client.model.data.feature

import android.util.Log
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.regiva.simple_vk_client.entity.PostModel
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
        val posts: List<PostModel>? = null
    )

    sealed class Wish {
        object GetAllPosts : Wish()
        //todo
//        data class DeleteDoc(val docId: String) : Wish()
//        data class ChangeFavoriteStatus(val docId: String) : Wish()
    }

    sealed class Effect {
        object GetAllPostsStart : Effect()
        data class GetAllPostsSuccess(val list: List<PostModel>) : Effect()
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
                    .map {
                        Log.d("rere", "map yopta")
                        Effect.GetAllPostsSuccess(it.response.items) as Effect
                    }
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
            is Effect.GetAllPostsSuccess -> state.copy(isLoading = false, posts = effect.list)
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