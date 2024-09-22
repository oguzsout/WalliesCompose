package com.oguzdogdu.walliescompose.features.login.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.util.FieldValidators

@Composable
fun PasswordTextField(modifier: Modifier = Modifier, onChangePassword: (String) -> Unit) {

    var password by rememberSaveable { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }

    val icon: Painter = if (passwordVisibility) painterResource(id = R.drawable.eye_password_hide)
    else painterResource(id = R.drawable.eye_password_show)

    val keyboardController = LocalSoftwareKeyboardController.current

    val passwordIsError by remember {
        derivedStateOf {
            password.isNotEmpty() && !FieldValidators.isValidPasswordCheck(password)
        }
    }

    val passwordErrorMessage by remember {
        derivedStateOf {
            FieldValidators.isValidPasswordChecksStatus(password)
        }
    }


    val path = remember { Path() }
    val pathMeasure = remember { PathMeasure() }
    val progress = remember { Animatable(0f) }
    val borderColor = remember { mutableStateOf<PasswordTextFieldValidationState>(PasswordTextFieldValidationState.OTHER) }
    val textColor = remember(borderColor.value) { borderColor.value.color }

    LaunchedEffect(passwordIsError) {
        borderColor.value = when {
            password.isNotEmpty() && !passwordIsError -> PasswordTextFieldValidationState.VALID
            password.isNotEmpty() && passwordIsError -> PasswordTextFieldValidationState.INVALID
            else -> PasswordTextFieldValidationState.OTHER
        }

        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(2000, easing = EaseInOutCubic)
        )
    }

    LaunchedEffect(password) {
        if(password.isNotEmpty()) {
            onChangePassword.invoke(password)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = stringResource(id = R.string.password),
                    fontFamily = medium,
                    color = textColor
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }, modifier = Modifier.animateContentSize(tween(1000))) {
                        Icon(
                            painter = icon, contentDescription = "Visibility Icon"
                        )
                    }
                    if (password.isNotEmpty()) {
                        AnimatedContent(targetState = borderColor.value, label = "", transitionSpec = {
                            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End, initialOffset = {
                                it
                            }, animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessVeryLow
                            )
                            ).togetherWith(
                                ExitTransition.None
                            )
                        }, modifier = Modifier
                            .size(24.dp)
                            .animateContentSize(tween(1000))) { state ->
                            when(state) {
                                PasswordTextFieldValidationState.VALID -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_completed),
                                        contentDescription = "",
                                        tint = EmailTextFieldValidationState.VALID.color,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                PasswordTextFieldValidationState.INVALID -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_cancel),
                                        contentDescription = "",
                                        tint = EmailTextFieldValidationState.INVALID.color,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                PasswordTextFieldValidationState.OTHER -> {}
                            }
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .drawWithContent {
                    drawContent()
                    val strokeWidth = 2.dp.toPx()
                    val cornerRadius = 16.dp.toPx()

                    path.apply {
                        reset()
                        addRoundRect(
                            RoundRect(
                                rect = Rect(0f, 0f, size.width, size.height),
                                CornerRadius(cornerRadius, cornerRadius)
                            )
                        )
                    }

                    pathMeasure.setPath(path, false)
                    val pathLength = pathMeasure.length

                    val animatedPath = Path()
                    pathMeasure.getSegment(0f, progress.value * pathLength, animatedPath, true)

                    drawPath(
                        path = animatedPath,
                        color = borderColor.value.color,
                        style = Stroke(width = strokeWidth)
                    )
                }
        )
        AnimatedVisibility(
            visible = passwordIsError,
            enter = expandVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessVeryLow)),
            exit = shrinkVertically(spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessVeryLow)),
            modifier = Modifier
        ) {
            if (passwordErrorMessage != 0) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                ) {
                    AnimatedContent(
                        targetState = passwordErrorMessage,
                        label = "Password Error Message",
                        transitionSpec = {
                            if (initialState == targetState) {
                                scaleIn() togetherWith scaleOut()
                            } else {
                                slideInVertically(initialOffsetY = { -it }) togetherWith slideOutVertically(targetOffsetY = { it })
                            }
                        }
                    ) { currentErrorMessage ->
                        Text(
                            text = stringResource(id = currentErrorMessage),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp,
                            fontFamily = medium,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

enum class PasswordTextFieldValidationState(val color: Color) {
    VALID( Color.Green.copy(green = 0.7f)),
    INVALID(Color.Red),
    OTHER(Color.Unspecified)
}