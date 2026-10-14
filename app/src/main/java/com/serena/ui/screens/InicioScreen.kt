package com.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.ui.components.SerenaTopBar

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    onNavigateToBotiquin: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToDiario: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
    ) {
        // TopBar fija superior
        SerenaTopBar(
            title = "Menú Principal",
            onNotificationClick = { /* Acción notificaciones */ }
        )

        // Contenido con scroll vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Saludo inicial
            Text(
                text = "Hola, Fernanda",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.serena_text_dark)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "¿Cómo te sientes en este momento?",
                fontSize = 13.sp,
                color = colorResource(R.color.serena_text_gray)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Card 1: Tu sintonía de hoy
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = colorResource(R.color.serena_card_sintonia_bg)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_cartitainterrogacion),
                                contentDescription = null,
                                tint = colorResource(R.color.serena_green_dark),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tu sintonía de hoy",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.serena_text_dark)
                            )
                        }

                        // Badge "Actualizado hace 2h"
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = colorResource(R.color.serena_badge_bg)
                        ) {
                            Text(
                                text = "Actualizado hace 2h",
                                fontSize = 10.sp,
                                color = colorResource(R.color.serena_text_gray),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Chips de emociones con SCROLL HORIZONTAL para evitar desborde
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EmotionChip(label = "En calma", iconRes = R.drawable.icon_hoja, isSelected = true)
                        EmotionChip(label = "Agradecida", iconRes = R.drawable.text_estrellitas, isSelected = false)
                        EmotionChip(label = "Cansada", iconRes = R.drawable.icon_nubecita, isSelected = false)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 2: Hablar con Serena
            ActionCard(
                backgroundColor = colorResource(R.color.serena_card_chat_bg),
                iconBgColor = colorResource(R.color.serena_card_chat_icon_bg),
                iconRes = R.drawable.icon_maceta,
                title = "Hablar con Serena",
                description = "Tu asistente de apoyo emocional siempre disponible para escucharte y acompañarte.",
                onClick = onNavigateToChat
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Card 3: Mi Diario Emocional
            ActionCard(
                backgroundColor = colorResource(R.color.serena_card_diario_bg),
                iconBgColor = colorResource(R.color.serena_card_diario_icon_bg),
                iconRes = R.drawable.icon_libro,
                title = "Mi Diario Emocional",
                description = "Registra tus pensamientos, desahoga tus cargas y reflexiona sobre tu crecimiento día a día.",
                onClick = onNavigateToDiario
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Card 4: Botiquín Emocional
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToBotiquin() },
                shape = RoundedCornerShape(20.dp),
                color = colorResource(R.color.serena_card_respiro_bg)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.serena_green_dark)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_corazon),
                            contentDescription = "Botiquín",
                            tint = colorResource(R.color.white),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "BOTIQUÍN EMOCIONAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.serena_green_dark),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Técnicas y herramientas guiadas para acompañarte según lo que necesites hoy.",
                            fontSize = 12.sp,
                            color = colorResource(R.color.serena_text_dark),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Explorar herramientas",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.serena_green_dark)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.icon_flechalogin),
                                contentDescription = null,
                                tint = colorResource(R.color.serena_green_dark),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Subcomponente: Chip de Emoción
@Composable
private fun EmotionChip(
    label: String,
    iconRes: Int,
    isSelected: Boolean
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (isSelected) colorResource(R.color.serena_chip_selected_bg) else colorResource(R.color.white)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = colorResource(R.color.serena_green_dark),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = colorResource(R.color.serena_text_dark)
            )
        }
    }
}

// Subcomponente: Tarjeta de Acción Grande
@Composable
private fun ActionCard(
    backgroundColor: Color,
    iconBgColor: Color,
    iconRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        tint = colorResource(R.color.serena_green_dark),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.icon_flechalogin),
                    contentDescription = "Ir a $title",
                    tint = colorResource(R.color.serena_text_dark),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.serena_text_dark)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                color = colorResource(R.color.serena_text_gray),
                lineHeight = 16.sp
            )
        }
    }
}