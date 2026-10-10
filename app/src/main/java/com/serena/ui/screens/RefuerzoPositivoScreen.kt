package com.serena.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun RefuerzoPositivoScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val afirmaciones = listOf(
        "Merezco sentirme en paz y darme el tiempo para sanar.",
        "Soy más fuerte de lo que creo y he superado días difíciles antes.",
        "Mis emociones son válidas, pero no me definen.",
        "Está bien no ser perfecta. Hago lo mejor que puedo y eso es suficiente.",
        "Inhalo calma, exhalo cualquier duda sobre mí misma."
    )

    var indiceActual by remember { mutableIntStateOf(0) }
    var haTerminado by remember { mutableStateOf(false) } // Controla si ya se mostraron todas

    Scaffold(
        containerColor = colorResource(id = R.color.serena_card_sintonia_bg),
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
                    text = "Refuerzo Positivo",
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

            if (!haTerminado) {
                // FASE 1: LEYENDO AFIRMACIONES
                Text(
                    text = "Repite esto en voz alta:",
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.serena_text_gray),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Tarjeta de Afirmación
                Surface(
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = colorResource(id = R.color.white),
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier.padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\"${afirmaciones[indiceActual]}\"",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.serena_green_dark),
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Contador de progreso
                Text(
                    text = "${indiceActual + 1} de ${afirmaciones.size}",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.serena_text_gray)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para siguiente afirmación
                Button(
                    onClick = {
                        if (indiceActual < afirmaciones.size - 1) {
                            indiceActual++
                        } else {
                            haTerminado = true // Al llegar a la última, cambia de pantalla
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.serena_green_primary)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Siguiente afirmación",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white)
                    )
                }
            } else {
                // FASE 2: MENSAJE FINAL DE ÉXITO
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.white)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_corazon), // O usa icon_check si tienes uno
                        contentDescription = null,
                        tint = colorResource(id = R.color.serena_green_dark),
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Estás lista para enfrentar al mundo!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.serena_green_dark),
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Has completado tus afirmaciones de hoy. Tu mente ahora está en un lugar más fuerte y positivo.",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.serena_text_gray),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Botón para salir
                Button(
                    onClick = { onBackClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.serena_green_primary)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Volver al botiquín",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RefuerzoPositivoPreview() {
    SerenaAppTheme {
        RefuerzoPositivoScreen()
    }
}