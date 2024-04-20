package com.oguzdogdu.walliescompose.features.signup

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

@Stable
sealed class SignUpScreenState {
   data object Start: SignUpScreenState()
   data object UserSignUp : SignUpScreenState()
   data class ErrorSignUp(val errorMessage: String) : SignUpScreenState()
   data class Loading(val loading:Boolean) : SignUpScreenState()
   data class PasswordRuleSet(val ruleSet: List<Pair<String,Boolean>> = emptyList()) : SignUpScreenState()
}
