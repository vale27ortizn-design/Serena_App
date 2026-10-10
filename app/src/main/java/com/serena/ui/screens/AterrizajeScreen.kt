package com.serena.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.ui.theme.SerenaAppTheme

@Composable
fun AterrizajeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    // Estado para controlar en qué paso de la técnica (5 a 0) se encuentra el usuario
    var pasoActual by remember { mutableIntStateOf(5) }

    // NUEVO: Estado para contar cuántos elementos ha encontrado/marcado el usuario en el paso actual
    var itemsMarcados by remember { mutableIntStateOf(0) }

    // Textos dinámicos según el paso
    val (titulo, descripcion, emoji) = when (pasoActual) {
        5 -> Triple("5 Cosas", "Busca a tu alrededor y nombra mentalmente 5 cosas que puedas VER (colores, objetos, luces). Toca un círculo por cada una.", "👁️")
        4 -> Triple("4 Cosas", "Toca y siente la textura de 4 cosas cerca de ti (tu ropa, la silla). Toca un círculo por cada una.", "🖐️")
        3 -> Triple("3 Cosas", "Haz silencio y nombra 3 cosas que puedas ESCUCHAR ahora mismo.", "👂")
        2 -> Triple("2 Cosas", "Respira profundo y busca 2 cosas que puedas OLER o imaginar su aroma.", "👃")
        1 -> Triple("1 Cosa", "Nombra 1 cosa que puedas SABOREAR en este momento.", "👅")
        else -> Triple("¡Lo lograste!", "Has completado el ciclo. Tu mente ahora está anclada en el momento presente. Respira profundo.", "✨")
    }

    // Verifica si el usuario ya marcó todos los checks requeridos en este paso
    val puedeAvanzar = itemsMarcados == pasoActual

    Scaffold(
        containerColor = colorResource(id = R.color.serena_card_chat_bg),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.white))
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_flechaizquierda),
                        contentDescription = "Volver",
                        tint = colorResource(id = R.color.serena_green_dark),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Aterrizaje 5-4-3-2-1",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.serena_text_dark)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Tarjeta central
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = colorResource(id = R.color.white),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.serena_badge_bg)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 40.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = titulo,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.serena_green_dark)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = descripcion,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.serena_text_gray),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    // NUEVO: Zona interactiva de Checkboxes/Círculos (Solo se muestra si no ha terminado)
                    if (pasoActual > 0) {
                        Spacer(modifier = Modifier.height(28.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Dibuja la cantidad de círculos exacta según el paso actual (Ej: 5 círculos)
                            for (i in 0 until pasoActual) {
                                val estaMarcado = i < itemsMarcados
                                val colorFondo by animateColorAsState(
                                    targetValue = if (estaMarcado) colorResource(id = R.color.serena_green_primary) else Color.Transparent,
                                    label = "colorAnim"
                                )

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(colorFondo)
                                        .border(
                                            width = 2.dp,
                                            color = colorResource(id = R.color.serena_green_primary),
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            // Solo permite marcar el siguiente si no ha llegado al límite
                                            if (itemsMarcados < pasoActual && !estaMarcado) {
                                                itemsMarcados++
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (estaMarcado) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_check), // Asegúrate de tener este ícono o usa icon_corazon
                                            contentDescription = "Marcado",
                                            tint = colorResource(id = R.color.white),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botón de acción
            Button(
                onClick = {
                    if (pasoActual > 0) {
                        pasoActual-- // Avanza al siguiente paso
                        itemsMarcados = 0 // Reinicia los checks para el nuevo paso
                    } else {
                        onBackClick() // Si ya terminó, regresa al botiquín
                    }
                },
                enabled = puedeAvanzar || pasoActual == 0, // Solo se habilita si marcó todos o si ya terminó
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.serena_green_primary),
                    disabledContainerColor = colorResource(id = R.color.serena_green_primary).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = if (pasoActual > 0) "Siguiente paso" else "Finalizar y volver",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.white)
                )
            }

            if (pasoActual > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Paso ${6 - pasoActual} de 5",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.serena_text_gray)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AterrizajeScreenPreview() {
    SerenaAppTheme {
        AterrizajeScreen()
    }
}