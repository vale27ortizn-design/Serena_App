package com.serena.ui.screens

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serena.R
import com.serena.ui.chat.ChatViewModel
import com.serena.ui.model.ChatMessage
import com.serena.ui.theme.SerenaAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: ChatViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Baja automáticamente al último mensaje (o al indicador "escribiendo")
    val itemCount = state.messages.size + if (state.isLoading) 1 else 0
    LaunchedEffect(itemCount) {
        if (itemCount > 0) listState.animateScrollToItem(itemCount - 1)
    }

    Scaffold(
        modifier = modifier.imePadding(), // el teclado empuja el input hacia arriba
        topBar = { ChatTopBar(onBackClick = onBackClick) },
        bottomBar = {
            ChatInputArea(
                input = state.input,
                isLoading = state.isLoading,
                error = state.error,
                showSuggestions = state.messages.size == 1,
                onInputChange = viewModel::onInputChange,
                onSend = { viewModel.sendMessage() },
                onSuggestionClick = { viewModel.sendMessage(it) },
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
                if (message.isFromUser) UserMessageBubble(message) else BotMessageBubble(message)
            }
            if (state.isLoading) {
                item { TypingIndicator() }
            }
        }
    }
}

// ---------------- SUBCOMPONENTES ----------------

@Composable
private fun ChatTopBar(onBackClick: () -> Unit) {
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

            // Menú
            Icon(
                painter = painterResource(id = R.drawable.icon_mas),
                contentDescription = "Opciones",
                tint = colorResource(R.color.serena_text_gray),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun formatTime(timestamp: Long): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))

@Composable
private fun BotMessageBubble(message: ChatMessage) {
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
            Text(
                text = formatTime(message.timestamp),
                fontSize = 10.sp,
                color = colorResource(R.color.serena_text_gray)
            )
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
        ChatScreen()
    }
}