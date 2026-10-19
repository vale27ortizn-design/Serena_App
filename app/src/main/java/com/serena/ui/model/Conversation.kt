package com.serena.ui.model

import java.util.UUID

const val DEFAULT_TITLE = "Nuevo chat"

fun greetingMessage() = ChatMessage(
    text = "Hola. Estoy aquí para acompañarte sin juzgarte. ¿Cómo ha estado tu día hoy?",
    isFromUser = false
)

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val title: String = DEFAULT_TITLE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val messages: List<ChatMessage> = listOf(greetingMessage())
)

fun Conversation.withMessage(message: ChatMessage): Conversation {
    val newTitle = if (title == DEFAULT_TITLE && message.isFromUser) {
        val t = message.text.trim().replace("\n", " ")
        if (t.length > 32) t.take(32).trimEnd() + "…" else t
    } else title

    return copy(
        messages = messages + message,
        title = newTitle,
        updatedAt = System.currentTimeMillis()
    )
}