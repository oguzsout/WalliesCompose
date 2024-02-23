package com.oguzdogdu.walliescompose.features.setwallpaper

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetWallpaperImageScreenRoute(
    modifier: Modifier,
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSetLockButtonClick: () -> Unit,
    onSetHomeButtonClick: () -> Unit,
    onSetHomeAndLockButtonClick: () -> Unit
) {
    var openBottomSheet by remember { mutableStateOf(isOpen) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isOpen) {
        openBottomSheet = isOpen
    }
    if (openBottomSheet) {

        ModalBottomSheet(
            modifier = modifier,
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(0, 0, 0, 0),
            onDismissRequest = {
                scope.launch { bottomSheetState.hide() }
                    .invokeOnCompletion { openBottomSheet = false }
                onDismiss.invoke()
            },
            dragHandle = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Icon(
                        painter = painterResource(id = R.drawable.wallpaper),
                        contentDescription = ""
                    )
                    Spacer(modifier = modifier.size(8.dp))
                    Text(
                        text = stringResource(id = R.string.set_wallpaper_desc_text),
                        fontSize = 14.sp,
                        fontFamily = medium,
                        color = Color.Unspecified,
                        maxLines = 3,
                        textAlign = TextAlign.Center,
                        lineHeight = TextUnit(24f, TextUnitType.Sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                }
            }
        ) {
            Column(
                modifier
                    .padding(8.dp)
                    .wrapContentHeight(),
            ) {
                BottomSheetContent(modifier.navigationBarsPadding(),
                    onSetLockButtonClick = {
                    onSetLockButtonClick.invoke()
                },
                    onSetHomeButtonClick = {
                    onSetHomeButtonClick.invoke()
                },
                    onSetHomeAndLockButtonClick = {
                    onSetHomeAndLockButtonClick.invoke()
                })
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BottomSheetContent(
    modifier: Modifier,
    onSetLockButtonClick: () -> Unit,
    onSetHomeButtonClick: () -> Unit,
    onSetHomeAndLockButtonClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier
            .navigationBarsPadding()

    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    onSetLockButtonClick.invoke()
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.set_to_lock_screen_text),
                    fontSize = 14.sp,
                    fontFamily = medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = modifier.size(8.dp))
            Button(
                onClick = {
                    onSetHomeButtonClick.invoke()
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.set_to_home_screen_text),
                    fontSize = 14.sp,
                    fontFamily = medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = modifier.size(8.dp))
            Button(
                onClick = {
                    onSetHomeAndLockButtonClick.invoke()
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.set_to_home_amp_lockscreens_text),
                    fontSize = 14.sp,
                    fontFamily = medium,
                    color = Color.Black
                )
            }
        }
    }
}