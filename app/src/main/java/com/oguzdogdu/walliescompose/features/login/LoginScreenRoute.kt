package com.oguzdogdu.walliescompose.features.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.home.LoadingState
import com.oguzdogdu.walliescompose.features.login.components.ButtonGoogleSignIn
import com.oguzdogdu.walliescompose.features.login.components.EmailTextField
import com.oguzdogdu.walliescompose.features.login.components.PasswordTextField
import com.oguzdogdu.walliescompose.features.login.components.rememberFirebaseAuthLauncher
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun LoginScreenRoute(modifier: Modifier = Modifier,viewModel: LoginViewModel = hiltViewModel(),navigateToHome:() -> Unit,onContinueWithoutLoginClick: () -> Unit) {

    val loginState by viewModel.loginState.collectAsState()

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.handleUIEvent(LoginScreenEvent.ButtonState)
    }

    Scaffold(modifier = modifier
        .fillMaxSize()) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LoginScreenContent(modifier = modifier, state = loginState, onEmailChange = { email ->
                viewModel.setEmail(email)
                viewModel.handleUIEvent(LoginScreenEvent.ButtonState)
            },
                onPasswordChange = { password ->
                    viewModel.setPassword(password)
                    viewModel.handleUIEvent(LoginScreenEvent.ButtonState)
                }, onLoginButtonClick = { email, password ->
                    viewModel.handleUIEvent(LoginScreenEvent.UserSignIn(email, password))
                },
                onContinueWithoutLoginClick = {
                    onContinueWithoutLoginClick.invoke()
                },
                onGoogleSignInButtonClicked = {idToken ->
                    viewModel.handleUIEvent(LoginScreenEvent.GoogleButton(idToken = idToken))
                },
                navigateToHome = {
                    navigateToHome.invoke()
                }
            )
        }
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier,
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginButtonClick:(String,String) -> Unit,
    onContinueWithoutLoginClick: () -> Unit,
    onGoogleSignInButtonClicked:(String) -> Unit,
    navigateToHome: () -> Unit
) {
    var buttonEnabled by remember { mutableStateOf(false) }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var loading by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            onGoogleSignInButtonClicked.invoke(result)
        },
        onAuthError = {
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }
    )
    LaunchedEffect(state) {
        when(state) {
            is LoginState.UserSignIn -> {
                navigateToHome.invoke()
            }
            is LoginState.ErrorSignIn -> {
                Toast.makeText(context, state.errorMessage,Toast.LENGTH_LONG).show()
            }
            is LoginState.UserNotSignIn -> {}
            is LoginState.Loading -> {
               loading = state.loading
            }
            is LoginState.ButtonEnabled -> {
                buttonEnabled = state.isEnabled
            }
            else -> {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            LoadingState(modifier = modifier)
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 32.dp, top = 32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.login_title),
                    fontSize = 24.sp,
                    fontFamily = bold
                )
                Text(
                    text = stringResource(id = R.string.please_sign_in_to_continue),
                    fontSize = 16.sp,
                    fontFamily = bold
                )
            }

            Card(
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    EmailTextField(modifier = modifier, onChangeEmail = {
                        email = it
                        onEmailChange.invoke(it)
                    })

                    PasswordTextField(modifier = modifier, onChangePassword = {
                        password = it
                        onPasswordChange.invoke(it)
                    })

                    Text(
                        text = stringResource(id = R.string.forgot_password_title),
                        fontSize = 12.sp,
                        fontFamily = medium,
                        modifier = modifier
                            .align(Alignment.End)
                            .padding(end = 16.dp)
                    )

                    Button(
                        onClick = {
                            onLoginButtonClick.invoke(email,password)
                        },
                        enabled = buttonEnabled,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 14.sp,
                            fontFamily = medium,
                            color = Color.Black
                        )
                    }
                    ButtonGoogleSignIn(modifier = modifier, onGoogleSignInButtonClick = {
                        val gso =
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .requestIdToken("225181955346-optbgqie0019o21o6d77h7b5inf7op6s.apps.googleusercontent.com")
                                .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                    })

                    Text(
                        buildAnnotatedString {
                            append(stringResource(id = R.string.not_registered))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(" ")
                                append(stringResource(id = R.string.sign_up_title))
                                append(" ! ")
                            }
                        },
                        fontSize = 16.sp,
                        fontFamily = regular,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = stringResource(id = R.string.or),
                            modifier = modifier.padding(horizontal = 12.dp),
                            fontSize = 16.sp,
                            fontFamily = regular,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )

                        Divider(
                            modifier = modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.continue_without_registration),
                        fontSize = 16.sp,
                        fontFamily = regular,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = modifier.clickable {
                            onContinueWithoutLoginClick.invoke()
                        }
                    )
                }
            }
        }
    }
}