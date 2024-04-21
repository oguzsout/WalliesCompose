package com.oguzdogdu.walliescompose.features.signup.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.R

@Composable
fun EmailTextFieldWithoutSubText(onChangeEmail: (String) -> Unit, modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }

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
    )
}