package com.serena.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
fun CatarsisScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var textoLiberacion by remember { mutableStateOf("") }
    var estaLiberado by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorResource(id = R.color.serena_badge_bg),
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
                    text = "Catarsis Simbólica",
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

            if (!estaLiberado) {
                // FASE 1: ESCRIBIR
                Text(
                    text = "¿Qué te frustra hoy?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.serena_text_dark)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Escríbelo aquí. Este texto no se guardará en ningún lado. Es solo para que lo saques de tu mente.",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.serena_text_gray),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = textoLiberacion,
                    onValueChange = { textoLiberacion = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = {
                        Text(
                            text = "Escribe libremente...",
                            color = colorResource(id = R.color.serena_text_gray)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(id = R.color.serena_text_dark),
                        unfocusedTextColor = colorResource(id = R.color.serena_text_dark),
                        focusedContainerColor = colorResource(id = R.color.white),
                        unfocusedContainerColor = colorResource(id = R.color.white),
                        focusedBorderColor = colorResource(id = R.color.serena_green_primary),
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { estaLiberado = true },
                    enabled = textoLiberacion.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.serena_green_primary)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Quemar y Liberar",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white)
                    )
                }

            } else {
                // FASE 2: LIBERADO
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.serena_green_light)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_viento),
                        contentDescription = null,
                        tint = colorResource(id = R.color.serena_green_dark),
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Lo has dejado ir",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.serena_green_dark)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Esa carga ya no está en ti. Respira profundo y continúa con tu día.",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.serena_text_gray),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

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
fun CatarsisScreenPreview() {
    SerenaAppTheme {
        CatarsisScreen()
    }
}