package com.oguzdogdu.walliescompose.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.oguzdogdu.walliescompose.domain.model.auth.User
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface UserAuthenticationRepository {
    suspend fun isUserAuthenticatedInFirebase(): Flow<Boolean>
    suspend fun isUserAuthenticatedWithGoogle(): Flow<Boolean>
    suspend fun signIn(userEmail: String?, password: String?):Flow<Resource<AuthResult>>
    suspend fun signUp(user: User?, password: String?) : Flow<Resource<User?>>
    suspend fun signInWithGoogle(idToken: String?):Flow<Resource<AuthResult>>
    suspend fun fetchUserInfos():Flow<Resource<User?>>
    suspend fun forgotMyPassword(email: String): Task<Void>
    suspend fun signOut()
    suspend fun changeProfilePhoto(photo: String?)
    suspend fun updatePassword(password: String?): Flow<Resource<Task<Void>?>>
    suspend fun changeEmail(email:String?,password: String?): Flow<Resource<Task<Void>?>>
    suspend fun changeUsername(name:String?): Flow<Resource<String>>
    suspend fun changeSurname(surname:String?): Flow<Resource<String>>
}