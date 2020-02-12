package com.regiva.simple_vk_client.model.data.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.entity.responses.newsfeed.CommentResponseModel
import com.regiva.simple_vk_client.model.data.feature.DetailedPostFeature.*
import com.regiva.simple_vk_client.model.interactors.PostsInteractor
import io.reactivex.Observable
import javax.inject.Inject

class DetailedPostFeature @Inject constructor(
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
        val post: PostModel? = null,
        val isLoading: Boolean = false,
        val comments: List<CommentResponseModel>? = null
    )

    sealed class Wish {
        data class GetComments(val post: PostModel) : Wish()
    }

    sealed class Effect {
        data class GetCommentsStart(val post: PostModel) : Effect()
        data class GetCommentsSuccess(val comments: List<CommentResponseModel>) : Effect()
        data class GetCommentsFailure(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class GetCommentsFailure(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val postsInteractor: PostsInteractor
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.GetComments ->
                postsInteractor.getComments(wish.post.source.id, wish.post.post_id)
                    .map { Effect.GetCommentsSuccess(it.response.items) as Effect }
                    .startWith(Effect.GetCommentsStart(wish.post))
                    .onErrorReturn { Effect.GetCommentsFailure(it) }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.GetCommentsStart -> state.copy(isLoading = true, post = effect.post)
            is Effect.GetCommentsSuccess -> state.copy(isLoading = false, comments = effect.comments)
            is Effect.GetCommentsFailure -> state.copy(isLoading = false)
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.GetCommentsFailure -> News.GetCommentsFailure(effect.throwable)
            else -> null
        }
    }
}