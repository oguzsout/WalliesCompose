package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium

@Composable
fun ChangeNameAndSurnameScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: EditUsernameSurnameViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val stateOfEmail by viewModel.userState.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navigateBack.invoke() }, modifier = modifier.wrapContentSize()
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
                text = stringResource(id = R.string.edit_user_info_title),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            ChangeEmailScreenContent(modifier = modifier, state = stateOfEmail, onChangeUserInfoButtonClick = { name, surname ->
                viewModel.handleUIEvent(EditUsernameSurnameEvent.ChangedUserNameAndSurname(name = name,surname = surname))
            })
        }
    }
}

@Composable
fun ChangeEmailScreenContent(
    modifier: Modifier,
    state: EditUsernameSurnameScreenState?,
    onChangeUserInfoButtonClick: (String,String) -> Unit,
) {
    val context = LocalContext.current

    var name by remember {
        mutableStateOf("")
    }
    var surname by remember {
        mutableStateOf("")
    }

    LaunchedEffect(state) {
        when (state) {
            is EditUsernameSurnameScreenState.UserInfoError -> {
                Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
            }

            is EditUsernameSurnameScreenState.UserInfos -> {
                Toast.makeText(context,
                    context.getString(R.string.user_name_surname_update), Toast.LENGTH_SHORT).show()
            }

            null -> {

            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(R.string.name),
                fontSize = 16.sp,
                fontFamily = medium,
                modifier = modifier
            )

            Spacer(modifier = modifier.size(8.dp))

            TextField(modifier = modifier.fillMaxWidth(), value = name, onValueChange = {
                name = it
            }, shape = ShapeDefaults.Medium, colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ), singleLine = true, trailingIcon = {
                if (name.isNotEmpty()) {
                    IconButton(
                        onClick = { name = "" }, modifier = modifier.wrapContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = modifier.wrapContentSize()
                        )
                    }
                }
            })

            Text(
                text = stringResource(R.string.surname),
                fontSize = 16.sp,
                fontFamily = medium,
                modifier = modifier
            )

            Spacer(modifier = modifier.size(8.dp))

            TextField(modifier = modifier.fillMaxWidth(), value = surname, onValueChange = {
                surname = it
            }, shape = ShapeDefaults.Medium, colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ), singleLine = true, trailingIcon = {
                if (surname.isNotEmpty()){
                    IconButton(
                        onClick = { surname = "" }, modifier = modifier.wrapContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = modifier.wrapContentSize()
                        )
                    }
                }
            })
        }

        Button(
            onClick = {
                onChangeUserInfoButtonClick.invoke(name,surname)
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(
                text = stringResource(R.string.send_info),
                fontSize = 14.sp,
                fontFamily = medium,
                color = Color.White
            )
        }
    }
}