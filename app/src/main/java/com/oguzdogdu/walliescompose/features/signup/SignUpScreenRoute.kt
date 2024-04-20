package com.oguzdogdu.walliescompose.features.signup

import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
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
import com.oguzdogdu.walliescompose.features.signup.component.PasswordRuleSetBox
import com.oguzdogdu.walliescompose.features.signup.component.TextFieldWithoutSubText
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenRoute(modifier: Modifier = Modifier,onBackClick: () -> Unit,viewModel: SignUpScreenViewModel = hiltViewModel()) {

    val signUpState by viewModel.signUpUiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUiEvents(SignUpScreenEvent.ExecuteValidation)
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(containerColor = MaterialTheme.colorScheme.background,
                    Color.Transparent, Color.Transparent, Color.Transparent, Color.Transparent),
                title = {},
                actions = {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = { onBackClick.invoke() },
                            modifier = modifier
                                .wrapContentSize()
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
                            modifier = modifier,
                            text = stringResource(R.string.text_create_account),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 16.sp,
                            fontFamily = medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            SignUpScreenContent(
                state = signUpState,
                modifier = modifier,
                passwordCheck = {
                    viewModel.executePasswordRuleset(it)
                },
                continueButtonClick = { email, password ->
                    viewModel.handleUiEvents(SignUpScreenEvent.ResumeToSignUp(email, password))
                },
            )
        }
    }
}

@Composable
fun SignUpScreenContent(
    state: SignUpScreenState,
    modifier: Modifier,
    passwordCheck: (String) -> Unit,
    continueButtonClick: (String, String) -> Unit,
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var ruleSetVisible by remember {
        mutableStateOf(false)
    }
    var loading by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(state) {
        when (state) {
            is SignUpScreenState.Start -> {}

            is SignUpScreenState.UserSignUp -> {
                Toast.makeText(context, context.getString(R.string.success_sign), Toast.LENGTH_LONG)
                    .show()
            }

            is SignUpScreenState.ErrorSignUp -> {
                Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
            }

            is SignUpScreenState.Loading -> {
                loading = state.loading
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailTextField(modifier = modifier, onChangeEmail = {
                email = it
            })

            TextFieldWithoutSubText(modifier = modifier, onChangePassword = {
                ruleSetVisible = it != ""
                password = it
                passwordCheck.invoke(it)
            })

            PasswordRuleSet(state = state, ruleSetVisible = ruleSetVisible, modifier = modifier)
        }
        Button(
            onClick = {
                continueButtonClick.invoke(email, password)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            when (loading) {
                true -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                }

                false -> {
                    Text(
                        text = stringResource(R.string.text_continue),
                        fontSize = 14.sp,
                        fontFamily = medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PasswordRuleSet(
    state: SignUpScreenState,
    ruleSetVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val ruleSetVisibility = rememberUpdatedState(newValue = ruleSetVisible)
    val stateOfRuleSet = remember { mutableStateOf(listOf(Pair("",false))) }

    LaunchedEffect(state) {
        when (state) {
            is SignUpScreenState.PasswordRuleSet -> {
               stateOfRuleSet.value = state.ruleSet
            }

            else -> {}
        }
    }

    if (ruleSetVisibility.value) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                Color.Transparent, Color.Transparent, Color.Transparent
            ),
            modifier = modifier
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .border(1.dp, Color.LightGray, shape = RoundedCornerShape(16.dp)),
        ) {
            LazyColumn(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(16.dp),
                state = rememberLazyListState()
            ) {
                items(items = stateOfRuleSet.value) {
                    PasswordRuleSetBox(title = it.first, validate = it.second)

                }
            }
        }
    }
}
