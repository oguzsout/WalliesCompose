package com.oguzdogdu.walliescompose.features.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleStartEffect
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium

@Immutable
enum class ValidateStateSnackbar {
    SUCCESS, ERROR
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun WalliesSnackbar(
    snackbarData: SnackbarData,
    validateStateSnackbar: ValidateStateSnackbar,
    modifier: Modifier = Modifier,
    shape: Shape = SnackbarDefaults.shape,
    containerColor: Color = SnackbarDefaults.color,
) {

    var containerBackgroundColor by remember {
        mutableStateOf(containerColor)
    }

    LifecycleStartEffect(key1 = validateStateSnackbar) {
        containerBackgroundColor = when (validateStateSnackbar) {
            ValidateStateSnackbar.SUCCESS -> {
                Color.Green
            }

            ValidateStateSnackbar.ERROR -> {
                Color.Red
            }
        }
        onStopOrDispose {

        }
    }

    Surface(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .size(64.dp)
            .padding(8.dp)
            .clip(shape),
        shape = shape,
        color = containerBackgroundColor,
    ) {
        BoxWithConstraints(
            modifier = modifier
                .navigationBarsPadding()
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { }, modifier = modifier
                        .size(32.dp)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        painter = when (validateStateSnackbar) {
                            ValidateStateSnackbar.SUCCESS -> painterResource(id = R.drawable.ic_completed)
                            ValidateStateSnackbar.ERROR -> painterResource(id = R.drawable.ic_error)
                        },
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = modifier.wrapContentSize()
                    )
                }

                Spacer(modifier = modifier.size(8.dp))

                Text(
                    modifier = modifier,
                    text = snackbarData.visuals.message,
                    color = Color.White,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    lineHeight = TextUnit(16f, TextUnitType.Sp),
                    fontFamily = medium,
                )
            }
        }
    }
}