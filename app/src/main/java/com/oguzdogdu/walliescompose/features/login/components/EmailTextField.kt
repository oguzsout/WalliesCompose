package com.oguzdogdu.walliescompose.features.login.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.util.FieldValidators


@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    onChangeEmail: (String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    val emailIsError by remember {
        derivedStateOf {
            email.isNotEmpty() && !FieldValidators.isValidEmail(email)
        }
    }
    val path = remember { Path() }
    val pathMeasure = remember { PathMeasure() }
    val progress = remember { Animatable(0f) }
    val borderColor = remember { mutableStateOf<EmailTextFieldValidationState>(EmailTextFieldValidationState.OTHER) }
    val textColor = remember(borderColor.value) { borderColor.value.color }

    LaunchedEffect(emailIsError) {
        borderColor.value = when {
            email.isNotEmpty() and emailIsError -> EmailTextFieldValidationState.INVALID
            email.isNotEmpty() and !emailIsError -> EmailTextFieldValidationState.VALID
            else -> EmailTextFieldValidationState.OTHER
        }

        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(2000, easing = EaseInOutCubic)
        )
    }

    LaunchedEffect(email) {
        if(email.isNotEmpty()) {
            onChangeEmail.invoke(email)
        }
    }

    TextField(
        value = email,
        onValueChange = { email = it },
        label = {
            Text(
                text = stringResource(id = R.string.email),
                fontFamily = medium,
                color = textColor
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                    .wrapContentSize(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AnimatedContent(targetState = borderColor.value, label = "", transitionSpec = {
                    slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End, initialOffset = {
                        it
                    }, animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    )).togetherWith(
                        ExitTransition.None
                    )
                }, modifier = Modifier.size(24.dp)) { state ->
                    when(state) {
                        EmailTextFieldValidationState.VALID -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_completed),
                                contentDescription = "",
                                tint = EmailTextFieldValidationState.VALID.color,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        EmailTextFieldValidationState.INVALID -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = "",
                                tint = EmailTextFieldValidationState.INVALID.color,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        EmailTextFieldValidationState.OTHER -> {}
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
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
}

enum class EmailTextFieldValidationState(val color: Color) {
    VALID( Color.Green.copy(green = 0.7f)),
    INVALID(Color.Red),
    OTHER(Color.Unspecified)
}
