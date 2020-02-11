package com.regiva.simple_vk_client

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.regiva.simple_vk_client.di.DI
import com.regiva.simple_vk_client.di.module.AppModule
import toothpick.Toothpick
import toothpick.configuration.Configuration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        toothpick()
        appScope()
        Fresco.initialize(this)
    }

    private fun toothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction())
        }
    }

    private fun appScope() {
        Toothpick.openScope(DI.APP_SCOPE)
            .installModules(AppModule(this))
    }

}