package com.oguzdogdu.walliescompose.features.signup.state

import android.net.Uri
import androidx.compose.runtime.Stable
import com.oguzdogdu.walliescompose.features.signup.PasswordRuleSetContainer

@Stable
data class SignUpUIState(
   val loading: Boolean = false,
   val errorMessage: String? = null,
   val isSignUp: Boolean = false,
   val email: String? = null,
   val password: String? = null,
   val name: String? = null,
   val surName: String? = null,
   val image: Uri? = null,
   val emailAndPasswordIsValid: Boolean = false,
   val ruleSet: List<PasswordRuleSetContainer> = emptyList()
)
