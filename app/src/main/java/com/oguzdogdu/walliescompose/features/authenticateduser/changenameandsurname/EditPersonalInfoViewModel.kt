package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.core.BaseViewModel
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.appstate.Duration
import com.oguzdogdu.walliescompose.features.appstate.MessageContent
import com.oguzdogdu.walliescompose.features.appstate.MessageType
import com.oguzdogdu.walliescompose.features.appstate.SnackbarModel
import com.oguzdogdu.walliescompose.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPersonalInfoViewModel @Inject constructor(
    private val authenticationRepository: UserAuthenticationRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<EditPersonalInfoState, EditPersonalInfoEvent, EditPersonalInfoEffect>(
    EditPersonalInfoState()
) {

    private val nameFlow: MutableStateFlow<String?> =
        MutableStateFlow(savedStateHandle.toRoute<Screens.ChangeNameAndSurnameScreenRoute>().name)
    private val surnameFlow: MutableStateFlow<String?> =
        MutableStateFlow(savedStateHandle.toRoute<Screens.ChangeNameAndSurnameScreenRoute>().surname)

    val initialData: StateFlow<EditPersonalInfoState> = combine(
        nameFlow,
        surnameFlow
    ) { name, surname ->
        EditPersonalInfoState(name, surname)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EditPersonalInfoState("", "")
    )

    private val _userState: MutableStateFlow<EditPersonalInfoState> = MutableStateFlow(
        EditPersonalInfoState()
    )
    val userState = _userState.asStateFlow()

    override fun handleEvents(event: EditPersonalInfoEvent) {
        super.handleEvents(event)
        when(event) {
            is EditPersonalInfoEvent.ChangedUserNameAndSurname -> changeUserNameAndSurname(event.name,event.surname)
        }
    }

    private fun changeUserNameAndSurname(name: String?, surname: String?) {
        viewModelScope.launch {
                when {
                    (name?.isEmpty() == true || surname?.isEmpty() == true) ->{
                        sendEffect(
                            EditPersonalInfoEffect.ShowSnackbar(
                                SnackbarModel(
                                    type = MessageType.ERROR,
                                    drawableRes = R.drawable.ic_cancel,
                                    message = MessageContent.PlainString("Name or surname field cannot be left blank"),
                                    duration = Duration.SHORT
                                )
                            )
                        )
                    }
                    else -> {
                        val changeName = authenticationRepository.changeUsername(name)
                        val changeSurName = authenticationRepository.changeSurname(surname)
                        combine(changeName, changeSurName) { username, userSurname ->
                        username.onSuccess { name ->
                           sendEffect(
                                EditPersonalInfoEffect.ShowSnackbar(
                                    SnackbarModel(
                                        type = MessageType.SUCCESS,
                                        drawableRes = R.drawable.ic_completed,
                                        message = MessageContent.ResourceString(R.string.user_name_surname_update),
                                        duration = Duration.SHORT
                                    )
                                )
                            )
                            setState(currentState.copy(name = name))
                        }
                        username.onFailure { error ->
                            sendEffect(
                                EditPersonalInfoEffect.ShowSnackbar(
                                    SnackbarModel(
                                        type = MessageType.ERROR,
                                        drawableRes = R.drawable.ic_cancel,
                                        message = MessageContent.PlainString(error),
                                        duration = Duration.SHORT
                                    )
                                )
                            )
                        }
                        userSurname.onSuccess { surname ->
                            setState(currentState.copy(surname = surname))
                        }
                        userSurname.onFailure { error ->
                            sendEffect(
                                EditPersonalInfoEffect.ShowSnackbar(
                                    SnackbarModel(
                                        type = MessageType.ERROR,
                                        drawableRes = R.drawable.ic_cancel,
                                        message = MessageContent.PlainString(error),
                                        duration = Duration.SHORT
                                    )
                                )
                            )
                        }
                    }.collect()
                }
            }
        }
    }
}
