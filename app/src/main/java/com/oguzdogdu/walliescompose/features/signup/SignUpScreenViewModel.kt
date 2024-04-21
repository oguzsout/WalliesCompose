package com.oguzdogdu.walliescompose.features.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.model.auth.User
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.util.FieldValidators.isValidEmailFlow
import com.oguzdogdu.walliescompose.util.FieldValidators.isValidPasswordCheckFlow
import com.oguzdogdu.walliescompose.util.FieldValidators.isValidPasswordCheckRuleSetFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(private val authenticationRepository: UserAuthenticationRepository) :
    ViewModel() {

    private val _signUpUiState : MutableStateFlow<SignUpUIState> = MutableStateFlow(SignUpUIState())
    val signUpUiState = _signUpUiState.asStateFlow()

    private var userInfosIsValid = MutableStateFlow(false)

    fun handleUiEvents(event: SignUpScreenEvent) {
        when(event) {
            is SignUpScreenEvent.ResumeToSignUp -> {
                combineValidations(email = event.email, password = event.password)
                userSignUp(email = event.email, password = event.password)
            }

            is SignUpScreenEvent.CheckPasswordRule -> {
                executePasswordRuleSet(event.password)
            }
        }
    }

    private fun userSignUp(
        email: String?,
        password: String?,
    ) {
        viewModelScope.launch {
            if (userInfosIsValid.value) {
                authenticationRepository.signUp(
                    user = User(
                        email = email.orEmpty()
                    ),
                    password = password.orEmpty()
                ).collect { result ->
                    result.onLoading { _signUpUiState.update { it.copy(loading = true) } }
                    delay(500)
                    result.onSuccess {
                        _signUpUiState.update {
                            it.copy(
                                isSignUp = true,
                                loading = false
                            )
                        }
                    }
                    result.onFailure { error ->
                        _signUpUiState.update {
                            it.copy(
                                errorMessage = error,
                                loading = false,
                                isSignUp = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun combineValidations(email: String?, password: String?) {
        viewModelScope.launch {
            isValidEmailFlow(email.orEmpty())
                .combine(isValidPasswordCheckFlow(password.orEmpty())) { isValidEmail, isValidPassword ->
                isValidEmail && isValidPassword
            }.map {
                userInfosIsValid.value = it
            }.collect()
        }
    }

   private fun executePasswordRuleSet(password: String?) {
        viewModelScope.launch {
            isValidPasswordCheckRuleSetFlow(password.orEmpty()).collectLatest { ruleSetList ->
                _signUpUiState.update {
                    it.copy(ruleSet = ruleSetList)
                }
            }
        }
    }
}