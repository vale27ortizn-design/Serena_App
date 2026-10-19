package com.serena.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.serena.data.local.ConversationStorage
import com.serena.data.repository.ChatRepository
import com.serena.ui.model.ChatMessage
import com.serena.ui.model.Conversation
import com.serena.ui.model.withMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val conversations: List<Conversation> = emptyList(),
    val currentId: String = "",
    val input: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val messages: List<ChatMessage>
        get() = conversations.firstOrNull { it.id == currentId }?.messages ?: emptyList()
}

class ChatViewModel(
    application: Application,
    userId: String
) : AndroidViewModel(application) {

    private val repository = ChatRepository()
    private val storage = ConversationStorage(application, userId)

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var sendJob: Job? = null

    private fun initialState(): ChatUiState {
        val snapshot = storage.load()
        if (snapshot.conversations.isEmpty()) {
            val fresh = Conversation()
            return ChatUiState(conversations = listOf(fresh), currentId = fresh.id)
        }
        val current = snapshot.conversations.firstOrNull { it.id == snapshot.currentId }
            ?: snapshot.conversations.maxBy { it.updatedAt }
        return ChatUiState(conversations = snapshot.conversations, currentId = current.id)
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(input = text) }
    }

    fun sendMessage(text: String = _uiState.value.input) {
        val clean = text.trim()
        if (clean.isEmpty() || _uiState.value.isLoading) return

        val targetId = _uiState.value.currentId
        val userMessage = ChatMessage(text = clean, isFromUser = true)

        _uiState.update { s ->
            s.copy(
                conversations = s.conversations.map {
                    if (it.id == targetId) it.withMessage(userMessage) else it
                },
                input = "",
                isLoading = true,
                error = null
            )
        }
        persist()

        sendJob = viewModelScope.launch {
            val history = _uiState.value.conversations
                .firstOrNull { it.id == targetId }?.messages ?: return@launch

            repository.sendMessage(history)
                .onSuccess { reply ->
                    val botMessage = ChatMessage(text = reply, isFromUser = false)
                    _uiState.update { s ->
                        s.copy(
                            conversations = s.conversations.map {
                                if (it.id == targetId) it.withMessage(botMessage) else it
                            },
                            isLoading = false
                        )
                    }
                    persist()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun newChat() {
        cancelPending()
        // Si el chat actual solo tiene el saludo, ya es un chat "nuevo"
        if (_uiState.value.messages.size <= 1) return

        val fresh = Conversation()
        _uiState.update {
            it.copy(
                conversations = it.conversations + fresh,
                currentId = fresh.id,
                input = "",
                error = null
            )
        }
        persist()
    }

    fun selectConversation(id: String) {
        if (id == _uiState.value.currentId) return
        cancelPending()
        _uiState.update { s ->
            s.copy(
                // Se descartan chats vacíos (solo saludo) que quedaron atrás
                conversations = s.conversations.filter { it.messages.size > 1 || it.id == id },
                currentId = id,
                input = "",
                error = null
            )
        }
        persist()
    }

    fun deleteConversation(id: String) {
        val s = _uiState.value
        val remaining = s.conversations.filter { it.id != id }

        if (id == s.currentId) {
            cancelPending()
            val kept = remaining.filter { it.messages.size > 1 }
            val next = kept.maxByOrNull { it.updatedAt }
            _uiState.update {
                if (next != null) {
                    it.copy(conversations = kept, currentId = next.id, input = "", error = null)
                } else {
                    val fresh = Conversation()
                    it.copy(conversations = listOf(fresh), currentId = fresh.id, input = "", error = null)
                }
            }
        } else {
            _uiState.update { it.copy(conversations = remaining) }
        }
        persist()
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun cancelPending() {
        sendJob?.cancel()
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun persist() {
        val s = _uiState.value
        storage.save(s.conversations, s.currentId)
    }
}

class ChatViewModelFactory(
    private val application: Application,
    private val userId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ChatViewModel(application, userId) as T
}