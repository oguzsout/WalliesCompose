package com.oguzdogdu.walliescompose.features.login.forgotpassword

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.login.components.EmailTextField
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: ForgotMyPasswordViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateBack:() -> Unit
) {

    val forgotPasswordScreenState by viewModel.forgotPasswordState.collectAsStateWithLifecycle()
    val snackState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.handleUIEvent(ForgotPasswordScreenEvent.ButtonState)
    }

    BackHandler(enabled = true) {
        navigateBack.invoke()
    }

    Scaffold(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp), topBar = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navigateBack.invoke() },
                modifier = modifier.wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier.wrapContentSize()
                )
            }

            Text(
                modifier = modifier,
                text = stringResource(id = R.string.forgot_password_title),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }, snackbarHost = {
        SnackbarHost(
            hostState = snackState
        )
    }) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            ForgotPasswordScreenContent(state = forgotPasswordScreenState,modifier = modifier, onEmailChange = { email ->
                viewModel.setEmail(email)
                viewModel.handleUIEvent(ForgotPasswordScreenEvent.ButtonState)
            },
               onSendToEmailButtonClick = { email ->
                  viewModel.handleUIEvent(ForgotPasswordScreenEvent.SendEmail(email = email))
                },
                navigateToHome = {
                    navigateToHome.invoke()
                },
                showMessage = {message ->
                    coroutineScope.launch {
                        snackState.showSnackbar(message)
                    }
                }
            )
        }
    }
}


@Composable
fun ForgotPasswordScreenContent(
    state: ForgotPasswordScreenState?,
    modifier: Modifier,
    onEmailChange: (String) -> Unit,
    onSendToEmailButtonClick: (String) -> Unit,
    showMessage: (String) -> Unit,
    navigateToHome: () -> Unit,
) {
    var buttonEnabled by remember { mutableStateOf(false) }
    var email by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    LaunchedEffect(state) {
        when (state) {
            is ForgotPasswordScreenState.ButtonEnabled -> {
                buttonEnabled = state.isEnabled
            }

            is ForgotPasswordScreenState.SendEmailError -> {
                Toast.makeText(context, state.error,Toast.LENGTH_SHORT).show()
            }

            is ForgotPasswordScreenState.Loading -> {
                loading = state.loading
            }
            is ForgotPasswordScreenState.ProcessStat -> {
                if (state.isCompleted){
                    showMessage.invoke(context.getString(R.string.reset_password_validation_desc))
                }
            }

            else -> {}
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                ,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailTextField(modifier = modifier, onChangeEmail = {
                email = it
                onEmailChange.invoke(it)
            })
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
                    onSendToEmailButtonClick.invoke(email)
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
                            text = stringResource(R.string.send_info),
                            fontSize = 14.sp,
                            fontFamily = medium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}