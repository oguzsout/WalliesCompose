package com.oguzdogdu.walliescompose.features.login

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Start)
    val loginState = _loginState.asStateFlow()

    fun handleUIEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.GoogleButton -> {
                signInWithGoogle(idToken = event.idToken)
            }
        }
    }

    private fun signInWithGoogle(idToken: String?) {
        viewModelScope.launch {
            authenticationRepository.signInWithGoogle(idToken).collect { response ->
                response.onLoading {
                    _loginState.update { LoginState.Loading(true) }
                }

                delay(2000)
                response.onSuccess {
                    _loginState.update {
                        LoginState.Loading(false)
                        LoginState.UserSignIn
                    }
                }
                response.onFailure { error ->
                    _loginState.update {
                        LoginState.ErrorSignIn(
                            errorMessage = error
                        )
                    }
                }
            }
        }
    }
}