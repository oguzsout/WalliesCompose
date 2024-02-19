package com.oguzdogdu.walliescompose.features.login.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.util.FieldValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ForgotMyPasswordViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _forgotPasswordState: MutableStateFlow<ForgotPasswordScreenState?> = MutableStateFlow(
        null
    )
    val forgotPasswordState = _forgotPasswordState.asStateFlow()

    private val userEmail = MutableStateFlow("")

    fun handleUIEvent(event: ForgotPasswordScreenEvent) {
        when (event) {
            is ForgotPasswordScreenEvent.SendEmail -> {
               sendNewPasswordRequest(event.email)
            }
            is ForgotPasswordScreenEvent.ButtonState -> {
                buttonStateUpdate()
            }
        }
    }

    fun setEmail(email: String?) {
        email?.let {
            userEmail.value = it
        }
    }

    private fun sendNewPasswordRequest(email: String?) {
        viewModelScope.launch {
            _forgotPasswordState.update { ForgotPasswordScreenState.Loading(true) }
            delay(750)
            authenticationRepository.forgotMyPassword(email.orEmpty()).addOnCompleteListener {task->
                when {
                    task.isSuccessful -> {
                        _forgotPasswordState.update { ForgotPasswordScreenState.ProcessStat(true) }
                    }
                    task.exception?.cause?.message?.isNotEmpty() == true -> {
                        _forgotPasswordState.update {
                            ForgotPasswordScreenState.ProcessStat(false)
                            ForgotPasswordScreenState.SendEmailError(task.exception?.cause?.message.orEmpty())
                        }
                    }
                    task.exception?.message?.isNotEmpty() == true -> {
                        _forgotPasswordState.update {
                            ForgotPasswordScreenState.ProcessStat(false)
                            ForgotPasswordScreenState.SendEmailError(task.exception?.message.orEmpty())
                        }
                    }
                    !task.isSuccessful -> {
                        _forgotPasswordState.update {
                            ForgotPasswordScreenState.ProcessStat(false)
                            ForgotPasswordScreenState.SendEmailError("Something went wrong")
                        }
                    }
                }
            }
            _forgotPasswordState.update { ForgotPasswordScreenState.Loading(false) }
        }
    }

    private fun checkButtonState(): Boolean {
        return FieldValidators.isValidEmail(email = userEmail.value)
    }

    private fun buttonStateUpdate() {
        viewModelScope.launch {
            val state = checkButtonState()
            _forgotPasswordState.update { ForgotPasswordScreenState.ButtonEnabled(isEnabled = state) }
        }
    }
}
