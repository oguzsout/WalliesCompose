package com.oguzdogdu.walliescompose.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.walliescompose.data.common.Constants.COLLECTION_PATH
import com.oguzdogdu.walliescompose.data.common.Constants.EMAIL
import com.oguzdogdu.walliescompose.data.common.Constants.FAVORITES
import com.oguzdogdu.walliescompose.data.common.Constants.ID
import com.oguzdogdu.walliescompose.data.common.Constants.IMAGE
import com.oguzdogdu.walliescompose.data.common.Constants.NAME
import com.oguzdogdu.walliescompose.data.common.Constants.SURNAME
import com.oguzdogdu.walliescompose.data.model.auth.User
import com.oguzdogdu.walliescompose.data.model.auth.toUserDomain
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import com.oguzdogdu.walliescompose.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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

    override suspend fun isUserAuthenticatedWithGoogle(): Flow<Boolean> = flow {
        val user = FirebaseAuth.getInstance().currentUser
        emit(user?.providerData?.any { it.providerId == GoogleAuthProvider.PROVIDER_ID } == true)
    }

    override suspend fun signIn(userEmail: String?, password: String?):Flow<Resource<AuthResult>> {
        return flow {
           emit(auth.signInWithEmailAndPassword(userEmail.orEmpty(), password.orEmpty()).await())
        }.toResource()
    }

    override suspend fun signUp(
        user: com.oguzdogdu.walliescompose.domain.model.auth.User?,
        password: String?
    ): Flow<Resource<com.oguzdogdu.walliescompose.domain.model.auth.User?>> {
        user?.email?.let { auth.createUserWithEmailAndPassword(it, password.orEmpty()).await() }
        val userModel = hashMapOf(
            ID to auth.currentUser?.uid,
            EMAIL to user?.email,
            NAME to user?.name,
            SURNAME to user?.surname,
            IMAGE to user?.image,
            FAVORITES to user?.favorites
        )
        auth.currentUser?.uid?.let {
            firebaseFirestore.collection(COLLECTION_PATH).document(it)
                .set(userModel)
        }?.await()
        val result = User(
            name = userModel.get(key = NAME).toString(),
            surname = userModel.get(key = SURNAME).toString(),
            email = userModel.get(key = EMAIL).toString(),
            image = userModel.get(key = IMAGE).toString(),
            favorites = userModel.get(key = FAVORITES) as? List<HashMap<String, String>>
        )
        return flowOf(result.toUserDomain()).toResource()
    }

    override suspend fun signInWithGoogle(idToken: String?): Flow<Resource<AuthResult>> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return flowOf(auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                val userModel = hashMapOf(
                    ID to auth.currentUser?.uid,
                    EMAIL to user?.email.orEmpty(),
                    NAME to user?.displayName.orEmpty(),
                    IMAGE to user?.photoUrl.toString()
                )
                auth.currentUser?.uid?.let {
                    firebaseFirestore.collection(COLLECTION_PATH).document(it)
                        .set(userModel)
                }
            }
        }.await()) .toResource()
    }

    override suspend fun fetchUserInfos(): Flow<Resource<com.oguzdogdu.walliescompose.domain.model.auth.User?>> {
        val user = FirebaseAuth.getInstance().currentUser
        val id = user?.uid ?: ""

        val db = FirebaseFirestore.getInstance()
        val userDocument = db.collection(COLLECTION_PATH).document(id).get().await()

        val name = userDocument?.getString(NAME)
        val email = userDocument?.getString(EMAIL)
        val profileImageUrl = userDocument?.getString(IMAGE)
        val surname = userDocument?.getString(SURNAME)
        val favorites = userDocument?.get(FAVORITES) as? List<HashMap<String, String>>?

        val result = User(
            name = name,
            surname = surname,
            email = email,
            image = profileImageUrl,
            favorites = favorites
        )
        return flowOf(result.toUserDomain()).toResource()
    }

    override suspend fun forgotMyPassword(email: String): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }

    override suspend fun updatePassword(password: String?) : Flow<Resource<Task<Void>?>> = flow {
        password?.let { newPassword ->
            val updateTask = auth.currentUser?.updatePassword(newPassword)
            emit(updateTask)
        }
    }.toResource()

    override suspend fun changeEmail(email: String?, password: String?) : Flow<Resource<Task<Void>?>> = flow {
        val credential =
            auth.currentUser?.email?.let { password?.let { password ->
                EmailAuthProvider.getCredential(it,
                    password
                )
            } }
        credential?.let {
            auth.currentUser?.reauthenticate(it)?.await()
            emit(auth.currentUser?.updateEmail(email.orEmpty()))
            firebaseFirestore.collection(COLLECTION_PATH).document(auth.currentUser!!.uid)
                .update(EMAIL, email)
        }
    }.toResource()

    override suspend fun changeUsername(name: String?): Flow<Resource<String>> {
        return flow {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                firebaseFirestore.collection(COLLECTION_PATH).document(userId).update(NAME, name)
                    .await()
                emit(name ?: "")
            } else {
                emit("User ID is null")
            }
        }.catch { e ->
            emit("An error occurred: ${e.message}")
        }.toResource()
    }


    override suspend fun changeSurname(surname: String?): Flow<Resource<String>> {
        return flow {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                firebaseFirestore.collection(COLLECTION_PATH).document(userId).update(SURNAME, surname)
                    .await()
                emit(surname ?: "")
            } else {
                emit("User ID is null")
            }
        }.catch { e ->
            emit("An error occurred: ${e.message}")
        }.toResource()
    }

    override suspend fun signOut() = auth.signOut()

    override suspend fun changeProfilePhoto(photo: String?) {
        auth.currentUser?.uid?.let {
            firebaseFirestore.collection(COLLECTION_PATH).document(it).update(IMAGE, photo)
        }?.await()
    }
}