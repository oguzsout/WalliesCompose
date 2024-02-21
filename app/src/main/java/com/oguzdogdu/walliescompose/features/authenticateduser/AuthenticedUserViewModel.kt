package com.oguzdogdu.walliescompose.features.authenticateduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticatedUserViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _userState: MutableStateFlow<AuthenticatedUserScreenState?> = MutableStateFlow(null)
    val userState = _userState.asStateFlow()

    private val _userAuthState = MutableStateFlow(false)
    val userAuthState = _userAuthState.asStateFlow()

    fun handleUiEvents(event: AuthenticatedUserEvent) {
        when (event) {
            is AuthenticatedUserEvent.CheckUserAuth -> {
                checkSignIn()
            }
            is AuthenticatedUserEvent.FetchUserInfos -> {
                if (_userAuthState.value) {
                    fetchUserDatas()
                    checkUserSignInMethod()
                }
            }

            is AuthenticatedUserEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun fetchUserDatas() {
        viewModelScope.launch {
            authenticationRepository.fetchUserInfos().collectLatest { result ->
                result.onLoading {
                    _userState.update { AuthenticatedUserScreenState.Loading }
                }
                result.onFailure { error ->
                    _userState.update {
                        AuthenticatedUserScreenState.UserInfoError(
                            error
                        )
                    }
                }

                result.onSuccess { user ->
                    _userState.update {
                        AuthenticatedUserScreenState.UserInfos(
                            name = user?.name,
                            surname = user?.surname,
                            email = user?.email,
                            profileImage = user?.image,
                            favorites = user?.favorites.orEmpty()
                        )
                    }
                }
            }
        }
    }

    private fun checkSignIn(){
        viewModelScope.launch {
            authenticationRepository.isUserAuthenticatedInFirebase().collectLatest { status ->
               _userAuthState.emit(status)
                _userState.update {
                    AuthenticatedUserScreenState.CheckUserAuthenticated(isAuthenticated = status)
                }
            }
        }
    }

    private fun checkUserSignInMethod() {
        viewModelScope.launch {
            authenticationRepository.isUserAuthenticatedWithGoogle().collectLatest { result ->
                _userState.update {
                    AuthenticatedUserScreenState.CheckUserGoogleSignIn(
                        isAuthenticated = result
                    )
                }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            authenticationRepository.signOut()
        }
    }
}
