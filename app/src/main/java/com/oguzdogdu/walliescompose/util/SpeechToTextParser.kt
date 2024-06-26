package com.oguzdogdu.walliescompose.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

@Stable
class SpeechToTextParser(
    private val context: Context
) : RecognitionListener {

    private var _stateOfSpeech = MutableStateFlow(SpeechToTextParserState())
    val stateOfSpeech : StateFlow<SpeechToTextParserState> get() = _stateOfSpeech

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    override fun onReadyForSpeech(params: Bundle?) {
        _stateOfSpeech.update {
            it.copy(
                error = null,
                voiceSteps = VoiceSteps.SPEAKING
            )
        }
    }

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(rmsdB: Float) {
        _stateOfSpeech.update {
            it.copy(
                volume = rmsdB
            )
        }
    }

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        _stateOfSpeech.update {
            it.copy(
                isSpeaking = false,
                isStart = false,
                voiceSteps = VoiceSteps.END_OF_SPEAK
            )
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _stateOfSpeech.update {
            it.copy(
                error = "Error: $error",
                isSpeaking = false,
                isStart = false,
                voiceSteps = VoiceSteps.END_OF_SPEAK
            )
        }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { text ->
                _stateOfSpeech.update {
                    it.copy(
                        spokenText = text,
                        isSpeaking = false,
                        isStart = false,
                        voiceSteps = VoiceSteps.END_OF_SPEAK
                    )
                }
            }
    }

    override fun onPartialResults(partialResults: Bundle?) {}

    override fun onEvent(eventType: Int, params: Bundle?) {}
    fun startListening() {
        _stateOfSpeech.update {
            it.copy(
                isSpeaking = true,
                isStart = true,
                voiceSteps = VoiceSteps.SPEAKING
            )
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().language)
        }
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _stateOfSpeech.update {
                it.copy(
                    error = "Speech recognition is not available"
                )
            }
        }
    }
    fun stopListening() {
        recognizer.stopListening()
        _stateOfSpeech.update {
            it.copy(
                isSpeaking = false,
                error = null,
                spokenText = null,
                isStart = false,
                voiceSteps = VoiceSteps.END_OF_SPEAK
            )
        }

    }
    fun setVoiceSteps() {
        _stateOfSpeech.update {
            val currentState = it
            val newVoiceStep = when {
                !currentState.isStart -> VoiceSteps.FIRST_OPENED
                currentState.isSpeaking -> VoiceSteps.SPEAKING
                else -> VoiceSteps.END_OF_SPEAK
            }
            currentState.copy(voiceSteps = newVoiceStep)
        }
    }

}
@Stable
data class SpeechToTextParserState(
    val isStart:Boolean = false,
    val isSpeaking: Boolean = false,
    val spokenText: String? = "",
    val error: String? = null,
    val volume: Float? = 0f,
    val voiceSteps: VoiceSteps? = null
)
enum class VoiceSteps {
    FIRST_OPENED,
    SPEAKING,
    END_OF_SPEAK
}