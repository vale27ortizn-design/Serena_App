package com.serena.ui.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serena.R
import com.serena.data.preferences.SessionManager
import com.serena.ui.chat.ChatViewModel
import com.serena.ui.chat.ChatViewModelFactory
import com.serena.ui.chat.TtsManager
import com.serena.ui.model.ChatMessage
import com.serena.ui.model.Conversation
import com.serena.ui.theme.SerenaAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val VOICE_UNAVAILABLE =
    "No hay una voz en español instalada. Actívala en Ajustes → Salida de texto a voz."

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    // Se obtiene del usuario con sesión iniciada: id + correo
    userId: String = SessionManager(LocalContext.current).let {
        "${it.getUserId()}_${it.getUserEmail()}"
    },
    viewModel: ChatViewModel = viewModel(
        key = "chat_$userId", // un ViewModel distinto por usuario
        factory = ChatViewModelFactory(
            LocalContext.current.applicationContext as Application,
            userId
        )
    )
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    var showHistory by remember { mutableStateOf(false) }
    var conversationToDelete by remember { mutableStateOf<String?>(null) }

    // ---------------- VOZ (solo al tocar "Escuchar") ----------------
    val tts = remember { TtsManager(context) }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }

    // Al cambiar de conversación, se calla
    LaunchedEffect(state.currentId) { tts.stop() }

    fun onSpeakClick(message: ChatMessage) {
        when {
            tts.unavailable -> Toast.makeText(context, VOICE_UNAVAILABLE, Toast.LENGTH_LONG).show()
            tts.speakingId == message.id -> tts.stop()
            else -> if (!tts.speak(message.id, message.text)) {
                Toast.makeText(context, VOICE_UNAVAILABLE, Toast.LENGTH_LONG).show()
            }
        }
    }

    // Baja automáticamente al último mensaje (o al indicador "escribiendo")
    val itemCount = state.messages.size + if (state.isLoading) 1 else 0
    LaunchedEffect(state.currentId, itemCount) {
        if (itemCount > 0) listState.animateScrollToItem(itemCount - 1)
    }

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            ChatTopBar(
                onBackClick = onBackClick,
                onHistoryClick = { showHistory = true },
                onNewChatClick = viewModel::newChat
            )
        },
        bottomBar = {
            ChatInputArea(
                input = state.input,
                isLoading = state.isLoading,
                error = state.error,
                showSuggestions = state.messages.size == 1,
                onInputChange = viewModel::onInputChange,
                onSend = {
                    tts.stop()
                    viewModel.sendMessage()
                },
                onSuggestionClick = {
                    tts.stop()
                    viewModel.sendMessage(it)
                },
                onDismissError = viewModel::dismissError
            )
        },
        containerColor = colorResource(R.color.white)
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.messages, key = { it.id }) { message ->
                if (message.isFromUser) {
                    UserMessageBubble(message)
                } else {
                    BotMessageBubble(
                        message = message,
                        isSpeaking = tts.speakingId == message.id,
                        voiceUnavailable = tts.unavailable,
                        onSpeakClick = { onSpeakClick(message) }
                    )
                }
            }
            if (state.isLoading) {
                item { TypingIndicator() }
            }
        }
    }

    if (showHistory) {
        HistorySheet(
            conversations = state.conversations,
            currentId = state.currentId,
            onSelect = {
                viewModel.selectConversation(it)
                showHistory = false
            },
            onNewChat = {
                viewModel.newChat()
                showHistory = false
            },
            onDelete = { conversationToDelete = it },
            onDismiss = { showHistory = false }
        )
    }

    // --- CUADRO DE AVISO: ELIMINAR CONVERSACIÓN ---
    conversationToDelete?.let { id ->
        DeleteConversationDialog(
            onConfirm = {
                viewModel.deleteConversation(id)
                conversationToDelete = null
            },
            onDismiss = { conversationToDelete = null }
        )
    }
}

// ---------------- SUBCOMPONENTES ----------------

@Composable
private fun DeleteConversationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Círculo con el ícono
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.serena_green_dark).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_basura),
                        contentDescription = "Eliminar",
                        tint = colorResource(id = R.color.serena_green_dark),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¿Eliminar conversación?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.serena_text_dark),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Esta conversación se borrará de forma permanente y no podrás recuperarla.",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.serena_text_gray),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, colorResource(id = R.color.serena_green_dark)),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Cancelar",
                            color = colorResource(id = R.color.serena_green_dark),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.serena_green_dark)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Eliminar",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatTopBar(
    onBackClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onNewChatClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(R.color.white),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_flechaizquierda),
                contentDescription = "Volver",
                tint = colorResource(id = R.color.serena_text_dark),
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { onBackClick() }
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Avatar Serena
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.serena_card_diario_icon_bg)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo_simple),
                    contentDescription = "Serena Avatar",
                    tint = colorResource(R.color.serena_green_dark),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            // Nombre y estado
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Serena",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.serena_text_dark)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.icon_hoja),
                        contentDescription = null,
                        tint = colorResource(R.color.serena_green_dark),
                        modifier = Modifier.size(12.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.serena_green_primary))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "En línea • Siempre aquí para ti",
                        fontSize = 11.sp,
                        color = colorResource(R.color.serena_text_gray)
                    )
                }
            }

            // Menú de tres puntos
            var menuExpanded by remember { mutableStateOf(false) }

            Box {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu3puntos),
                    contentDescription = "Opciones",
                    tint = colorResource(R.color.serena_text_gray),
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable { menuExpanded = true }
                )

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.background(colorResource(R.color.white))
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Historial",
                                fontSize = 14.sp,
                                color = colorResource(R.color.serena_text_dark)
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onHistoryClick()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Nuevo chat",
                                fontSize = 14.sp,
                                color = colorResource(R.color.serena_text_dark)
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onNewChatClick()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistorySheet(
    conversations: List<Conversation>,
    currentId: String,
    onSelect: (String) -> Unit,
    onNewChat: () -> Unit,
    onDelete: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Solo se muestran chats con al menos un mensaje del usuario
    val items = conversations
        .filter { it.messages.size > 1 }
        .sortedByDescending { it.updatedAt }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colorResource(R.color.white)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Tus conversaciones",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.serena_text_dark)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = colorResource(R.color.serena_green_primary),
                modifier = Modifier.clickable(onClick = onNewChat)
            ) {
                Text(
                    text = "+  Nuevo chat",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.white),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (items.isEmpty()) {
                Text(
                    text = "Aún no tienes conversaciones guardadas.",
                    fontSize = 13.sp,
                    color = colorResource(R.color.serena_text_gray)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items, key = { it.id }) { conversation ->
                        ConversationRow(
                            conversation = conversation,
                            selected = conversation.id == currentId,
                            onClick = { onSelect(conversation.id) },
                            onDelete = { onDelete(conversation.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationRow(
    conversation: Conversation,
    selected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val date = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
        .format(Date(conversation.updatedAt))

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) colorResource(R.color.serena_green_light)
        else colorResource(R.color.serena_bg_input),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.serena_text_dark),
                    maxLines = 1
                )
                Text(
                    text = date,
                    fontSize = 11.sp,
                    color = colorResource(R.color.serena_text_gray)
                )
            }
            TextButton(onClick = onDelete) {
                Text(
                    text = "Eliminar",
                    fontSize = 12.sp,
                    color = colorResource(R.color.serena_text_gray)
                )
            }
        }
    }
}

private fun formatTime(timestamp: Long): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))

@Composable
private fun SpeakButton(
    isSpeaking: Boolean,
    voiceUnavailable: Boolean,
    onClick: () -> Unit
) {
    val iconRes = when {
        voiceUnavailable -> R.drawable.icon_sinvolumen
        isSpeaking -> R.drawable.icon_detener
        else -> R.drawable.icon_volumen
    }
    val label = when {
        voiceUnavailable -> "Sin voz"
        isSpeaking -> "Detener"
        else -> "Escuchar"
    }
    val background = if (isSpeaking) colorResource(R.color.serena_green_primary)
    else colorResource(R.color.serena_bg_input)
    val content = if (isSpeaking) colorResource(R.color.white)
    else colorResource(R.color.serena_text_dark)

    Surface(
        shape = RoundedCornerShape(50),
        color = background,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = content,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = content
            )
        }
    }
}

@Composable
private fun BotMessageBubble(
    message: ChatMessage,
    isSpeaking: Boolean,
    voiceUnavailable: Boolean,
    onSpeakClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_salud),
            contentDescription = null,
            tint = colorResource(R.color.serena_green_dark),
            modifier = Modifier
                .size(16.dp)
                .padding(bottom = 20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.Start) {
            Surface(
                color = colorResource(R.color.serena_green_light),
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = colorResource(R.color.serena_text_dark),
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = formatTime(message.timestamp),
                    fontSize = 10.sp,
                    color = colorResource(R.color.serena_text_gray)
                )
                Spacer(modifier = Modifier.width(10.dp))
                SpeakButton(
                    isSpeaking = isSpeaking,
                    voiceUnavailable = voiceUnavailable,
                    onClick = onSpeakClick
                )
            }
        }
    }
}

@Composable
private fun UserMessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth(0.85f)) {
            Surface(
                color = colorResource(R.color.serena_card_chat_bg),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = colorResource(R.color.serena_text_dark),
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatTime(message.timestamp),
                fontSize = 10.sp,
                color = colorResource(R.color.serena_text_gray)
            )
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(24.dp))
        Surface(
            color = colorResource(R.color.serena_green_light),
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
        ) {
            Text(
                text = "Serena está escribiendo…",
                fontSize = 12.sp,
                color = colorResource(R.color.serena_text_gray),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun SuggestionChip(icon: String, text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = colorResource(R.color.serena_bg_input),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 13.sp, color = colorResource(R.color.serena_text_dark))
        }
    }
}

@Composable
private fun ChatInputArea(
    input: String,
    isLoading: Boolean,
    error: String?,
    showSuggestions: Boolean,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onSuggestionClick: (String) -> Unit,
    onDismissError: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(R.color.white),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {

            if (error != null) {
                Text(
                    text = error,
                    fontSize = 12.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable(onClick = onDismissError)
                )
            }

            if (showSuggestions) {
                Text(
                    text = "Sugerencias para responder:",
                    fontSize = 11.sp,
                    color = colorResource(R.color.serena_text_gray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SuggestionChip("🫁", "Ejercicio de respiración") {
                        onSuggestionClick("Quiero hacer un ejercicio de respiración")
                    }
                    SuggestionChip("📝", "Quiero desahogarme") {
                        onSuggestionClick("Quiero desahogarme")
                    }
                    SuggestionChip("🌿", "Necesito calmarme") {
                        onSuggestionClick("Necesito calmarme, ¿qué puedo hacer?")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = input,
                    onValueChange = onInputChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Escribe lo que sientes...",
                            fontSize = 14.sp,
                            color = colorResource(R.color.serena_text_gray)
                        )
                    },
                    maxLines = 4,
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.serena_text_dark),
                        unfocusedTextColor = colorResource(R.color.serena_text_dark),
                        focusedContainerColor = colorResource(R.color.serena_bg_input),
                        unfocusedContainerColor = colorResource(R.color.serena_bg_input),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))

                val canSend = input.isNotBlank() && !isLoading
                IconButton(
                    onClick = onSend,
                    enabled = canSend,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (canSend) colorResource(R.color.serena_green_primary)
                            else colorResource(R.color.serena_icon_gray)
                        )
                ) {
                    Icon(
                        // La flecha izquierda girada 180° hace de "enviar"
                        painter = painterResource(id = R.drawable.icon_flechaizquierda),
                        contentDescription = "Enviar",
                        tint = colorResource(R.color.white),
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(180f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    SerenaAppTheme {
        ChatScreen(userId = "preview")
    }
}