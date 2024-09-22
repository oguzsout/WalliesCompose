package com.oguzdogdu.walliescompose.features.login.signinwithemail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.login.components.EmailTextField
import com.oguzdogdu.walliescompose.features.login.components.PasswordTextField
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun SignInWithEmailScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: SignInWithEmailViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateBack:() -> Unit
) {

    val signInEmailScreenState by viewModel.signInEmailState.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.handleUIEvent(SignInWithEmailEvents.ButtonState)
    }

    BackHandler(enabled = true) {
        navigateBack.invoke()
    }

    Scaffold(modifier = modifier
        .fillMaxSize()) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            SignInWithEmailScreenContent(state = signInEmailScreenState,modifier = modifier, onEmailChange = { email ->
                viewModel.setEmail(email)
                viewModel.handleUIEvent(SignInWithEmailEvents.ButtonState)
            },
                onPasswordChange = { password ->
                    viewModel.setPassword(password)
                    viewModel.handleUIEvent(SignInWithEmailEvents.ButtonState)
                }, onLoginButtonClick = { email, password ->
                    viewModel.handleUIEvent(SignInWithEmailEvents.UserSignIn(email, password))
                },
                navigateToHome = {
                    navigateToHome.invoke()
                }, navigateToBack = {
                    navigateBack.invoke()
                },
                navigateToForgotPassword = {
                    navigateToForgotPassword.invoke()
                },
                navigateToSignUpScreen = {
                    navigateToSignUpScreen.invoke()
                }
            )
        }
    }
}

@Composable
fun SignInWithEmailScreenContent(
    state: SignInWithEmailState,
    modifier: Modifier,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginButtonClick: (String, String) -> Unit,
    navigateToHome: () -> Unit,
    navigateToBack: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToSignUpScreen: () -> Unit
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

    LaunchedEffect(state) {
        when (state) {
            is SignInWithEmailState.UserSignIn -> {
                navigateToHome.invoke()
            }

            is SignInWithEmailState.ErrorSignIn -> {
                Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
            }

            is SignInWithEmailState.UserNotSignIn -> {}
            is SignInWithEmailState.Loading -> {
                loading = state.loading
            }

            is SignInWithEmailState.ButtonEnabled -> {
                buttonEnabled = state.isEnabled
            }

            else -> {}
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(
                onClick = {
                          navigateToBack.invoke()
                },
                modifier = modifier
                    .wrapContentSize()
                    .align(Alignment.Start)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier
                        .wrapContentSize()
                )
            }

            Text(
                text = stringResource(R.string.sign_in_desc_title),
                fontSize = 16.sp,
                fontFamily = medium,
                modifier = modifier.padding(horizontal = 16.dp)
            )

            EmailTextField(onChangeEmail = {
                email = it
                onEmailChange.invoke(it)
            })

            PasswordTextField(onChangePassword = {
                password = it
                onPasswordChange.invoke(it)
            })

            Text(
                buildAnnotatedString {
                append(stringResource(id = R.string.forgot_password_title))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" ")
                    append(stringResource(R.string.reset_here))
                }
            },
                fontSize = 14.sp,
                fontFamily = medium,
                modifier = modifier
                    .align(Alignment.CenterHorizontally).clickable {
                        navigateToForgotPassword.invoke()
                    }
            )
        }

        Column(
            modifier = modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    onLoginButtonClick.invoke(email,password)
                },
                enabled = buttonEnabled,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                when {
                    loading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(R.string.sign_in),
                            fontSize = 14.sp,
                            fontFamily = medium,
                            color = Color.White
                        )
                    }
                }
            }

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
                modifier = modifier
                    .padding(8.dp).clickable {
                        navigateToSignUpScreen.invoke()
                    }
            )
        }
    }
}