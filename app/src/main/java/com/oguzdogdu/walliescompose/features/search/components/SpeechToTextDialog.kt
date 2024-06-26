package com.oguzdogdu.walliescompose.features.search.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
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
import com.oguzdogdu.walliescompose.util.VoiceSteps
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

    LaunchedEffect(searchState.speechDialogState) {
        if (searchState.speechDialogState) {
            speechToText.setVoiceSteps()
        } else {
            speechToText.stopListening()
        }
    }

    LaunchedEffect(key1 = speechToTextState.isSpeaking, key2 = speechToTextState.spokenText) {
        if (!speechToTextState.isSpeaking && speechToTextState.spokenText?.isNotEmpty() == true) {
            delay(1000)
            spokenText.invoke(speechToTextState.spokenText.orEmpty())
            onDismiss.invoke()
        }
    }

    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            dismissOnClickOutside = true, dismissOnBackPress = true, usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.75f)
                .heightIn(min = 240.dp, max = 360.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 240.dp, max = 360.dp)
                .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedContent(
                    targetState = speechToTextState.voiceSteps,
                    modifier = Modifier
                        .fillMaxWidth(),
                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec = tween(1000),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(1000),
                                    targetOffsetX = { fullWidth -> -fullWidth }
                                )
                    }, label = ""
                ) { targetState ->
                    when(targetState) {
                        VoiceSteps.FIRST_OPENED -> BeforeSpeechContent(onClick = {
                            coroutineScope.launch {
                                speechToText.startListening()
                            }
                        })
                        VoiceSteps.SPEAKING -> ProcessOfSpeechWave(speechToTextState = speechToTextState)
                        VoiceSteps.END_OF_SPEAK -> {
                            when {
                                speechToTextState.error.isNullOrEmpty().not() -> {
                                    ErrorSpeechContent(
                                        onClick = {
                                            coroutineScope.launch {
                                                speechToText.startListening()
                                            }
                                        })
                                }
                                speechToTextState.spokenText.isNullOrEmpty().not() -> LoadingState()
                            }
                        }
                        null -> {
                            return@AnimatedContent
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProcessOfSpeechWave(speechToTextState:SpeechToTextParserState) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val barCount by remember { mutableIntStateOf(7) }
    val maxBarHeight = 96.dp.toPx()
    val barWidth = 4.dp.toPx()
    val barSpacing = 8.dp.toPx()
    val maxAmplitude by remember { mutableFloatStateOf(10f) }
    val barColor: Color = colorResource(id = R.color.orange)
    val cornerRadius = 8.dp.toPx()
    val scaledAmplitude = (speechToTextState.volume?.coerceIn(0f, maxAmplitude) ?: 0f) / maxAmplitude

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(96.dp)
    ) {
        val canvasWidth = size.width
        val startX = (canvasWidth - (barCount * barWidth + (barCount - 1) * barSpacing)) / 2

        for (i in 0 until barCount) {
            val barHeight: Float = when {
                speechToTextState.isSpeaking && speechToTextState.volume!! < 4f -> maxBarHeight * 0.5f
                speechToTextState.isSpeaking -> {
                    val clampedAmplitude = scaledAmplitude.coerceIn(0f, 0.4f)
                    maxBarHeight * (0.5f + clampedAmplitude * sin(waveOffset + i) * 0.5f)
                }
                else -> {
                    maxBarHeight * 0.5f
                }
            }

            val top = (size.height - barHeight) / 2
            drawRoundRect(
                color = barColor,
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
fun BeforeSpeechContent(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.speech_dialog_title_text),
            fontSize = 16.sp,
            fontFamily = medium,
            color = Color.Unspecified,
            maxLines = 3,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.size(16.dp))
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
                modifier = Modifier.wrapContentSize(),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun ErrorSpeechContent(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.try_again),
            fontSize = 16.sp,
            fontFamily = medium,
            color = Color.Unspecified,
            maxLines = 3,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.size(16.dp))
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
                modifier = Modifier.wrapContentSize(),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}