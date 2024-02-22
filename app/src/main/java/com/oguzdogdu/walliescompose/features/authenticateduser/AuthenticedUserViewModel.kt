package com.oguzdogdu.walliescompose.features.authenticateduser

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.oguzdogdu.walliescompose.data.common.Constants
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class AuthenticatedUserViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _userState: MutableStateFlow<AuthenticatedUserScreenState?> = MutableStateFlow(null)
    val userState = _userState.asStateFlow()

    private val _userAuthState = MutableStateFlow(false)
    val userAuthState = _userAuthState.asStateFlow()

    private val _changeProfilePhotoBottomSheetOpenStat = MutableStateFlow(false)
    val changeProfilePhotoBottomSheetOpenStat = _changeProfilePhotoBottomSheetOpenStat.asStateFlow()

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

            is AuthenticatedUserEvent.OpenChangeProfileBottomSheet -> {
                _changeProfilePhotoBottomSheetOpenStat.value = event.isOpen
            }

            is AuthenticatedUserEvent.ChangeProfileImage -> {
                changeProfileImage(event.photoUri)
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

    private fun changeProfileImage(uri: Uri?) {
        viewModelScope.launch {
            val photoUploadProcess = async { checkChangedPhotoStatus(uri = uri) }
            photoUploadProcess.await()
            authenticationRepository.changeProfilePhoto(photo = uploadImage(uri = uri))
            if (photoUploadProcess.isCompleted) {
                fetchUserDatas()
            }
        }
    }

    private fun checkChangedPhotoStatus(uri: Uri?) {
        viewModelScope.launch {
            if (uploadImage(uri)?.isNotEmpty() == true) {
               _changeProfilePhotoBottomSheetOpenStat.value = false
            }
        }
    }

    private suspend fun uploadImage(uri: Uri?): String? = suspendCancellableCoroutine { continuation ->
        if (uri == null) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        val storageRef = FirebaseStorage.getInstance().reference.child(Constants.IMAGE)
        val childRef = storageRef.child(System.currentTimeMillis().toString())

        val uploadTask = childRef.putFile(uri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { exception ->
                    throw exception
                }
            }
            childRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                continuation.resume(downloadUri?.toString())
            } else {
                continuation.resume(null)
            }
        }

        continuation.invokeOnCancellation {
            uploadTask.cancel()
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            authenticationRepository.signOut()
        }
    }
}
