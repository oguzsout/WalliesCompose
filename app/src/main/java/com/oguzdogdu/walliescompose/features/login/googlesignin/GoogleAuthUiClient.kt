package com.oguzdogdu.walliescompose.features.login.googlesignin

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.compose.runtime.Stable
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.oguzdogdu.walliescompose.R
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

@Stable
class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = com.google.firebase.ktx.Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result =
            try {
                oneTapClient.beginSignIn(
                    buildSignInRequest()
                ).await()
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                null
            }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): String? {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        return credential.googleIdToken

    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}

