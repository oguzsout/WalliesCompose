package com.oguzdogdu.walliescompose.features.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.home.LoadingState
import com.oguzdogdu.walliescompose.features.search.SearchState
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.util.SpeechToTextParser
import com.oguzdogdu.walliescompose.util.SpeechToTextParserState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun SpeechToTextDialog(
    searchState: SearchState, onDismiss: () -> Unit, spokenText: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val speechToText by remember {
        mutableStateOf(SpeechToTextParser(context = context))
    }
    val speechToTextState by speechToText.stateOfSpeech.collectAsState()
    var displayState by remember { mutableStateOf(speechToTextState) }

    LaunchedEffect(searchState.speechDialogState) {
        if (searchState.speechDialogState) {
            speechToText.startListening()
        } else {
            speechToText.stopListening()
        }
    }

    LaunchedEffect(key1 = displayState.isSpeaking, key2 = displayState.spokenText) {
        if (!displayState.isSpeaking && displayState.spokenText?.isNotEmpty() == true) {
            delay(1000)
            spokenText.invoke(displayState.spokenText.orEmpty())
            onDismiss.invoke()
        }
    }

    LaunchedEffect(speechToTextState) {
        delay(200)
        displayState = speechToTextState
    }

    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            dismissOnClickOutside = true, dismissOnBackPress = true, usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .animateContentSize(spring(stiffness = Spring.StiffnessMediumLow))
                .fillMaxWidth(fraction = 0.90f)
                .heightIn(min = 240.dp, max = 480.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .animateContentSize()
                    .heightIn(min = 240.dp, max = 480.dp),
            ) {
                if (displayState.spokenText?.isNotEmpty() == false) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        if (displayState.error?.isNotEmpty() == true) {
                            Text(
                                text = stringResource(R.string.try_again),
                                fontSize = 16.sp,
                                fontFamily = medium,
                                color = Color.Unspecified,
                                maxLines = 3,
                                textAlign = TextAlign.Start,
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.speech_dialog_title_text),
                                fontSize = 16.sp,
                                fontFamily = medium,
                                color = Color.Unspecified,
                                maxLines = 3,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (speechToTextState.spokenText?.isNotEmpty() == true && !speechToTextState.isSpeaking) {
                        LoadingState()
                    } else {
                        VoiceWaveform(
                            speechToTextState = speechToTextState
                        )
                    }

                    AnimatedVisibility(visible = speechToTextState.error?.isNotEmpty() == true) {
                        MicrophoneButton(onClick = {
                            coroutineScope.launch {
                                speechToText.startListening()
                            }
                        })
                    }
                }
            }

        }
    }
}

@Composable
fun VoiceWaveform(speechToTextState:SpeechToTextParserState) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val barCount by remember {
        mutableStateOf(5)
    }
    val maxBarHeight = 48.dp.toPx()
    val barWidth = 6.dp.toPx()
    val barSpacing = 8.dp.toPx()
    val maxAmplitude by remember {
        mutableStateOf(10f)
    }
    val cornerRadius = 8.dp.toPx()

    val scaledAmplitude = (speechToTextState.volume?.coerceIn(0f, maxAmplitude) ?: 0f) / maxAmplitude

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(96.dp)
        .padding(horizontal = 8.dp)
    ) {
        val canvasWidth = size.width
        val startX = (canvasWidth - (barCount * barWidth + (barCount - 1) * barSpacing)) / 2

        for (i in 0 until barCount) {
            val barHeight: Float = when {
                speechToTextState.isSpeaking && speechToTextState.volume!! < 4f -> maxBarHeight * 0.5f
                speechToTextState.isSpeaking -> {
                    maxBarHeight * (0.5f + 0.5f * scaledAmplitude * sin(waveOffset + i))
                }
                else -> {
                    maxBarHeight * 0.5f
                }
            }

            val top = (size.height - barHeight) / 2
            drawRoundRect(
                color = Color.Gray,
                topLeft = Offset(startX + i * (barWidth + barSpacing), top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}

@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}

@Composable
fun MicrophoneButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentSize(),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.round_mic_24),
            contentDescription = "Microphone",
            modifier = Modifier.wrapContentSize()
        )
    }
}
