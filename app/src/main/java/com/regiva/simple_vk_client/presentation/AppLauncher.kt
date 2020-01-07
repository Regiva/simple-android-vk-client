package com.regiva.simple_vk_client.presentation

import com.regiva.simple_vk_client.Screens
import com.regiva.simple_vk_client.di.DI
import com.regiva.simple_vk_client.di.module.ServerModule
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import javax.inject.Inject

class AppLauncher @Inject constructor(
    private val router: Router,
    private val authInteractor: AuthInteractor
) {

    fun initModules() {
        if (!Toothpick.isScopeOpen(DI.SERVER_SCOPE)) {
            Toothpick.openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
                .installModules(ServerModule())
        }
    }

    fun coldStart() {
        if (authInteractor.isLoggedIn()) {
            router.newRootScreen(Screens.Main())
        }
        else {
            router.newRootScreen(Screens.AuthFlow)
        }
    }
}