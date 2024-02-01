package com.oguzdogdu.walliescompose.features.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.util.FieldValidators

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordTextField(modifier: Modifier, onChangePassword: (String) -> Unit) {

    var password by rememberSaveable { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }

    val icon: Painter = if (passwordVisibility) painterResource(id = R.drawable.eye_password_hide)
    else painterResource(id = R.drawable.eye_password_show)

    val keyboardController = LocalSoftwareKeyboardController.current

    val passwordErrorCheck by remember {
        derivedStateOf {
            FieldValidators.isValidPasswordChecksStatus(input = password)
        }
    }

    val passwordIsError by remember {
        derivedStateOf {
            FieldValidators.isValidPasswordCheck(password)
        }
    }

    OutlinedTextField(value = password,
        onValueChange = {
            password = it
            onChangePassword.invoke(it)
        },
        placeholder = { Text(text = stringResource(id = R.string.password)) },
        label = { Text(stringResource(id = R.string.password)) },
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(
                    painter = icon, contentDescription = "Visibility Icon"
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        visualTransformation = if (passwordVisibility) VisualTransformation.None
        else PasswordVisualTransformation(),
        isError = password.isNotEmpty() && !passwordIsError,
        supportingText = {
            if (password.isNotEmpty() && passwordErrorCheck != 0) {
                Text(text = stringResource(id = passwordErrorCheck))
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}