package com.serena.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.data.local.DatabaseHelper
import com.serena.data.repository.ChatRepository
import com.serena.ui.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetalleDiarioScreen(
    diarioId: Int,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current
    // No inicializamos DatabaseHelper en modo Preview para evitar problemas
    val dbHelper = remember { if (!isPreview) DatabaseHelper(context) else null }

    // Estados
    var diario by remember { 
        mutableStateOf<DatabaseHelper.DiarioItem?>(
            if (isPreview) {
                DatabaseHelper.DiarioItem(
                    id = 1,
                    fecha = System.currentTimeMillis(),
                    emocion = "Feliz",
                    texto = "Hoy fue un día increíble, pude avanzar con mis pendientes y me siento en paz.",
                    influencias = "Trabajo, Descanso"
                )
            } else null
        ) 
    }
    var isLoadingDiario by remember { mutableStateOf(!isPreview) } 
    var reflexionIA by remember { 
        mutableStateOf(if (isPreview) "Qué alegría que hoy haya sido un día tan productivo y pacífico. #Tranquilidad #Productividad #Paz" else "Generando reflexión y hashtags para ti...") 
    }
    var isReflexionLoading by remember { mutableStateOf(!isPreview) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (!isPreview) {
        LaunchedEffect(diarioId) {
            // 1. Cargar el diario en el Hilo de Fondo (Seguro contra cierres)
            val loadedDiario = withContext(Dispatchers.IO) {
                dbHelper?.obtenerDiarioPorId(diarioId)
            }
            diario = loadedDiario
            isLoadingDiario = false // Ya cargó, quitamos la pantalla de carga

            // 2. Generar la reflexión en el Hilo de Fondo
            loadedDiario?.let { miDiario ->
                try {
                    val resultado = withContext(Dispatchers.IO) {
                        // Instanciamos el repositorio aquí adentro para que no bloquee la pantalla
                        val repository = ChatRepository()
                        val promptOculto = ChatMessage(
                            id = "temp",
                            text = "Eres un terapeuta empático. Lee este diario: '${miDiario.texto}'. Escribe una breve y dulce reflexión (máximo 3 líneas) y al final agrega 3 hashtags relacionados con la emoción y el texto (ej: #Desconexión). No uses saludos, ve directo a la reflexión.",
                            isFromUser = true,
                            timestamp = System.currentTimeMillis()
                        )
                        // Solicitamos la respuesta
                        repository.sendMessage(history = listOf(promptOculto))
                    }

                    if (resultado.isSuccess) {
                        reflexionIA = resultado.getOrNull() ?: "No se pudo generar la reflexión."
                    } else {
                        reflexionIA = "No pude conectarme para generar tu reflexión hoy. Respira profundo."
                    }
                } catch (_: Exception) {
                    reflexionIA = "La reflexión no está disponible en este momento."
                } finally {
                    isReflexionLoading = false
                }
            }
        }
    }

    // Si sigue cargando, mostramos el spinner
    if (isLoadingDiario) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = colorResource(R.color.serena_green_dark))
        }
        return
    }

    // Si por algún error la nota se borró o no existe, mostramos este aviso en vez de congelar la app
    if (diario == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No se pudo cargar la nota. Regresa e intenta de nuevo.", color = colorResource(R.color.serena_text_gray))
        }
        return
    }
    val d = diario!!
    val date = Date(d.fecha)
    val formatoFecha = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy • HH:mm'hrs'", Locale("es", "ES"))

    val imagenRelacionadaRes = when (d.emocion) {
        "Feliz", "Con esperanza" -> R.drawable.cielo_soleado
        "Tranquila" -> R.drawable.mar_calmado
        "Ansiosa", "Algo abrumada" -> R.drawable.bosque_luz
        "Frustrada", "Triste", "Cansada" -> R.drawable.paisaje_relajante
        else -> R.drawable.ic_logo_completo_
    }
    val emocionIconMap = mapOf(
        "Feliz" to R.drawable.emoji_feliz,
        "Tranquila" to R.drawable.emoji_tranquila,
        "Ansiosa" to R.drawable.emoji_ansiosa,
        "Cansada" to R.drawable.emoji_cansada,
        "Triste" to R.drawable.emoji_triste,
        "Frustrada" to R.drawable.emoji_frustada
    )
    val iconEmocionRes = emocionIconMap[d.emocion]

    Scaffold(
        containerColor = colorResource(R.color.white),
        topBar = {
            Surface(color = colorResource(R.color.white), shadowElevation = 1.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_flechaizquierda),
                            contentDescription = "Volver",
                            tint = colorResource(R.color.serena_text_dark),
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .clickable { onBackClick() }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Mi Diario", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.serena_text_dark))
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.serena_green_dark)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painter = painterResource(id = R.drawable.icon_notis), contentDescription = "Avisos", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = colorResource(R.color.white),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, "Un pedacito de mi diario en Serena:\n\n${d.texto}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Compartir mi nota con:")
                            context.startActivity(shareIntent)
                        }
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.icon_up), contentDescription = "Compartir", tint = colorResource(R.color.serena_text_dark), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compartir nota", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = colorResource(R.color.serena_text_dark))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Emoción y Botones de Acción (Editar / Eliminar) - ¡Aquí están tus íconos!
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colorResource(R.color.serena_streak_bg)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (iconEmocionRes != null) {
                            Icon(
                                painter = painterResource(id = iconEmocionRes),
                                contentDescription = null,
                                tint = Color.Unspecified, // Mantiene el color original de tu emoji
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = d.emocion,
                            fontSize = 14.sp,
                            color = colorResource(R.color.serena_green_dark),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Ícono de Editar (Asegúrate de que este nombre sea correcto en tu drawable)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_pencil),
                        contentDescription = "Editar",
                        tint = colorResource(R.color.serena_text_gray),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onEditClick() }
                    )

                    // Ícono de Eliminar (Asegúrate de que este nombre sea correcto en tu drawable)
                    Icon(
                        painter = painterResource(id = R.drawable.icon__delete),
                        contentDescription = "Eliminar",
                        tint = colorResource(R.color.serena_text_gray),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showDeleteDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.icon_reloj), contentDescription = null, tint = colorResource(R.color.serena_text_gray), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = formatoFecha.format(date).replaceFirstChar { it.uppercase() }, fontSize = 12.sp, color = colorResource(R.color.serena_text_gray))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = imagenRelacionadaRes),
                contentDescription = "Imagen de la emoción",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            if (d.influencias.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colorResource(R.color.serena_bg_input)
                ) {
                    Text(
                        text = "Influencia principal: ${d.influencias.split(",").first().trim()}",
                        fontSize = 11.sp,
                        color = colorResource(R.color.serena_text_dark),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = d.texto,
                fontSize = 16.sp,
                color = colorResource(R.color.serena_text_dark),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.serena_card_reflexion_bg)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(32.dp).clip(CircleShape).background(colorResource(R.color.serena_green_dark).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(painter = painterResource(id = R.drawable.icon_hoja), contentDescription = null, tint = colorResource(R.color.serena_green_dark), modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Reflexión de Serena \uD83D\uDC9A", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.serena_green_dark))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isReflexionLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = colorResource(R.color.serena_green_dark))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"$reflexionIA\"",
                        fontSize = 14.sp,
                        color = colorResource(R.color.serena_text_dark),
                        fontStyle = FontStyle.Italic,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = Color.White,
                title = {
                    Text(text = "Eliminar nota", fontWeight = FontWeight.Bold, color = colorResource(R.color.serena_text_dark))
                },
                text = {
                    Text(
                        text = "¿Estás segura de que deseas eliminar esta reflexión de tu diario? Esta acción no se puede deshacer.",
                        color = colorResource(R.color.serena_text_gray)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val exito = dbHelper?.eliminarDiario(diarioId) ?: false
                            if (exito) {
                                Toast.makeText(context, "Nota eliminada", Toast.LENGTH_SHORT).show()
                                showDeleteDialog = false
                                onBackClick()
                            } else {
                                Toast.makeText(context, "Error al eliminar la nota", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Text("Sí, eliminar", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar", color = colorResource(R.color.serena_green_dark), fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DetalleDiarioScreenPreview() {
    // Le pasamos un ID falso (ej: 1) solo para que Android Studio pueda pintar la pantalla vacía en el panel de diseño.
    DetalleDiarioScreen(
        diarioId = 1,
        onBackClick = {},
        onEditClick = {}
    )
}