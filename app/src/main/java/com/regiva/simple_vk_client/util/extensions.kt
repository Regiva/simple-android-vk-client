package com.regiva.simple_vk_client.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace
import java.util.concurrent.TimeUnit

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun Navigator.setLaunchScreen(screen: SupportAppScreen) {
    applyCommands(
        arrayOf(
            BackTo(null),
            Replace(screen)
        )
    )
}

fun <T> Observable<T>.delayEach(interval: Long, timeUnit: TimeUnit): Observable<T> =
    Observable.zip(
        this,
        Observable.interval(interval, timeUnit),
        BiFunction { item, _ -> item }
    )

fun <T> Single<T>.delayEach(interval: Long, timeUnit: TimeUnit): Observable<T> =
    Observable.zip(
        this.toObservable(),
        Observable.interval(interval, timeUnit),
        BiFunction { item, _ -> item }
    )