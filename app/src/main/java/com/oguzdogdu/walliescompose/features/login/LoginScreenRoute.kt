package com.oguzdogdu.walliescompose.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.bold
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import com.oguzdogdu.walliescompose.util.FieldValidators.isStringContainNumber
import com.oguzdogdu.walliescompose.util.FieldValidators.isStringContainSpecialCharacter
import com.oguzdogdu.walliescompose.util.FieldValidators.isStringLowerAndUpperCase
import com.oguzdogdu.walliescompose.util.FieldValidators.isValidEmail

@Composable
fun LoginScreenRoute(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier
        .fillMaxSize()) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
         LoginScreenContent(modifier = modifier)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreenContent(modifier: Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val icon: Painter = if (passwordVisibility)
        painterResource(id = R.drawable.eye_password_hide)
    else
        painterResource(id = R.drawable.eye_password_show)
    Box(
        modifier = modifier
            .fillMaxSize()

    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
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
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = { Text("Email") },
                    placeholder = { Text("Enter your email") },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    isError = email.isNotEmpty() && !isValidEmail(email),
                    supportingText = {
                        if (email.isEmpty() && !isValidEmail(email)) {
                            Text(text = stringResource(id = R.string.required_field))
                        }
                    }
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    placeholder = { Text(text = "Password") },
                    label = { Text(text = "Password") },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility = !passwordVisibility
                        }) {
                            Icon(
                                painter = icon,
                                contentDescription = "Visibility Icon"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    isError = !isStringContainNumber(password) && !isStringLowerAndUpperCase(
                        password
                    ) && !isStringContainSpecialCharacter(password),
                    supportingText = {
                    when {
                        password.isEmpty() -> Text(text = stringResource(id = R.string.required_field))
                        password.length < 6 -> Text(text = stringResource(id = R.string.password_length))
                        !isStringContainNumber(password) -> Text(text = stringResource(id = R.string.required_at_least_1_digit))
                        !isStringLowerAndUpperCase(password) -> Text(text = stringResource(id = R.string.password_must_contain_upper_and_lower_case_letters))
                        !isStringContainSpecialCharacter(password) -> Text(text = stringResource(id = R.string.one_special_character_required))
                    }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Text(
                    text = stringResource(id = R.string.forgot_password_title),
                    fontSize = 12.sp,
                    fontFamily = medium,
                    modifier = modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp)
                )

                Button(
                    onClick = {},
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

                Button(
                    onClick = {},
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = ""
                    )
                    Spacer(modifier = modifier.size(8.dp))
                    Text(
                        text = stringResource(id = R.string.sign_in_with_google),
                        fontSize = 14.sp,
                        fontFamily = medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
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
                )
            }
        }
    }
}