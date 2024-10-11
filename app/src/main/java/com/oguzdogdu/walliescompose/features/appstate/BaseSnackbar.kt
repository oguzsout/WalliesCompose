package com.oguzdogdu.walliescompose.features.appstate

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@Immutable
enum class MessageType {
    ERROR,
    SUCCESS
}

@Stable
data class SnackbarModel(
    val type: MessageType,
    @DrawableRes val drawableRes: Int?,
    val message: String? = null,
    val additionalMessage: String? = null,
    val duration: SnackbarDuration,
)

@Composable
fun CustomSnackbar(
    snackbarModel: SnackbarModel?,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val errorColor = colorResource(R.color.red)
    val successColor = colorResource(R.color.lush_green)

    var snackbarBackgroundColor by remember {
        mutableStateOf<Color?>(null)
    }

    LaunchedEffect(snackbarModel) {
        launch {
            snackbarBackgroundColor = when (snackbarModel?.type) {
                MessageType.ERROR -> errorColor
                MessageType.SUCCESS -> successColor
                null -> Color.Transparent
            }
        }
        snackbarModel?.let {
            snackbarHostState.showSnackbar(
                message = it.message.toString(),
                duration = it.duration
            )
        }
    }
    snackbarModel?.let { model ->
        SnackbarHost(snackbarHostState) {
            Snackbar(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                containerColor = snackbarBackgroundColor ?: Color.Transparent,
                shape = RoundedCornerShape(12.dp),
                actionOnNewLine = false,
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        if (model.drawableRes != null) {
                            Icon(
                                painter = painterResource(id = model.drawableRes),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = model.message.orEmpty(),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = medium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            )
        }
    }
}