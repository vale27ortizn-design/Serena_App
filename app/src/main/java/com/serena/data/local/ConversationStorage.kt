package com.serena.data.local

import android.content.Context
import com.serena.ui.model.ChatMessage
import com.serena.ui.model.Conversation
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.security.MessageDigest

class ConversationStorage(context: Context, userId: String) {

    class Snapshot(val conversations: List<Conversation>, val currentId: String?)

    // Un archivo por usuario (el hash evita caracteres raros del correo)
    private val file = File(
        context.applicationContext.filesDir,
        "conversations_${hash(userId)}.json"
    )

    private fun hash(value: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(value.toByteArray())
            .joinToString("") { "%02x".format(it) }
            .take(16)

    fun load(): Snapshot {
        if (!file.exists()) return Snapshot(emptyList(), null)
        return try {
            val root = JSONObject(file.readText())
            val array = root.getJSONArray("conversations")
            val list = List(array.length()) { array.getJSONObject(it).toConversation() }
            Snapshot(list, root.optString("currentId", "").ifEmpty { null })
        } catch (e: Exception) {
            Snapshot(emptyList(), null)
        }
    }

    fun save(conversations: List<Conversation>, currentId: String) {
        try {
            val array = JSONArray()
            conversations.forEach { array.put(it.toJson()) }
            val root = JSONObject()
                .put("currentId", currentId)
                .put("conversations", array)
            file.writeText(root.toString())
        } catch (e: Exception) {
            // Si falla el guardado, la app sigue funcionando
        }
    }

    private fun Conversation.toJson(): JSONObject {
        val msgs = JSONArray()
        messages.forEach {
            msgs.put(
                JSONObject()
                    .put("id", it.id)
                    .put("text", it.text)
                    .put("isFromUser", it.isFromUser)
                    .put("timestamp", it.timestamp)
            )
        }
        return JSONObject()
            .put("id", id)
            .put("title", title)
            .put("createdAt", createdAt)
            .put("updatedAt", updatedAt)
            .put("messages", msgs)
    }

    private fun JSONObject.toConversation(): Conversation {
        val arr = getJSONArray("messages")
        val msgs = List(arr.length()) { i ->
            val o = arr.getJSONObject(i)
            ChatMessage(
                id = o.getString("id"),
                text = o.getString("text"),
                isFromUser = o.getBoolean("isFromUser"),
                timestamp = o.getLong("timestamp")
            )
        }
        return Conversation(
            id = getString("id"),
            title = getString("title"),
            createdAt = getLong("createdAt"),
            updatedAt = getLong("updatedAt"),
            messages = msgs
        )
    }
}