package com.oguzdogdu.walliescompose.features.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.util.FieldValidators

@Composable
fun EmailTextField(modifier: Modifier,onChangeEmail: (String) -> Unit) {

    var email by rememberSaveable { mutableStateOf("") }

    val emailIsError by remember {
        derivedStateOf {
            email.isNotEmpty() && !FieldValidators.isValidEmail(email)
        }
    }

    OutlinedTextField(
        value = email,
        onValueChange = {
            email = it
            onChangeEmail.invoke(it)
        },
        label = { Text(stringResource(id = R.string.email)) },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        isError = emailIsError,
        supportingText = {
            if (emailIsError) {
                Text(text = stringResource(id = R.string.invalid_email))
            }
        }
    )
}