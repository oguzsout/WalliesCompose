package com.oguzdogdu.walliescompose.features.signup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.oguzdogdu.walliescompose.domain.model.auth.User
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.signup.state.SignUpSteps
import com.oguzdogdu.walliescompose.features.signup.state.SignUpStepsData
import com.oguzdogdu.walliescompose.features.signup.state.SignUpUIState
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
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(private val authenticationRepository: UserAuthenticationRepository) :
    ViewModel() {

    private val stepsOrder: List<SignUpSteps> = listOf(
        SignUpSteps.EMAIL_PASSWORD,
        SignUpSteps.PHOTO_NAME_SURNAME,
        SignUpSteps.SIGN_UP_STATUS
    )

    private var stepsIndex = 0

    private val _signUpUiState : MutableStateFlow<SignUpUIState> = MutableStateFlow(SignUpUIState())
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _signUpStepsState : MutableStateFlow<SignUpStepsData> = MutableStateFlow(SignUpStepsData())
    val signUpStepsState = _signUpStepsState.asStateFlow()

    private var userEmailAndPasswordIsValid = MutableStateFlow(false)
    private var userPhotoNameAndSurnameIsValid = MutableStateFlow(false)

    init {
       _signUpStepsState.value = createSignUpStepsScreenData()
    }

    fun handleUiEvents(event: SignUpScreenEvent) {
        when(event) {
            is SignUpScreenEvent.ResumeToSignUp -> {
                combineValidations(email = event.email, password = event.password)
            }

            is SignUpScreenEvent.CheckPasswordRule -> {
                executePasswordRuleSet(event.password)
            }

            is SignUpScreenEvent.SendEmail ->   setStateOfEmail(email = event.email)
            is SignUpScreenEvent.SendPassword -> setStateOfPassword(password = event.password)
            is SignUpScreenEvent.SendName -> setStateOfName(name = event.name)
            is SignUpScreenEvent.SendSurname -> setStateOfSurname(surname = event.surname)
            is SignUpScreenEvent.SendUri -> setStateOfUri(uri = event.imageUri)
            SignUpScreenEvent.ExecuteFlowOfSignUp -> {
                if (_signUpUiState.value.image != null ||
                    _signUpUiState.value.name?.isNotEmpty() == true ||
                    _signUpUiState.value.surName?.isNotEmpty() == true) {
                    userPhotoNameAndSurnameIsValid.value = true
                    executeFlowOfUserSignUp()
                }
            }
        }
    }

    private fun setStateOfEmail(email: String?) {
        viewModelScope.launch {
            _signUpUiState.update {
                it.copy(email = email)
            }
        }
    }

    private fun setStateOfPassword(password: String?) {
        viewModelScope.launch {
            _signUpUiState.update {
                it.copy(password = password)
            }
        }
    }
    private fun setStateOfName(name: String?) {
        viewModelScope.launch {
            _signUpUiState.update {
                it.copy(name = name)
            }
        }
    }
    private fun setStateOfSurname(surname: String?) {
        viewModelScope.launch {
            _signUpUiState.update {
                it.copy(surName = surname)
            }
        }
    }
    private fun setStateOfUri(uri: Uri?) {
        viewModelScope.launch {
            _signUpUiState.update {
                it.copy(image = uri)
            }
        }
    }

    fun onPreviousPressed() {
        if (stepsIndex == 0) {
            throw IllegalStateException("onPreviousPressed when on question 0")
        }
        changeStep(stepsIndex - 1)
    }

    fun onNextPressed() {
        viewModelScope.launch {
            if (getIsNextEnabled()) {
                _signUpUiState.update {
                    it.copy(loading = true)
                }
                delay(1000)
                changeStep(stepsIndex + 1)
                _signUpUiState.update {
                    it.copy(loading = false)
                }
            } else {
                _signUpUiState.update { it.copy(emailAndPasswordIsValid = false) }
            }
            if (stepsIndex == stepsOrder.size - 1) {
                _signUpStepsState.update {
                    it.copy(shouldShowDoneButton = true)
                }
            }
        }
    }

    private fun changeStep(newStepIndex: Int) {
        viewModelScope.launch {
            stepsIndex = newStepIndex
            _signUpStepsState.update {
                it.copy(stepIndex = stepsIndex, signUpSteps = stepsOrder[stepsIndex])
            }
        }
    }

    private fun executeFlowOfUserSignUp() {
        viewModelScope.launch {
            if (userEmailAndPasswordIsValid.value) {
                authenticationRepository.signUp(
                    user = User(
                        name = _signUpUiState.value.name,
                        surname = _signUpUiState.value.surName,
                        email = _signUpUiState.value.email,
                        image = uploadImage(_signUpUiState.value.image)
                    ),
                    password = _signUpUiState.value.password
                ).collect { result ->
                    result.onLoading { _signUpUiState.update { it.copy(loading = true) } }
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
            }.map {valid ->
                userEmailAndPasswordIsValid.value = valid
            }.collect()
            getIsNextEnabled()
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
    private fun getIsNextEnabled(): Boolean {
        return when (stepsOrder[stepsIndex]) {
            SignUpSteps.EMAIL_PASSWORD -> userEmailAndPasswordIsValid.value
            SignUpSteps.PHOTO_NAME_SURNAME -> userPhotoNameAndSurnameIsValid.value
            SignUpSteps.SIGN_UP_STATUS -> false
        }
    }

    private fun createSignUpStepsScreenData(): SignUpStepsData {
        return SignUpStepsData(
            stepIndex = stepsIndex,
            stepCount = stepsOrder.size,
            shouldShowPreviousButton = stepsIndex > 0,
            shouldShowDoneButton = stepsIndex == stepsOrder.size - 1,
            signUpSteps = stepsOrder[stepsIndex],
        )
    }
    private suspend fun uploadImage(uri: Uri?): String? = suspendCancellableCoroutine { continuation ->
        val storageRef = FirebaseStorage.getInstance().reference.child(com.oguzdogdu.walliescompose.data.common.Constants.IMAGE)
        val childRef = storageRef.child(System.currentTimeMillis().toString())

        uri?.let { uri ->
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
        } ?: run {
            continuation.resume(null)
        }
    }
}