package com.oguzdogdu.walliescompose.features.authenticateduser.changeemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _emailState: MutableStateFlow<ChangeEmailScreenState> = MutableStateFlow(
        ChangeEmailScreenState.Start
    )
    val emailState = _emailState.asStateFlow()

    private var userEmail = MutableStateFlow("")

    private var userPassword = MutableStateFlow("")

    fun handleUIEvent(event: ChangeUserEmailEvent) {
        when (event) {
            is ChangeUserEmailEvent.ChangedEmail -> {
                changeEmail(email = userEmail.value, password = userPassword.value)
            }

            is ChangeUserEmailEvent.ButtonState -> checkButtonState()
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
            val buttonState =
                FieldValidators.isValidEmail(email = userEmail.value) && FieldValidators.isValidPasswordCheck(
                    input = userPassword.value
                )
            _emailState.update { ChangeEmailScreenState.ButtonEnabled(isEnabled = buttonState) }
        }
    }

    private fun changeEmail(email: String?, password: String?) {
        viewModelScope.launch {
            _emailState.update {
                ChangeEmailScreenState.Loading(true)
            }

            authenticationRepository.changeEmail(email = email, password = password)
                .collect { result ->
                    result.onFailure { error ->
                        _emailState.update {
                            ChangeEmailScreenState.ChangedEmailError(error)
                        }
                    }

                    result.onSuccess { auth ->
                        _emailState.update {
                            ChangeEmailScreenState.ChangedEmail("Email Changed")
                        }
                    }
                }
            delay(1000)
            _emailState.update {
                ChangeEmailScreenState.Loading(false)
            }
        }
    }
}