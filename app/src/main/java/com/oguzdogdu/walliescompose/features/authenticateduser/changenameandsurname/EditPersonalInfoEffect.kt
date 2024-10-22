package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import com.oguzdogdu.walliescompose.core.ViewEffect
import com.oguzdogdu.walliescompose.features.appstate.SnackbarModel

sealed interface EditPersonalInfoEffect : ViewEffect{
    data class ShowSnackbar(val snackbarModel: SnackbarModel) : EditPersonalInfoEffect
}