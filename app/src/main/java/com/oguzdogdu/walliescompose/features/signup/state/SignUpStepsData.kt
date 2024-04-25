package com.oguzdogdu.walliescompose.features.signup.state

import androidx.compose.runtime.Stable

@Stable
data class SignUpStepsData(
    val stepIndex: Int? = null,
    val stepCount: Int? = null,
    val shouldShowPreviousButton: Boolean = false,
    val shouldShowDoneButton: Boolean? = false,
    val signUpSteps: SignUpSteps? = null,
)