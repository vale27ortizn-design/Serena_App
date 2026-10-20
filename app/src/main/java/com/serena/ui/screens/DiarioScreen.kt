package com.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.data.local.DatabaseHelper
import com.serena.ui.components.SerenaTopBar
import com.serena.ui.theme.SerenaAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DiarioScreen(
    modifier: Modifier = Modifier,
    onNewEntryClick: () -> Unit = {},
    onEntryClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }

    // Estado para guardar la lista de diarios desde la BD
    var listaDiarios by remember { mutableStateOf<List<DatabaseHelper.DiarioItem>>(emptyList()) }
    // Estado para el filtro actual
    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    // Función para recargar los diarios cada vez que se abre la pantalla
    LaunchedEffect(Unit) {
        listaDiarios = dbHelper.obtenerDiarios(1)
    }

    // Calculamos las emociones únicas para crear los chips de filtro dinámicamente
    val emocionesUnicas = listOf("Todos") + listaDiarios.map { it.emocion }.distinct()

    // Filtramos la lista según el chip seleccionado
    val diariosFiltrados = if (filtroSeleccionado == "Todos") {
        listaDiarios
    } else {
        listaDiarios.filter { it.emocion == filtroSeleccionado }
    }

    // Mapa de íconos según la emoción
    val emocionIconMap = mapOf(
        "Feliz" to R.drawable.emoji_feliz,
        "Tranquila" to R.drawable.emoji_tranquila,
        "Ansiosa" to R.drawable.emoji_ansiosa,
        "Cansada" to R.drawable.emoji_cansada,
        "Triste" to R.drawable.emoji_triste,
        "Frustrada" to R.drawable.emoji_frustada
    )

    // NUEVO: Mapa para pintar el ícono de la influencia seleccionada en las tarjetas
    val influenciaIconMap = mapOf(
        "Trabajo" to R.drawable.icon_trabajo,
        "Familia" to R.drawable.icon_familia,
        "Descanso" to R.drawable.icon_descanso,
        "Salud" to R.drawable.icon_salud,
        "Naturaleza" to R.drawable.icon_naturaleza,
        "Rutina" to R.drawable.icon_rutina
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TopBar superior fija
            SerenaTopBar(
                title = "Diario Emocional",
                onNotificationClick = { /* Acción notificaciones */ }
            )

            // Contenido con scroll vertical
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Barra de Búsqueda
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

                // Chips de Filtro con Scroll Horizontal
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    emocionesUnicas.forEach { emocion ->
                        FilterChipItem(
                            label = emocion,
                            iconRes = emocionIconMap[emocion],
                            isSelected = filtroSeleccionado == emocion,
                            onClick = { filtroSeleccionado = emocion }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tarjeta de Reflexión del Día
                ReflexionDelDiaCard()

                Spacer(modifier = Modifier.height(16.dp))

                // Lista Dinámica de Entradas del Diario
                if (diariosFiltrados.isEmpty()) {
                    Text(
                        text = "Aún no hay entradas para mostrar.",
                        color = colorResource(R.color.serena_text_gray),
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    diariosFiltrados.forEach { diario ->
                        // Formateamos la fecha (ej: "29 Septiembre") y la hora (ej: "16:45 hs")
                        val date = Date(diario.fecha)
                        val formatFecha = SimpleDateFormat("dd MMMM", Locale("es", "ES"))
                        val formatHora = SimpleDateFormat("HH:mm 'hs'", Locale.getDefault())

                        JournalEntryCard(
                            fecha = formatFecha.format(date),
                            contenido = diario.texto,
                            hora = formatHora.format(date),
                            influencias = diario.influencias,
                            influenciaIconMap = influenciaIconMap, // Pasamos el mapa de íconos
                            onClick = { onEntryClick(diario.id) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(70.dp)) // Espacio extra para el FAB
            }
        }

        // Botón Flotante para crear entrada
        FloatingActionButton(
            onClick = onNewEntryClick,
            containerColor = colorResource(R.color.serena_fab_bg),
            contentColor = colorResource(R.color.white),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_mas),
                contentDescription = "Nueva entrada",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ---------------- SUBCOMPONENTES ----------------

@Composable
private fun FilterChipItem(
    label: String,
    iconRes: Int?,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(50), // Da la forma redondeada
        color = if (isSelected) colorResource(R.color.serena_green_dark) else colorResource(R.color.serena_chip_unselected_bg),
        modifier = Modifier
            .clip(RoundedCornerShape(50)) // 1. Corta el borde
            .clickable { onClick() }      // 2. Aplica el click (ahora el efecto gris será redondeado)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun JournalEntryCard(
    fecha: String,
    contenido: String,
    hora: String,
    influencias: String,
    influenciaIconMap: Map<String, Int>, // Recibe el mapa
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = colorResource(R.color.serena_card_entry_bg)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Fila Superior: Fecha y la Influencia (Con su ícono)
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

                // Si hay influencias, mostramos la primera arriba a la derecha con su ícono
                if (influencias.isNotBlank()) {
                    val primeraInfluencia = influencias.split(",").first().trim()
                    val iconRes = influenciaIconMap[primeraInfluencia]

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = colorResource(R.color.white)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (iconRes != null) {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    tint = colorResource(R.color.serena_green_dark),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = primeraInfluencia,
                                fontSize = 11.sp,
                                color = colorResource(R.color.serena_text_gray),
                                fontWeight = FontWeight.Medium // Lo hacemos un poquito más resaltante
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contenido principal (cortado a 2 líneas)
            Text(
                text = contenido,
                fontSize = 14.sp,
                color = colorResource(R.color.serena_text_dark),
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        .background(colorResource(R.color.white)),
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