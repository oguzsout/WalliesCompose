package com.oguzdogdu.walliescompose.features.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.login.components.ButtonGoogleSignIn
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import kotlinx.coroutines.launch

@Composable
fun LoginScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    googleAuthUiClient: GoogleAuthUiClient,
    navigateToHome: () -> Unit,
    navigateToSignInEmail: () -> Unit,
    onContinueWithoutLoginClick: () -> Unit,
    navigateBack: () -> Unit
) {

    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    BackHandler(enabled = true) {
        navigateBack.invoke()
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LoginScreenContent(state = loginState,
                modifier = modifier,
                googleAuthUiClient = googleAuthUiClient,
                onContinueWithoutLoginClick = {
                    onContinueWithoutLoginClick.invoke()
                },
                onGoogleSignInButtonClicked = { idToken ->
                    viewModel.handleUIEvent(LoginScreenEvent.GoogleButton(idToken = idToken))
                },
                navigateToHome = {
                    navigateToHome.invoke()
                },
                navigateToSignInEmail = {
                    navigateToSignInEmail.invoke()
                })
        }
    }
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    modifier: Modifier,
    googleAuthUiClient: GoogleAuthUiClient,
    onContinueWithoutLoginClick: () -> Unit,
    onGoogleSignInButtonClicked: (String) -> Unit,
    navigateToSignInEmail: () -> Unit,
    navigateToHome: () -> Unit
) {

    var loading by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                Log.d("TAG", "Activity")
                if (result.resultCode == Activity.RESULT_OK) {
                    scope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(
                            intent = result.data ?: return@launch
                        )
                        onGoogleSignInButtonClicked.invoke(signInResult.orEmpty())
                    }
                }
            })

    LaunchedEffect(state) {
        when (state) {
            is LoginState.UserSignIn -> {
                navigateToHome.invoke()
            }

            is LoginState.ErrorSignIn -> {
                Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
            }

            is LoginState.UserNotSignIn -> {}
            is LoginState.Loading -> {
                loading = state.loading
            }

            else -> {}
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_color__1_),
                contentDescription = stringResource(
                    id = R.string.app_logo
                ),
                tint = Color.Unspecified,
                modifier = modifier.size(width = 72.dp, height = 72.dp)
            )
            Text(
                text = stringResource(R.string.sign_in_to_wallies),
                fontSize = 24.sp,
                fontFamily = bold
            )
            Text(
                text = stringResource(R.string.sign_in_desc),
                fontSize = 16.sp,
                fontFamily = regular,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonGoogleSignIn(
                modifier = modifier, onGoogleSignInButtonClick = {
                    scope.launch {
                        val intentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                intentSender = intentSender ?: return@launch
                            ).build()
                        )
                    }
                }, loading = loading
            )

            Button(
                onClick = {
                    navigateToSignInEmail.invoke()
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lush_green)
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.continue_with_email),
                    fontSize = 14.sp,
                    fontFamily = medium,
                    color = Color.White
                )
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = stringResource(id = R.string.or),
                    modifier = modifier.padding(horizontal = 12.dp),
                    fontSize = 16.sp,
                    fontFamily = bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )

                HorizontalDivider(
                    modifier = modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Text(text = stringResource(id = R.string.continue_without_registration),
                fontSize = 16.sp,
                fontFamily = bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                modifier = modifier.clickable {
                    onContinueWithoutLoginClick.invoke()
                })
        }
    }
}