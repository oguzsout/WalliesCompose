package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import androidx.compose.runtime.Stable
import com.oguzdogdu.walliescompose.core.ViewState

@Stable
data class EditPersonalInfoState(
    val name: String? = null,
    val surname: String? = null,
    val bio: String? = null,
    val email: String? = null,
    val profileImage: String? = null
): ViewState

