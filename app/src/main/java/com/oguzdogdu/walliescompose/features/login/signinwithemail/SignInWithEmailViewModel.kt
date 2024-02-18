package com.oguzdogdu.walliescompose.features.login.signinwithemail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.login.LoginState
import com.oguzdogdu.walliescompose.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class SignInWithEmailViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _signInEmailState: MutableStateFlow<SignInWithEmailState?> = MutableStateFlow(null)
    val signInEmailState = _signInEmailState.asStateFlow()

     private var userEmail = MutableStateFlow("")


     private var userPassword = MutableStateFlow("")


    fun handleUIEvent(event: SignInWithEmailEvents) {

        when(event) {
            is SignInWithEmailEvents.UserSignIn -> {
                signIn(userEmail = event.email, password = event.password)
            }
            is SignInWithEmailEvents.ButtonState -> {
                checkButtonState()
            }
        }
    }

    fun setEmail(email: String?) {
        email?.let {
            userEmail.value = it
        }
    }

    fun setPassword(password: String?) {
        password?.let {
            userPassword.value = it
        }
    }

    private fun checkButtonState() {
        viewModelScope.launch {
            val buttonState =  FieldValidators.isValidEmail(email = userEmail.value) && FieldValidators.isValidPasswordCheck(
                input = userPassword.value
            )
            _signInEmailState.update { SignInWithEmailState.ButtonEnabled(isEnabled = buttonState) }
        }
    }

    private fun signIn(userEmail: String?, password: String?) {
        viewModelScope.launch {
            authenticationRepository.signIn(userEmail, password).collect { response ->
                response.onLoading {
                    _signInEmailState.update { SignInWithEmailState.Loading(true) }
                }
                delay(1000)
                response.onSuccess {
                    _signInEmailState.update {
                        SignInWithEmailState.Loading(false)
                        SignInWithEmailState.UserSignIn
                    }
                }
                response.onFailure {error ->
                    _signInEmailState.update {
                        SignInWithEmailState.ErrorSignIn(
                            errorMessage = error
                        )
                    }
                }
            }
        }
    }
}