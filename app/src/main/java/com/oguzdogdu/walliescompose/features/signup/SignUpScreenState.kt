package com.oguzdogdu.walliescompose.features.signup

import androidx.compose.runtime.Stable

@Stable
data class SignUpUIState(
   val loading: Boolean = false,
   val errorMessage: String? = null,
   val isSignUp: Boolean = false,
   val ruleSet: List<PasswordRuleSetContainer> = emptyList()
)
