package com.serena.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.ui.theme.SerenaAppTheme

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            ChatTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            ChatInputArea()
        },
        containerColor = colorResource(R.color.white)
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Píldora de fecha
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = colorResource(R.color.serena_badge_bg)
                    ) {
                        Text(
                            text = "Hoy, 18:42",
                            fontSize = 11.sp,
                            color = colorResource(R.color.serena_text_gray),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Mensaje del Bot 1
            item {
                BotMessageBubble(
                    text = "Hola. Estoy aquí para acompañarte sin juzgarte. ¿Cómo ha estado tu día hoy?",
                    time = "18:42"
                )
            }

            // Mensaje del Usuario
            item {
                UserMessageBubble(
                    text = "Me siento un poco ansiosa por una presentación que tengo mañana en el trabajo...",
                    time = "18:43"
                )
            }

            // Mensaje del Bot 2
            item {
                BotMessageBubble(
                    text = "Es muy normal sentir esa inquietud ante situaciones importantes. ¿Te gustaría que hagamos un ejercicio breve de respiración de 2 minutos, o prefieres desahogarte y contarme más detalles?",
                    time = "18:44"
                )
            }

            // Tarjeta de Acción (Momento de Pausa)
            item {
                PauseActionCard()
            }

            // Sugerencias para responder
            item {
                SuggestionsSection()
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// ---------------- SUBCOMPONENTES ----------------

@Composable
private fun ChatTopBar(onBackClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(R.color.white),
        shadowElevation = 2.dp // Sutil sombra para separar del chat
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
                    painter = painterResource(id = R.drawable.logo_serena),
                    contentDescription = "Serena Avatar",
                    tint = colorResource(R.color.serena_green_dark),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            // Nombre y Estado
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

            // Menú 3 puntos
            Icon(
                painter = painterResource(id = R.drawable.icon_mas), // Cambiar por icono de 3 puntos (more_vert) si lo tienes
                contentDescription = "Opciones",
                tint = colorResource(R.color.serena_text_gray),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun BotMessageBubble(text: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_salud), // Icono chiquito al lado
            contentDescription = null,
            tint = colorResource(R.color.serena_green_dark),
            modifier = Modifier.size(16.dp).padding(bottom = 20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.Start) {
            Surface(
                color = colorResource(R.color.serena_green_light),
                // Borde redondeado excepto en la esquina superior izquierda
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = colorResource(R.color.serena_text_dark),
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = time, fontSize = 10.sp, color = colorResource(R.color.serena_text_gray))
        }
    }
}

@Composable
private fun UserMessageBubble(text: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth(0.85f)) {
            Surface(
                color = colorResource(R.color.serena_card_chat_bg), // Beige
                // Borde redondeado excepto en la esquina superior derecha
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = colorResource(R.color.serena_text_dark),
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = time, fontSize = 10.sp, color = colorResource(R.color.serena_text_gray))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.icon_notis), // Reemplazar por ícono de doble check (check_all)
                    contentDescription = "Leído",
                    tint = colorResource(R.color.serena_green_primary),
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun PauseActionCard() {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp), // Alineado con el globo del bot
        shape = RoundedCornerShape(16.dp),
        color = colorResource(R.color.white),
        border = androidx.compose.foundation.BorderStroke(1.dp, colorResource(R.color.serena_search_border))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.serena_card_respiro_bg)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_viento),
                    contentDescription = null,
                    tint = colorResource(R.color.serena_green_dark),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Momento de pausa", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colorResource(R.color.serena_text_dark))
                Text("Inhala en 4 segundos, exhala", fontSize = 12.sp, color = colorResource(R.color.serena_text_gray))
            }
            Surface(
                shape = RoundedCornerShape(50),
                color = colorResource(R.color.serena_chip_selected_bg),
                modifier = Modifier.clickable { /* Navegar a RespiracionScreen */ }
            ) {
                Text("Iniciar", fontSize = 12.sp, color = colorResource(R.color.serena_green_dark), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun SuggestionsSection() {
    Column(modifier = Modifier.padding(start = 24.dp)) {
        Text("Sugerencias para responder:", fontSize = 11.sp, color = colorResource(R.color.serena_text_gray))
        Spacer(modifier = Modifier.height(8.dp))
        SuggestionChip(icon = "🫁", text = "Ejercicio de respiración")
        Spacer(modifier = Modifier.height(6.dp))
        SuggestionChip(icon = "📝", text = "Quiero desahogarme")
        Spacer(modifier = Modifier.height(6.dp))
        SuggestionChip(icon = "🌿", text = "Solo escuchar música suave")
    }
}

@Composable
private fun SuggestionChip(icon: String, text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = colorResource(R.color.serena_bg_input),
        modifier = Modifier.clickable { /* Rellenar input con la sugerencia */ }
    ) {
        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 12.sp) // Emoji nativo
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 13.sp, color = colorResource(R.color.serena_text_dark))
        }
    }
}

@Composable
private fun ChatInputArea() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(R.color.white),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono Micrófono
            Icon(
                painter = painterResource(id = R.drawable.icon_notis), // Cambiar por icono de micro (mic)
                contentDescription = "Audio",
                tint = colorResource(R.color.serena_icon_gray),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Campo de texto simulado
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                color = colorResource(R.color.white),
                border = androidx.compose.foundation.BorderStroke(1.dp, colorResource(R.color.serena_search_border))
            ) {
                Text(
                    text = "Escribe lo que sientes...",
                    fontSize = 14.sp,
                    color = colorResource(R.color.serena_text_gray),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            // Botón Enviar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.serena_green_primary)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_flechaizquierda), // Cambiar por icono de enviar (send)
                    contentDescription = "Enviar",
                    tint = colorResource(R.color.white),
                    modifier = Modifier.size(18.dp)
                )
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