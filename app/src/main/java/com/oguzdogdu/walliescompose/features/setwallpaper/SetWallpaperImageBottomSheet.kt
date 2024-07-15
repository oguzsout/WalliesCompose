package com.oguzdogdu.walliescompose.features.setwallpaper

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.detail.TypeOfSetWallpaper
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetWallpaperImageBottomSheet(
    imageForFilter: Bitmap?,
    wallpaperPlace: String?,
    isOpen: Boolean,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSetLockButtonClick: () -> Unit,
    onSetHomeButtonClick: () -> Unit,
    onSetHomeAndLockButtonClick: () -> Unit,
    adjustColorFilter:(ColorFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    var openBottomSheet by remember { mutableStateOf(isOpen) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isOpen) {
        openBottomSheet = isOpen
    }

    if (openBottomSheet) {

        ModalBottomSheet(
            modifier = modifier
                .wrapContentHeight(),
            sheetState = bottomSheetState,
            onDismissRequest = {
                scope.launch { bottomSheetState.hide() }
                    .invokeOnCompletion { openBottomSheet = false }
                onDismiss.invoke()
            },
            dragHandle = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                       AnimatedContent(targetState = bottomSheetState.currentValue,
                           transitionSpec = {
                               slideInVertically(
                                   tween(1000),
                                   initialOffsetY = {
                                       it
                                   }) togetherWith slideOutVertically(
                                   tween(
                                       1000
                                   ), targetOffsetY = {
                                       it
                                   }
                               )
                           },
                           label = ""
                       ) { sheetValue ->
                           Row(
                               modifier = Modifier
                                   .fillMaxWidth(),
                               verticalAlignment = Alignment.CenterVertically,
                               horizontalArrangement = Arrangement.Start
                           ) {
                           when(sheetValue) {
                               SheetValue.Expanded -> {
                                   IconButton(
                                       onClick = { onDismiss.invoke() },
                                       modifier = modifier
                                           .size(24.dp)
                                   ) {
                                       Icon(
                                           painter = painterResource(id = R.drawable.back),
                                           contentDescription = "",
                                           tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                       )
                                   }
                                   Spacer(modifier = Modifier.width(16.dp))
                                   Text(
                                       text = stringResource(id = R.string.set_wallpaper_text),
                                       color = MaterialTheme.colorScheme.onPrimaryContainer,
                                       fontFamily = medium,
                                       fontSize = 16.sp,
                                   )
                               }
                               SheetValue.PartiallyExpanded -> Text(
                                   text = stringResource(id = R.string.set_wallpaper_text),
                                   color = MaterialTheme.colorScheme.onPrimaryContainer,
                                   fontFamily = medium,
                                   fontSize = 16.sp,
                               )

                               SheetValue.Hidden -> {}
                           }
                       }
                    }

                    Spacer(modifier = modifier.size(16.dp))
                    Text(
                        text = stringResource(id = R.string.set_wallpaper_desc_text),
                        fontSize = 16.sp,
                        fontFamily = regular,
                        color = Color.Unspecified,
                        maxLines = 3,
                        textAlign = TextAlign.Start,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                }
            }) {
            BottomSheetContent(imageForFilter = imageForFilter,
                bottomSheetState = bottomSheetState,
                wallpaperPlace = wallpaperPlace,
                onSetLockButtonClick = {
                    onSetLockButtonClick.invoke()
                },
                onSetHomeButtonClick = {
                    onSetHomeButtonClick.invoke()
                },
                onSetHomeAndLockButtonClick = {
                    onSetHomeAndLockButtonClick.invoke()
                },
                adjustColorFilter = {
                    adjustColorFilter.invoke(it)
                }, isLoading = isLoading)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    imageForFilter: Bitmap?,
    bottomSheetState: SheetState,
    wallpaperPlace: String?,
    onSetLockButtonClick: () -> Unit,
    onSetHomeButtonClick: () -> Unit,
    onSetHomeAndLockButtonClick: () -> Unit,
    adjustColorFilter:(ColorFilter) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    var brightness by remember { mutableFloatStateOf(0f) }
    var contrast by remember { mutableFloatStateOf(1f) }
    var saturation by remember { mutableFloatStateOf(1f) }
    val colorMatrix: FloatArray = floatArrayOf(
        contrast * saturation,
        (1 - saturation) * 0.3086f * contrast,
        (1 - saturation) * 0.6094f * contrast,
        0f,
        brightness,
        (1 - saturation) * 0.3086f * contrast,
        contrast * saturation,
        (1 - saturation) * 0.082f * contrast,
        0f,
        brightness,
        (1 - saturation) * 0.6094f * contrast,
        (1 - saturation) * 0.082f * contrast,
        contrast * saturation,
        0f,
        brightness,
        0f,
        0f,
        0f,
        1f,
        0f
    )
    var cachedColorFilter by remember {
        mutableStateOf(colorMatrix)

    }
    LaunchedEffect(key1 = colorMatrix) {
        cachedColorFilter = colorMatrix
    }

    val brightnessInteractionSource = remember { MutableInteractionSource() }
    val contrastInteractionSource = remember { MutableInteractionSource() }
    val saturationInteractionSource = remember { MutableInteractionSource() }
    val anim by animateFloatAsState(
        targetValue = when (bottomSheetState.currentValue) {
            SheetValue.Hidden -> 90f
            SheetValue.PartiallyExpanded -> 45f
            SheetValue.Expanded -> 0f
        },
        animationSpec = tween(1000),
        label = ""
    )
    Column(
        modifier = Modifier
            .fillMaxHeight(0.87f)
            .padding(horizontal = 16.dp)
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        imageForFilter?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = "",
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp)),
                colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier
            .graphicsLayer {
                translationY = anim
            },
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(id = R.drawable.round_brightness_6_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Slider(
                    interactionSource = brightnessInteractionSource,
                    colors = SliderDefaults.colors(
                        inactiveTrackColor = MaterialTheme.colorScheme.tertiary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        thumbColor = MaterialTheme.colorScheme.inversePrimary),
                    thumb = {
                        SliderDefaults.Thumb(
                            interactionSource = brightnessInteractionSource,
                            thumbSize = DpSize(4.dp,24.dp)
                        )
                    },
                    value = brightness,
                    onValueChange = { brightness = it },
                    valueRange = 0f..100f,
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(id = R.drawable.round_contrast_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Slider(
                    interactionSource = contrastInteractionSource,
                    colors = SliderDefaults.colors(
                        inactiveTrackColor = MaterialTheme.colorScheme.tertiary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        thumbColor = MaterialTheme.colorScheme.inversePrimary),
                    thumb = {
                        SliderDefaults.Thumb(
                            interactionSource = contrastInteractionSource,
                            thumbSize = DpSize(4.dp,24.dp)
                        )
                    },
                    value = contrast,
                    onValueChange = { contrast = it },
                    valueRange = 0f..5f
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(id = R.drawable.round_invert_colors_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = modifier.size(8.dp))

                Slider(
                    interactionSource = saturationInteractionSource,
                    colors = SliderDefaults.colors(
                        inactiveTrackColor = MaterialTheme.colorScheme.tertiary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        thumbColor = MaterialTheme.colorScheme.inversePrimary),
                    thumb = {
                        SliderDefaults.Thumb(
                            interactionSource = saturationInteractionSource,
                            thumbSize = DpSize(4.dp,24.dp)
                        )
                    },
                    value = saturation,
                    onValueChange = { saturation = it },
                    valueRange = 0f..2f,
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        onSetLockButtonClick.invoke()
                        adjustColorFilter.invoke(ColorFilter.colorMatrix(ColorMatrix(cachedColorFilter)))
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    contentPadding = PaddingValues(12.dp),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                ) {
                    when {
                        isLoading && wallpaperPlace == TypeOfSetWallpaper.LOCK.name -> {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        else -> {
                            Text(
                                text = stringResource(id = R.string.set_to_lock_screen_text),
                                fontSize = 12.sp,
                                fontFamily = medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedButton(
                    onClick = {
                        onSetHomeButtonClick.invoke()
                        adjustColorFilter.invoke(ColorFilter.colorMatrix(ColorMatrix(cachedColorFilter)))
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    contentPadding = PaddingValues(12.dp),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                ) {
                    when {
                        isLoading && wallpaperPlace == TypeOfSetWallpaper.HOME.name -> {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        else -> {
                            Text(
                                text = stringResource(id = R.string.set_to_home_screen_text),
                                fontSize = 12.sp,
                                fontFamily = medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedButton(
                    onClick = {
                        onSetHomeAndLockButtonClick.invoke()
                        adjustColorFilter.invoke(ColorFilter.colorMatrix(ColorMatrix(cachedColorFilter)))
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    contentPadding = PaddingValues(12.dp),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                ) {
                    when {
                        isLoading && wallpaperPlace == TypeOfSetWallpaper.HOME_AND_LOCK.name -> {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        else -> {
                            Text(
                                text = stringResource(id = R.string.set_to_home_amp_lockscreens_text),
                                fontSize = 12.sp,
                                fontFamily = medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}