package com.oguzdogdu.walliescompose.features.authenticateduser.changeemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
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
        ChangeEmailScreenState()
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
            _emailState.update { it.copy(isEnabled = buttonState) }
        }
    }

     fun changeEmail(email: String?, password: String?) {
        viewModelScope.launch {
            authenticationRepository.changeEmail(email = email, password = password)
                .collect { result ->
                    result.onLoading {
                        _emailState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    result.onFailure { error ->
                        _emailState.update {
                            it.copy(isLoading = false, errorMessage = error)
                        }
                    }

                    result.onSuccess { auth ->
                        _emailState.update {
                            it.copy(isLoading = false, emailChanged = "Email Changed")
                        }
                    }
                }
        }
    }
}