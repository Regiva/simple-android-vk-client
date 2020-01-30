package com.regiva.simple_vk_client.model.interactors

import com.regiva.simple_vk_client.model.repositories.AuthRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository/*,
    private val accountRepository: AccountRepository*/
) {

    //fun isLoggedIn() = accountRepository.isLoggedIn()

    fun storeToken(token: String) = authRepository.storeToken(token)

    /*fun register(userAuthDto: UserAuthDto) =
        authRepository.register(userAuthDto)
            .toObservable()

    fun logIn(userAuthDto: UserAuthDto) =
        authRepository.logIn(userAuthDto)
            .toObservable()

    fun logOut() =
        authRepository.logOut()

    fun restorePassword(email: String) =
        authRepository.sendEmailForRestoringPassword(email)
            .toObservable()*/
}