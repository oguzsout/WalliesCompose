package com.oguzdogdu.walliescompose.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.walliescompose.data.common.Constants.COLLECTION_PATH
import com.oguzdogdu.walliescompose.data.common.Constants.EMAIL
import com.oguzdogdu.walliescompose.data.common.Constants.ID
import com.oguzdogdu.walliescompose.data.common.Constants.IMAGE
import com.oguzdogdu.walliescompose.data.common.Constants.NAME
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import com.oguzdogdu.walliescompose.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : UserAuthenticationRepository {
    override suspend fun isUserAuthenticatedInFirebase(): Flow<Boolean> = flow {
        emit(auth.currentUser != null)
    }

    override suspend fun signIn(userEmail: String?, password: String?):Flow<Resource<AuthResult>> {
        return flow {
           emit(auth.signInWithEmailAndPassword(userEmail.orEmpty(), password.orEmpty()).await())
        }.toResource()
    }
}