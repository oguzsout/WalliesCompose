package com.oguzdogdu.walliescompose.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Start)
    val loginState = _loginState.asStateFlow()

     private var userEmail = MutableStateFlow("")


     private var userPassword = MutableStateFlow("")


    fun handleUIEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.ButtonState -> {
                checkButtonState()
            }
            is LoginScreenEvent.UserSignIn -> {
                signIn(userEmail = event.email, password = event.password)
            }
            is LoginScreenEvent.GoogleButton -> {
                signInWithGoogle(idToken = event.idToken)
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
            _loginState.update { LoginState.ButtonEnabled(isEnabled = buttonState) }
        }
    }

    private fun signInWithGoogle(idToken: String?) {

    }

    private fun checkSignIn() {

    }

    private fun signIn(userEmail: String?, password: String?) {
        viewModelScope.launch {
            authenticationRepository.signIn(userEmail, password).collect { response ->
                response.onLoading {
                    _loginState.update { LoginState.Loading }
                }
                response.onSuccess {
                    _loginState.update { LoginState.UserSignIn }
                }
                response.onFailure {error ->
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
