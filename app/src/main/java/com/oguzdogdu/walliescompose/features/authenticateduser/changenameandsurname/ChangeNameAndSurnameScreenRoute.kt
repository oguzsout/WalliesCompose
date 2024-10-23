package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.appstate.AnimatableSnackbar
import com.oguzdogdu.walliescompose.features.appstate.SnackbarModel
import com.oguzdogdu.walliescompose.ui.theme.medium

typealias onPersonalInfoEditScreenEvent = (EditPersonalInfoEvent) -> Unit

@Composable
fun ChangeNameAndSurnameScreenRoute(
    viewModel: EditPersonalInfoViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialState by viewModel.initialData.collectAsStateWithLifecycle()

    var snackbarModel by remember { mutableStateOf<SnackbarModel?>(null) }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when(it) {
                is EditPersonalInfoEffect.ShowSnackbar -> snackbarModel = it.snackbarModel
            }
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize()
        ,topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navigateBack.invoke() }, modifier = Modifier.wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.wrapContentSize()
                )
            }

            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.edit_user_info_title),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ChangeEmailScreenContent(
                initialState = initialState,
                snackbarModel = snackbarModel,
                onPersonalInfoEditScreenEvent = { event ->
                    viewModel.sendEvent(event)
                },
                onDismissSnackbar = { snackbarModel = null }
            )
        }
    }
}

@Composable
fun ChangeEmailScreenContent(
    initialState: EditPersonalInfoState,
    snackbarModel: SnackbarModel?,
    onPersonalInfoEditScreenEvent: onPersonalInfoEditScreenEvent,
    onDismissSnackbar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var isNameFocused by remember { mutableStateOf(false) }
    var isSurnameFocused by remember { mutableStateOf(false) }
    val nameInteractionSource = remember { MutableInteractionSource() }
    val surnameInteractionSource = remember { MutableInteractionSource() }
    val snackbar by rememberUpdatedState(snackbarModel)

    LaunchedEffect(initialState) {
        if (initialState.name?.isNotEmpty() == true or (initialState.surname?.isNotEmpty() == true)) {
            name = initialState.name ?: ""
            surname = initialState.surname ?: ""
        }
    }

    LaunchedEffect(nameInteractionSource) {
        nameInteractionSource.interactions.collect { interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> isNameFocused = true
                is FocusInteraction.Unfocus -> isNameFocused = false
            }
        }
    }

    LaunchedEffect(surnameInteractionSource) {
        surnameInteractionSource.interactions.collect { interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> isSurnameFocused = true
                is FocusInteraction.Unfocus -> isSurnameFocused = false
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(R.string.name),
                fontSize = 16.sp,
                fontFamily = medium
            )

            Spacer(modifier = Modifier.size(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                interactionSource = nameInteractionSource,
                shape = ShapeDefaults.Medium,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
                trailingIcon = {
                    if (isNameFocused && name.isNotEmpty()) {
                        IconButton(
                            onClick = { name = "" },
                            modifier = modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = modifier.wrapContentSize()
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = stringResource(R.string.surname),
                fontSize = 16.sp,
                fontFamily = medium,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.size(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = surname,
                onValueChange = { surname = it },
                interactionSource = surnameInteractionSource,
                shape = ShapeDefaults.Medium,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
                trailingIcon = {
                    if (isSurnameFocused && surname.isNotEmpty()) {
                        IconButton(
                            onClick = { surname = "" },
                            modifier = modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = modifier.wrapContentSize()
                            )
                        }
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center
        ) {
            AnimatableSnackbar(snackbarModel = snackbar, onSnackbarDismiss = onDismissSnackbar)
            Button(
                onClick = {
                    onPersonalInfoEditScreenEvent.invoke(
                        EditPersonalInfoEvent.ChangedUserNameAndSurname(
                            name = name,
                            surname = surname
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
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
}



@Preview(showBackground = true)
@Composable
fun ChangeNameAndSurnamePreview() {
    ChangeEmailScreenContent(
        snackbarModel = null,
        initialState = EditPersonalInfoState("Muhammet", "Küdür"),
        onPersonalInfoEditScreenEvent = { },
        onDismissSnackbar = { }
    )
}