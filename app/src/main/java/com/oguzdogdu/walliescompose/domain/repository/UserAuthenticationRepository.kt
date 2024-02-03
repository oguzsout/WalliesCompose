package com.oguzdogdu.walliescompose.domain.repository

import com.google.firebase.auth.AuthResult
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface UserAuthenticationRepository {
    suspend fun isUserAuthenticatedInFirebase(): Flow<Boolean>
    suspend fun signIn(userEmail: String?, password: String?):Flow<Resource<AuthResult>>
    suspend fun signInWithGoogle(idToken: String?):Flow<Resource<AuthResult>>
}