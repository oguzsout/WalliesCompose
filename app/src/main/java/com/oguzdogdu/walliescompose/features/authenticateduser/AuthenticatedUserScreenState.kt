package com.oguzdogdu.walliescompose.features.authenticateduser

sealed class AuthenticatedUserScreenState {
    data object Loading : AuthenticatedUserScreenState()
    data class UserInfoError(val errorMessage: String?) : AuthenticatedUserScreenState()
    data class CheckUserAuthenticated(val isAuthenticated: Boolean) : AuthenticatedUserScreenState()
    data class CheckUserGoogleSignIn(val isAuthenticated: Boolean) : AuthenticatedUserScreenState()
    data class UserInfos(
        val name: String? = null,
        val surname: String? = null,
        val email: String? = null,
        val profileImage: String? = null,
        val favorites: List<HashMap<String, String>>? = emptyList()
    ) : AuthenticatedUserScreenState()
}
