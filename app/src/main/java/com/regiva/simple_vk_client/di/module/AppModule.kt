package com.regiva.simple_vk_client.di.module

import android.content.Context
import com.google.gson.Gson
import com.regiva.simple_vk_client.di.provider.GsonProvider
import com.regiva.simple_vk_client.model.data.storage.Prefs
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule(
    context: Context
) : Module() {
    init {
        bind(Context::class.java).toInstance(context)
        bind(Gson::class.java).toProvider(GsonProvider::class.java).providesSingletonInScope()
        bind(Prefs::class.java).singletonInScope()
        // navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
    }
}