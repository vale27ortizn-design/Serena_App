package com.serena.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import kotlinx.coroutines.delay

@Composable
fun RespiracionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    // --- ESTADOS DE LA LÓGICA ---
    var isActive by remember { mutableStateOf(false) }
    var isInhaling by remember { mutableStateOf(true) } // true = Inhala (4s), false = Exhala (6s)

    // 4 segundos para inhalar, 6 segundos para exhalar
    var timeLeft by remember { mutableIntStateOf(4) }

    // Mensajes de apoyo guiados y amigables
    val mensaje = if (!isActive) {
        "Encuentra una postura cómoda, relaja los hombros y presiona comenzar."
    } else if (isInhaling) {
        "Inhala suavemente llenando tu pecho de aire..."
    } else {
        "Exhala despacio liberando toda la tensión acumulada..."
    }

    val tituloFase = "Momento de Respiro"

    // --- ANIMACIÓN FLUIDA DEL HALO ---
    val animDuration = if (isInhaling) 4000 else 6000
    val haloScale by animateFloatAsState(
        targetValue = if (isActive) (if (isInhaling) 1.65f else 1.0f) else 1.0f,
        animationSpec = tween(durationMillis = animDuration, easing = LinearEasing),
        label = "haloAnimation"
    )

    // --- TEMPORIZADOR (INHALA 4s / EXHALA 6s) ---
    LaunchedEffect(isActive, isInhaling) {
        if (isActive) {
            timeLeft = if (isInhaling) 4 else 6
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isInhaling = !isInhaling
        }
    }

    Scaffold(
        containerColor = colorResource(id = R.color.serena_card_sintonia_bg), // Fondo beige suave
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
                        contentDescription = "Volver al inicio",
                        tint = colorResource(id = R.color.serena_green_dark),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Respiración Guiada",
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // --- CABECERA DE TEXTOS ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = tituloFase,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.serena_green_dark),
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = mensaje,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.serena_text_gray),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            // --- ZONA DEL HALO MULTICAPA (EFECTO ORGÁNICO) ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(270.dp)
                    .padding(vertical = 10.dp)
            ) {
                // Capa exterior fija translúcida (da volumen en reposo)
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.serena_green_light).copy(alpha = 0.4f))
                )

                // Capa animada que se expande y contrae
                Box(
                    modifier = Modifier
                        .size(145.dp)
                        .scale(haloScale)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.serena_streak_bg).copy(alpha = 0.85f))
                )

                // Círculo central principal
                Surface(
                    modifier = Modifier.size(135.dp),
                    shape = CircleShape,
                    color = colorResource(id = R.color.serena_green_primary),
                    shadowElevation = 0.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (isActive) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = timeLeft.toString(),
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.white)
                                )
                                Text(
                                    text = "segundos",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colorResource(id = R.color.white).copy(alpha = 0.85f)
                                )
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_viento),
                                    contentDescription = null,
                                    tint = colorResource(id = R.color.white),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                }
            }

            // --- TARJETA INFORMATIVA CÁLIDA ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = colorResource(id = R.color.serena_card_diario_bg) // Beige cálido
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.white)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_corazon),
                            contentDescription = null,
                            tint = colorResource(id = R.color.serena_green_dark),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Exhalar durante 6 segundos estimula el nervio vago, ayudando a reducir tu ritmo cardíaco y calmar la mente.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.serena_text_dark),
                        lineHeight = 17.sp
                    )
                }
            }

            // --- BOTÓN PRINCIPAL DE ACCIÓN ---
            Button(
                onClick = {
                    if (isActive) {
                        isActive = false
                        isInhaling = true
                        timeLeft = 4
                    } else {
                        isActive = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) colorResource(id = R.color.serena_card_chat_bg) else colorResource(id = R.color.serena_green_primary),
                    contentColor = if (isActive) colorResource(id = R.color.serena_text_dark) else colorResource(id = R.color.white)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = if (isActive) "Detener ejercicio" else "Iniciar respiración",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RespiracionScreenPreview() {
    SerenaAppTheme {
        RespiracionScreen()
    }
}