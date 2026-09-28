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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.ui.components.SerenaBottomNavBar
import com.serena.ui.components.SerenaTab
import com.serena.ui.components.SerenaTopBar
import com.serena.ui.theme.SerenaAppTheme

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    onNavigateToTab: (SerenaTab) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            SerenaTopBar(
                title = "Perfil",
                onNotificationClick = { /* Acción notificaciones */ }
            )
        },
        bottomBar = {
            SerenaBottomNavBar(
                currentTab = SerenaTab.PERFIL,
                onTabSelected = { tab -> onNavigateToTab(tab) }
            )
        },
        containerColor = colorResource(R.color.white)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Principal de Perfil (Fondo Beige/Crema)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = colorResource(R.color.serena_bg_input)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar con botón de Cámara
                        Box {
                            Image(
                                painter = painterResource(id = R.drawable.logo_serena), // Cambiar por tu foto de perfil
                                contentDescription = "Foto de Perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                            )
                            // Ícono de cámara flotante
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = 2.dp, y = 2.dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(colorResource(R.color.serena_green_dark)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_email), // Cambiar por ícono cámara
                                    contentDescription = "Cambiar Foto",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Nombre y Miembro desde
                        Column {
                            Text(
                                text = "Fernanda Navarro",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.serena_text_dark)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Miembro desde Septiembre 2026",
                                fontSize = 12.sp,
                                color = colorResource(R.color.serena_text_gray)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Banner de Días Consecutivos (Tarjeta Blanca interna)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = colorResource(R.color.white)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(colorResource(R.color.serena_bg_input)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_email), // Ícono Fuego/Hojas
                                        contentDescription = "Racha",
                                        tint = colorResource(R.color.serena_green_dark),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "14 días consecutivos",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(R.color.serena_text_dark)
                                    )
                                    Text(
                                        text = "Registrando emociones en calma",
                                        fontSize = 11.sp,
                                        color = colorResource(R.color.serena_text_gray)
                                    )
                                }
                            }

                            // Badge verificado a la derecha
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(colorResource(R.color.serena_bg_input)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_email), // Ícono Check
                                    contentDescription = "Verificado",
                                    tint = colorResource(R.color.serena_green_dark),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección: Datos Personales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Datos Personales",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.serena_text_dark)
                )
                Text(
                    text = "Guardado automático",
                    fontSize = 11.sp,
                    color = colorResource(R.color.serena_green_dark)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Card Datos Personales
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.serena_bg_input)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                    // Nombre Completo
                    DatoPersonalItem(
                        iconRes = R.drawable.icon_email, // Ícono Usuario
                        label = "Nombre completo",
                        value = "Fernanda Navarro"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Correo Electrónico
                    DatoPersonalItem(
                        iconRes = R.drawable.icon_email,
                        label = "Correo electrónico",
                        value = "fer.navo@email.com"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección: Seguridad y Bienestar
            Text(
                text = "Seguridad y Bienestar",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.serena_text_dark),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Opciones de Seguridad y Bienestar
            OpcionAjusteItem(
                iconRes = R.drawable.icon_candado,
                titulo = "Cambiar contraseña",
                subtitulo = "Actualizada hace 2 meses",
                onClick = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OpcionAjusteItem(
                iconRes = R.drawable.icon_email,
                titulo = "Recordatorios de calma",
                subtitulo = "Notificaciones suaves y respiración diaria",
                onClick = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OpcionAjusteItem(
                iconRes = R.drawable.icon_candado,
                titulo = "Privacidad del diario",
                subtitulo = "Tus reflexiones están encriptadas",
                onClick = { }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón: Cerrar Sesión
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    colorResource(R.color.serena_green_dark)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_flechalogin), // Ícono Salir
                        contentDescription = "Cerrar sesión",
                        tint = colorResource(R.color.serena_green_dark),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar sesión",
                        color = colorResource(R.color.serena_green_dark),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer / Texto informativo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_email),
                    contentDescription = null,
                    tint = colorResource(R.color.serena_green_dark),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Serena v1.1.0 • Hecho con cariño para tu tranquilidad",
                    fontSize = 10.sp,
                    color = colorResource(R.color.serena_text_gray)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Tus datos emocionales están protegidos y permanecen contigo",
                fontSize = 10.sp,
                color = colorResource(R.color.serena_text_gray),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Sub-componente para los ítems de Datos Personales
@Composable
private fun DatoPersonalItem(
    iconRes: Int,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = colorResource(R.color.serena_text_gray),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = colorResource(R.color.serena_text_gray)
                )
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = colorResource(R.color.serena_text_dark),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Ícono Check
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .border(1.dp, colorResource(R.color.serena_green_dark), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_email), // Reemplazar por checkmark
                contentDescription = "OK",
                tint = colorResource(R.color.serena_green_dark),
                modifier = Modifier.size(10.dp)
            )
        }
    }
}

// Sub-componente para las opciones de Seguridad y Bienestar
@Composable
private fun OpcionAjusteItem(
    iconRes: Int,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.serena_bg_input)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = colorResource(R.color.serena_text_dark),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = titulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.serena_text_dark)
                )
                Text(
                    text = subtitulo,
                    fontSize = 11.sp,
                    color = colorResource(R.color.serena_text_gray)
                )
            }
        }

        // Flecha a la derecha
        Icon(
            painter = painterResource(id = R.drawable.icon_flechalogin), // Cambiar por flecha a la derecha '>'
            contentDescription = "Siguiente",
            tint = colorResource(R.color.serena_text_gray),
            modifier = Modifier.size(14.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    SerenaAppTheme {
        PerfilScreen()
    }
}