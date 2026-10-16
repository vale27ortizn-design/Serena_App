package com.serena.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serena.data.repository.ChatRepository
import com.serena.ui.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            text = "Hola. Estoy aquí para acompañarte sin juzgarte. ¿Cómo ha estado tu día hoy?",
            isFromUser = false
        )
    ),
    val input: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputChange(text: String) {
        _uiState.update { it.copy(input = text) }
    }

    fun sendMessage(text: String = _uiState.value.input) {
        val clean = text.trim()
        if (clean.isEmpty() || _uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                messages = it.messages + ChatMessage(text = clean, isFromUser = true),
                input = "",
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            repository.sendMessage(_uiState.value.messages)
                .onSuccess { reply ->
                    _uiState.update {
                        it.copy(
                            messages = it.messages + ChatMessage(text = reply, isFromUser = false),
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}