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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun EjerciciosScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToExercise: (String) -> Unit = {}
) {
    Scaffold(
        containerColor = colorResource(id = R.color.white),
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
                        .background(colorResource(id = R.color.serena_card_sintonia_bg))
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
                    text = "Botiquín Emocional",
                    fontSize = 18.sp,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            Text(
                text = "Técnicas de apoyo",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.serena_text_dark)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Selecciona la herramienta que mejor se adapte a lo que necesitas sentir en este momento.",
                fontSize = 13.sp,
                color = colorResource(R.color.serena_text_gray),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ejercicio 1: Respiración
            EjercicioCard(
                title = "Respiración Guiada",
                description = "Activa tu sistema parasimpático para reducir el ritmo cardíaco y la tensión.",
                iconRes = R.drawable.icon_viento,
                tag = "Para relajar",
                backgroundColor = colorResource(R.color.serena_card_respiro_bg),
                onClick = { onNavigateToExercise("respiracion") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ejercicio 2: Técnica 5-4-3-2-1
            EjercicioCard(
                title = "Aterrizaje 5-4-3-2-1",
                description = "Ancla tu mente en el presente utilizando tus 5 sentidos para frenar la ansiedad.",
                iconRes = R.drawable.icon_hoja,
                tag = "Para la ansiedad",
                backgroundColor = colorResource(R.color.serena_card_chat_bg), // Usamos el verde del chat
                onClick = { onNavigateToExercise("aterrizaje") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ejercicio 3: Refuerzo Positivo
            EjercicioCard(
                title = "Refuerzo Positivo",
                description = "Transforma el diálogo interno negativo a través de afirmaciones guiadas.",
                iconRes = R.drawable.icon_corazon,
                tag = "Para motivar",
                backgroundColor = colorResource(R.color.serena_card_sintonia_bg),
                onClick = { onNavigateToExercise("afirmaciones") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ejercicio 4: Catarsis Simbólica
            EjercicioCard(
                title = "Catarsis Simbólica",
                description = "Escribe lo que te frustra y libéralo simbólicamente para soltar la carga.",
                iconRes = R.drawable.icon_libro,
                tag = "Para soltar",
                backgroundColor = colorResource(R.color.serena_badge_bg), // Usamos un tono distinto
                onClick = { onNavigateToExercise("liberacion") }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Componente reutilizable para cada tarjeta de ejercicio
@Composable
private fun EjercicioCard(
    title: String,
    description: String,
    iconRes: Int,
    tag: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.white)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        tint = colorResource(R.color.serena_green_dark),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = colorResource(R.color.white).copy(alpha = 0.6f)
                ) {
                    Text(
                        text = tag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(R.color.serena_text_dark),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
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

@Preview(showBackground = true)
@Composable
fun EjerciciosScreenPreview() {
    SerenaAppTheme {
        EjerciciosScreen()
    }
}