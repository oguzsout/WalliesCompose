package com.oguzdogdu.walliescompose.features.authenticateduser.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.login.signinwithemail.SignInWithEmailEvents
import com.oguzdogdu.walliescompose.features.login.signinwithemail.SignInWithEmailState
import com.oguzdogdu.walliescompose.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _passwordState: MutableStateFlow<ChangePasswordScreenState?> = MutableStateFlow(null)
    val passwordState = _passwordState.asStateFlow()

    private var userPassword = MutableStateFlow("")

    fun handleUIEvent(event: ChangePasswordScreenEvent) {
        when (event) {
            is ChangePasswordScreenEvent.UserPassword -> {
                updatePassword(event.password)
            }
            is ChangePasswordScreenEvent.ButtonState -> {
                checkButtonState()
            }
        }
    }

    fun setPassword(password: String?) {
        password?.let {
            userPassword.value = it
        }
    }
    private fun updatePassword(password: String?) {
        viewModelScope.launch {
            authenticationRepository.updatePassword(password = password).collectLatest { state ->
                state.onLoading {
                    _passwordState.update { ChangePasswordScreenState.Loading(true) }
                }
                delay(500)
                state.onSuccess { task ->
                    task?.addOnCompleteListener { isSuccess ->
                        _passwordState.update {
                            ChangePasswordScreenState.PasswordChangeStatus(
                                status = isSuccess.isSuccessful
                            )
                        }
                    }
                    _passwordState.update { ChangePasswordScreenState.Loading(false) }
                }
                state.onFailure {error ->
                    _passwordState.update {
                        ChangePasswordScreenState.PasswordChangeError(
                            errorMessage = error
                        )
                    }
                }
            }
        }
    }

    private fun checkButtonState() {
        viewModelScope.launch {
            val buttonState = FieldValidators.isValidPasswordCheck(
                input = userPassword.value
            )
            _passwordState.update { ChangePasswordScreenState.ButtonEnabled(isEnabled = buttonState) }
        }
    }
}
