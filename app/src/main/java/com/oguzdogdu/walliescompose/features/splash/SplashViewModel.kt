package com.oguzdogdu.walliescompose.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) :
    ViewModel() {

    private val _splashState: MutableStateFlow<SplashScreenState> = MutableStateFlow(
        SplashScreenState.StartFlow
    )
    val splashState = _splashState.asStateFlow()

    fun handleUIEvent(event: SplashScreenEvent) {
        when (event) {
            is SplashScreenEvent.CheckAuthState -> checkSignIn()
        }
    }

    private fun checkSignIn() {
        viewModelScope.launch {
            delay(2000)
            val authState = authenticationRepository.isUserAuthenticatedInFirebase().single()
            if (authState) {
                _splashState.update { SplashScreenState.UserSignedIn }
            } else {
                _splashState.update { SplashScreenState.UserNotSigned }
            }
        }
    }
}
