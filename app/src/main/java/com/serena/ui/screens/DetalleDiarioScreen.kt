package com.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.ui.components.SerenaBackTopBar
import com.serena.ui.theme.SerenaAppTheme

@Composable
fun DetalleDiarioScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            SerenaBackTopBar(
                title = "Mi Diario",
                onBackClick = onBackClick,
                onActionClick = { /* Acción de notificaciones / perfil */ }
            )
        },
        containerColor = colorResource(R.color.white)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {

            // 1. Cabecera: Chip de Emoción y Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chip de emoción (En Paz / Tranquilo)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colorResource(R.color.serena_card_respiro_bg) // Fondo menta suave
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_hoja), // Reemplazar si el id es distinto
                            contentDescription = "Emoción",
                            tint = colorResource(R.color.serena_green_dark),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "En Paz / Tranquilo",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.serena_green_dark)
                        )
                    }
                }

                // Botones Editar y Eliminar
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionIconButton(
                        iconRes = R.drawable.icon_pencil, // Asume que tienes este ícono
                        onClick = onEditClick
                    )
                    ActionIconButton(
                        iconRes = R.drawable.icon__delete, // Asume que tienes este ícono
                        onClick = onDeleteClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Fecha y Hora
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_reloj), // Asume ícono reloj
                    contentDescription = "Fecha",
                    tint = colorResource(R.color.serena_text_gray),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Domingo, 20 de Septiembre de 2026 • 16:45hrs",
                    fontSize = 12.sp,
                    color = colorResource(R.color.serena_text_gray),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Imagen adjunta con Badge sobrepuesto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_maceta), // Reemplazar con tu imagen
                    contentDescription = "Imagen del diario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Badge inferior izquierdo "Paseo de la tarde"
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(50),
                    color = colorResource(R.color.serena_badge_bg).copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_tree), // Ícono arbolito
                            contentDescription = null,
                            tint = colorResource(R.color.serena_green_dark),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Paseo de la tarde",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.serena_text_dark)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Texto del Diario (Cuerpo principal)
            Text(
                text = "Hoy me tomé 10 minutos para caminar bajo el sol y sentí mucha paz mental. Dejar el teléfono un rato y desconectarme un poco fue bueno. Al principio sentí esa inquietud de revisar notificaciones, pero poco a poco el sonido del viento y el aire fresco me devolvieron la calma. Supongo que el descanso tbm es productivo.",
                fontSize = 15.sp,
                color = colorResource(R.color.serena_text_dark),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Etiquetas (Tags)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TagChip(text = "#Desconexión")
                TagChip(text = "#Naturaleza")
                TagChip(text = "#Autocuidado")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 6. Tarjeta de Reflexión de Serena
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.serena_card_diario_bg) // Fondo beige cálido claro
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Ícono circular de Serena
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.serena_card_diario_icon_bg)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_serenaface), // Tu logo de serena
                            contentDescription = "Serena",
                            tint = colorResource(R.color.serena_green_dark),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Reflexión de Serena",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.serena_text_dark)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.icon_corazon), // Ícono corazón
                                contentDescription = null,
                                tint = colorResource(R.color.serena_green_dark),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "“Recuerda este momento de calma cuando sientas que el día se acelera. Lo hiciste muy bien.”",
                            fontSize = 13.sp,
                            color = colorResource(R.color.serena_text_gray),
                            lineHeight = 18.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 7. Botón Compartir nota
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShareClick() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_up), // Ícono compartir
                    contentDescription = "Compartir",
                    tint = colorResource(R.color.serena_text_dark),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Compartir nota",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.serena_text_dark)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// Subcomponente: Botones circulares grises (Editar/Eliminar)
@Composable
private fun ActionIconButton(iconRes: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(colorResource(R.color.serena_chip_unselected_bg))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = colorResource(R.color.serena_icon_gray),
            modifier = Modifier.size(16.dp)
        )
    }
}

// Subcomponente: Etiquetas/Tags grises
@Composable
private fun TagChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = colorResource(R.color.serena_chip_unselected_bg)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = colorResource(R.color.serena_text_gray),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleDiarioScreenPreview() {
    SerenaAppTheme {
        DetalleDiarioScreen()
    }
}