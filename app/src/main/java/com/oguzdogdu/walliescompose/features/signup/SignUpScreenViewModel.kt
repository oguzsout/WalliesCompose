package com.oguzdogdu.walliescompose.features.signup

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.model.auth.User
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.login.signinwithemail.SignInWithEmailState
import com.oguzdogdu.walliescompose.util.FieldValidators
import com.oguzdogdu.walliescompose.util.FieldValidators.isValidEmailFlow
import com.oguzdogdu.walliescompose.util.FieldValidators.isValidPasswordCheckFlow
import com.oguzdogdu.walliescompose.util.FieldValidators.isValidPasswordCheckRuleSetFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(private val authenticationRepository: UserAuthenticationRepository) : ViewModel() {

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val _signUpUiState : MutableStateFlow<SignUpScreenState> = MutableStateFlow(SignUpScreenState.Start)
    val signUpUiState = _signUpUiState.asStateFlow()

    fun handleUiEvents(event: SignUpScreenEvent) {
        when(event) {
            is SignUpScreenEvent.ResumeToSignUp -> {
                email.value = event.email
                password.value = event.password
                combineValidation()
                userSignUp(email = email.value, password = password.value)

            }

            SignUpScreenEvent.ExecuteValidation -> executePasswordRuleset(password.value)
        }
    }

    private fun userSignUp(
        email: String?,
        password: String?,
    ) {
        if (combineValidation()) {
            viewModelScope.launch {
                authenticationRepository.signUp(
                    user = User(
                        email = email.orEmpty()
                    ),
                    password = password.orEmpty()
                ).collect { result ->
                    result.onLoading { _signUpUiState.update { SignUpScreenState.Loading(true) } }
                    delay(500)
                    result.onSuccess { _signUpUiState.update { SignUpScreenState.UserSignUp } }
                    result.onFailure { error -> _signUpUiState.update { SignUpScreenState.ErrorSignUp(error) } }
                    delay(1000)
                    _signUpUiState.update {
                        SignUpScreenState.Loading(false)
                    }
                }
            }
        }
    }

    private fun combineValidation() : Boolean {
        var successValidForSignUp = false
        viewModelScope.launch {
            isValidEmailFlow(email.value).combine(isValidPasswordCheckFlow(password.value)) { isValidEmail, isValidPassword ->
                    isValidEmail && isValidPassword
                }.map {
                    successValidForSignUp = it
            }.collect()
        }
        return successValidForSignUp
    }

    fun executePasswordRuleset(password: String) {
        viewModelScope.launch {
            isValidPasswordCheckRuleSetFlow(password).distinctUntilChanged().collectLatest { ruleSetList ->
                _signUpUiState.update {
                    SignUpScreenState.PasswordRuleSet(ruleSet = ruleSetList)
                }
            }
        }
    }
}