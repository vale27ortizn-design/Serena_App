package com.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.ui.components.SerenaBottomNavBar
import com.serena.ui.components.SerenaTab
import com.serena.ui.components.SerenaTopBar
import com.serena.ui.theme.SerenaAppTheme

@Composable
fun DiarioScreen(
    modifier: Modifier = Modifier,
    onNavigateToTab: (SerenaTab) -> Unit = {},
    onNewEntryClick: () -> Unit = {},
    onEntryClick: () -> Unit = {} // <-- 1. Añadimos el parámetro aquí
) {
    Scaffold(
        topBar = {
            SerenaTopBar(
                title = "Diario Emocional",
                onNotificationClick = { /* Acción notificaciones */ }
            )
        },
        bottomBar = {
            SerenaBottomNavBar(
                currentTab = SerenaTab.DIARIO,
                onTabSelected = { tab -> onNavigateToTab(tab) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewEntryClick,
                containerColor = colorResource(R.color.serena_fab_bg),
                contentColor = colorResource(R.color.white),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_mas),
                    contentDescription = "Nueva entrada",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        containerColor = colorResource(R.color.white)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            // Barra de Búsqueda Falsa
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.serena_search_border),
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { /* Acción buscar */ },
                color = colorResource(R.color.white),
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_lupa),
                        contentDescription = "Buscar",
                        tint = colorResource(R.color.serena_text_gray),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Buscar...",
                        fontSize = 13.sp,
                        color = colorResource(R.color.serena_text_gray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chips de Filtro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipItem(label = "Todos", iconRes = null, isSelected = true)
                FilterChipItem(label = "Tranquila", iconRes = R.drawable.icon_paztranquilidad, isSelected = false)
                FilterChipItem(label = "Con esperanza", iconRes = R.drawable.icon_solnube, isSelected = false)
                FilterChipItem(label = "Abrumada", iconRes = R.drawable.icon_nubellorando, isSelected = false)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Reflexión del Día
            ReflexionDelDiaCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Entradas del Diario
            // 2. Pasamos el evento onClick a cada tarjeta
            JournalEntryCard(
                fecha = "Hoy, 29 Septiembre",
                emocion = "Tranquila",
                emocionIconRes = R.drawable.icon_paztranquilidad,
                contenido = "Hoy me tomé 10 minutos para caminar bajo el sol y sentí mucha paz mental. Dejar el...",
                hora = "16:45 hs",
                onClick = onEntryClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            JournalEntryCard(
                fecha = "Ayer, 28 de Septiembre",
                emocion = "Con esperanza",
                emocionIconRes = R.drawable.icon_solnube,
                contenido = "Logré terminar mi proyecto a tiempo. Aunque hubo tensión, pude respirar profundo y con...",
                hora = "20:15 hs",
                onClick = onEntryClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            JournalEntryCard(
                fecha = "Jueves, 24 de Septiembre",
                emocion = "Algo abrumada",
                emocionIconRes = R.drawable.icon_nubellorando,
                contenido = "Hoy es un día con muchas tareas pendientes. Serena me sugirió priorizar y me ayudó a...",
                hora = "11:30 hs",
                onClick = onEntryClick
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// ---------------- SUBCOMPONENTES ----------------

@Composable
private fun FilterChipItem(
    label: String,
    iconRes: Int?,
    isSelected: Boolean
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (isSelected) colorResource(R.color.serena_green_dark) else colorResource(R.color.serena_chip_unselected_bg)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { /* Acción seleccionar filtro */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = if (isSelected) colorResource(R.color.white) else colorResource(R.color.serena_text_dark)
            )
        }
    }
}

@Composable
private fun ReflexionDelDiaCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colorResource(R.color.serena_card_reflexion_bg)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.white)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_corazon),
                    contentDescription = "Reflexión",
                    tint = colorResource(R.color.serena_green_dark),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "REFLEXIÓN DEL DÍA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.serena_green_dark),
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"Honrar cada emoción es el primer paso hacia una calma duradera.\"",
                    fontSize = 13.sp,
                    color = colorResource(R.color.serena_text_dark),
                    fontStyle = FontStyle.Italic,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun JournalEntryCard(
    fecha: String,
    emocion: String,
    emocionIconRes: Int,
    contenido: String,
    hora: String,
    onClick: () -> Unit = {} // <-- 3. Añadimos el parámetro al subcomponente
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // <-- 4. Hacemos toda la tarjeta clickeable
        shape = RoundedCornerShape(20.dp),
        color = colorResource(R.color.serena_card_entry_bg)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Fila Superior: Fecha y Emoción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fecha,
                    fontSize = 12.sp,
                    color = colorResource(R.color.serena_text_dark)
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = colorResource(R.color.white)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = emocionIconRes),
                            contentDescription = emocion,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = emocion,
                            fontSize = 11.sp,
                            color = colorResource(R.color.serena_text_dark)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contenido de la entrada
            Text(
                text = contenido,
                fontSize = 14.sp,
                color = colorResource(R.color.serena_text_dark),
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Fila Inferior: Hora y Botón Flecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_reloj),
                        contentDescription = "Hora",
                        tint = colorResource(R.color.serena_text_dark),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = hora,
                        fontSize = 11.sp,
                        color = colorResource(R.color.serena_text_dark)
                    )
                }

                // Botón Circular de Flecha
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.white))
                        .clickable { onClick() }, // <-- 5. También agregamos el clic aquí por si el usuario toca la flecha
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_flechalogin),
                        contentDescription = "Ver entrada",
                        tint = colorResource(R.color.serena_text_dark),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiarioScreenPreview() {
    SerenaAppTheme {
        DiarioScreen()
    }
}