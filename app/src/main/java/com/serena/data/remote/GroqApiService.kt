package com.serena.data.remote

import android.util.Log
import com.serena.BuildConfig
import com.serena.ui.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class GroqException(val httpCode: Int) : Exception("Groq error $httpCode")

class GroqApiService(
    private val apiKey: String = BuildConfig.GROQ_API_KEY,
    private val model: String = MODEL
) {

    suspend fun generate(systemPrompt: String, history: List<ChatMessage>): String =
        withContext(Dispatchers.IO) {
            if (apiKey.isBlank()) throw GroqException(MISSING_KEY)

            val body = buildBody(systemPrompt, history).toString().toByteArray(Charsets.UTF_8)
            var lastCode = -3

            repeat(MAX_ATTEMPTS) { attempt ->
                val connection = (URL(URL_CHAT).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 10_000
                    readTimeout = 30_000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Authorization", "Bearer $apiKey")
                }

                try {
                    connection.outputStream.use { it.write(body) }

                    val code = connection.responseCode
                    if (code in 200..299) {
                        val response = connection.inputStream.bufferedReader().use { it.readText() }
                        return@withContext parseReply(response)
                    }

                    val err = connection.errorStream?.bufferedReader()?.use { it.readText() }
                    Log.e(TAG, "HTTP $code (intento ${attempt + 1}): $err")

                    lastCode = code
                    if (code != 500 && code != 503) throw GroqException(code)
                } finally {
                    connection.disconnect()
                }

                if (attempt < MAX_ATTEMPTS - 1) delay(1_000L * (attempt + 1))
            }
            throw GroqException(lastCode)
        }

    private fun buildBody(systemPrompt: String, history: List<ChatMessage>): JSONObject {
        val messages = JSONArray()
        messages.put(JSONObject().put("role", "system").put("content", systemPrompt))
        history.forEach {
            messages.put(
                JSONObject()
                    .put("role", if (it.isFromUser) "user" else "assistant")
                    .put("content", it.text)
            )
        }
        return JSONObject()
            .put("model", model)
            .put("messages", messages)
            .put("temperature", 0.7)
            .put("max_tokens", 400)
    }

    private fun parseReply(json: String): String {
        val text = JSONObject(json)
            .optJSONArray("choices")
            ?.optJSONObject(0)
            ?.optJSONObject("message")
            ?.optString("content")
            ?.trim()
            ?: throw GroqException(NO_CONTENT)

        if (text.isEmpty()) throw GroqException(NO_CONTENT)
        return text
    }

    companion object {
        // Verifica el nombre vigente en console.groq.com → Playground / Models
        const val MODEL = "qwen/qwen3.8-27b"
        const val NO_CONTENT = -1
        const val MISSING_KEY = -2
        private const val MAX_ATTEMPTS = 2
        private const val TAG = "Serena"
        private const val URL_CHAT = "https://api.groq.com/openai/v1/chat/completions"
    }
}