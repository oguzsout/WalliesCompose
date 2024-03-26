package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditUsernameSurnameViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {

    private val _userState: MutableStateFlow<EditUsernameSurnameScreenState?> = MutableStateFlow(
        null
    )
    val userState = _userState.asStateFlow()

    fun handleUIEvent(event: EditUsernameSurnameEvent) {
        when (event) {
            is EditUsernameSurnameEvent.ChangedUserNameAndSurname -> {
                changeUserNameAndSurname(event.name,event.surname)
            }
        }
    }

    private fun changeUserNameAndSurname(name: String?, surname: String?) {
        viewModelScope.launch {
            when {
                name.isNullOrBlank() or surname.isNullOrBlank() -> {
                    _userState.update {
                        EditUsernameSurnameScreenState.UserInfoError("Empty Fields")
                    }
                }
                else -> {
                    val changeName = authenticationRepository.changeUsername(name)
                    val changeSurName = authenticationRepository.changeSurname(surname)
                    combine(changeName, changeSurName) { username, surname ->
                        username.onSuccess { name ->
                            _userState.update {
                                EditUsernameSurnameScreenState.UserInfos(name = name)
                            }
                        }
                        username.onFailure { error ->
                            _userState.update {
                                EditUsernameSurnameScreenState.UserInfoError(errorMessage = error)
                            }
                        }
                        surname.onSuccess { userSurname ->
                            _userState.update {
                                EditUsernameSurnameScreenState.UserInfos(surname = userSurname)
                            }
                        }
                        surname.onFailure { error ->
                            _userState.update {
                                EditUsernameSurnameScreenState.UserInfoError(errorMessage = error)
                            }
                        }
                    }.collect()
                }
            }
        }
    }
}
