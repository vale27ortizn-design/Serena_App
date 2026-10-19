package com.serena.ui.chat

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

class TtsManager(context: Context) {

    /** Id del mensaje que se está leyendo (null si no suena nada). */
    var speakingId by mutableStateOf<String?>(null)
        private set

    /** true si el teléfono no tiene una voz en español disponible. */
    var unavailable by mutableStateOf(false)
        private set

    private val main = Handler(Looper.getMainLooper())
    private var engine: TextToSpeech? = null
    private var ready = false
    private var pendingId: String? = null
    private var pendingText: String? = null

    init {
        engine = TextToSpeech(context.applicationContext) { status ->
            main.post { onInit(status) }
        }
    }

    private fun onInit(status: Int) {
        val tts = engine ?: return
        if (status != TextToSpeech.SUCCESS) {
            unavailable = true
            speakingId = null
            return
        }

        // Busca la mejor voz en español disponible en el teléfono
        val candidates = listOf(
            Locale("es", "PE"), Locale("es", "MX"), Locale("es", "US"),
            Locale("es", "ES"), Locale("es")
        )
        val chosen = candidates.firstOrNull {
            tts.isLanguageAvailable(it) >= TextToSpeech.LANG_AVAILABLE
        }
        if (chosen == null) {
            unavailable = true
            speakingId = null
            return
        }

        tts.language = chosen
        tts.setSpeechRate(0.9f) // un poco más lento = más calmado
        tts.setPitch(1.0f)
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // El estado ya se marcó en speak(); no se toca aquí para
                // evitar que un aviso tardío reactive el botón tras "Detener".
            }

            override fun onDone(utteranceId: String?) = finish(utteranceId)

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) = finish(utteranceId)

            override fun onStop(utteranceId: String?, interrupted: Boolean) = finish(utteranceId)
        })
        ready = true

        // Si tocaron "Escuchar" antes de que el motor estuviera listo
        val id = pendingId
        val text = pendingText
        pendingId = null
        pendingText = null
        if (id != null && text != null) speak(id, text)
    }

    private fun finish(utteranceId: String?) {
        main.post { if (speakingId == utteranceId) speakingId = null }
    }

    /** Devuelve false si no hay voz en español disponible. */
    fun speak(id: String, text: String): Boolean {
        if (unavailable) return false
        val clean = clean(text)
        if (clean.isBlank()) return true

        speakingId = id
        if (!ready) {
            pendingId = id
            pendingText = text
            return true
        }
        engine?.speak(clean, TextToSpeech.QUEUE_FLUSH, null, id)
        return true
    }

    fun stop() {
        pendingId = null
        pendingText = null
        speakingId = null
        engine?.stop()
    }

    fun shutdown() {
        pendingId = null
        pendingText = null
        engine?.stop()
        engine?.shutdown()
        engine = null
        speakingId = null
    }

    // Quita emojis y símbolos de formato para que no los lea raro
    private fun clean(text: String): String = text
        .replace(Regex("[\\p{So}\\p{Cs}\\uFE0F\\u200D]"), "")
        .replace(Regex("[*_#`~>]"), "")
        .replace(Regex("\\s+"), " ")
        .trim()
        .take(3900)
}