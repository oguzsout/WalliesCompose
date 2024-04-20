package com.oguzdogdu.walliescompose.features.signup.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch

@Composable
fun TextFieldWithoutSubText(onChangePassword: (String) -> Unit, modifier: Modifier = Modifier) {

    var password by remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }

    val icon: Painter = if (passwordVisibility) painterResource(id = R.drawable.eye_password_hide)
    else painterResource(id = R.drawable.eye_password_show)

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = password,
        onValueChange = { value ->
                password = value
                onChangePassword.invoke(password)
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
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        visualTransformation = if (passwordVisibility) VisualTransformation.None
        else PasswordVisualTransformation(),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    )
}