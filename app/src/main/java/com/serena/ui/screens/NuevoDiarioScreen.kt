package com.serena.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.serena.R
import com.serena.data.local.DatabaseHelper
import com.serena.ui.components.SerenaBackTopBar

data class EmocionData(val nombre: String, val iconoRes: Int)
data class InfluenciaData(val nombre: String, val iconoRes: Int)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NuevoDiarioScreen(
    diarioIdToEdit: Int? = null, // NUEVO: Si recibimos un ID, entramos en modo edición
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    // --- CONTEXTO PARA LA BASE DE DATOS ---
    val context = LocalContext.current

    // --- ESTADOS ---
    var emocionSeleccionada by remember { mutableStateOf<String?>(null) }
    var textoDiario by remember { mutableStateOf("") }
    var influenciasSeleccionadas by remember { mutableStateOf(setOf<String>()) }
    var mostrarError by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // --- LÓGICA DE EDICIÓN ---
    var isEditing by remember { mutableStateOf(false) }

    // Si nos pasan un ID, buscamos los datos en la base de datos y los cargamos
    LaunchedEffect(diarioIdToEdit) {
        if (diarioIdToEdit != null && diarioIdToEdit != -1) {
            isEditing = true
            val dbHelper = DatabaseHelper(context)
            val diario = dbHelper.obtenerDiarioPorId(diarioIdToEdit)
            if (diario != null) {
                emocionSeleccionada = diario.emocion
                textoDiario = diario.texto
                if (diario.influencias.isNotBlank()) {
                    influenciasSeleccionadas = diario.influencias.split(",").map { it.trim() }.toSet()
                }
            }
        }
    }

    // --- EMOJIS ---
    val listaEmociones = listOf(
        EmocionData("Feliz", R.drawable.emoji_feliz),
        EmocionData("Tranquila", R.drawable.emoji_tranquila),
        EmocionData("Ansiosa", R.drawable.emoji_ansiosa),
        EmocionData("Cansada", R.drawable.emoji_cansada),
        EmocionData("Triste", R.drawable.emoji_triste),
        EmocionData("Frustrada", R.drawable.emoji_frustada)
    )

    // --- INFLUENCIAS ---
    val listaInfluencias = listOf(
        InfluenciaData("Trabajo", R.drawable.icon_trabajo),
        InfluenciaData("Familia", R.drawable.icon_familia),
        InfluenciaData("Descanso", R.drawable.icon_descanso),
        InfluenciaData("Salud", R.drawable.icon_salud),
        InfluenciaData("Naturaleza", R.drawable.icon_naturaleza),
        InfluenciaData("Rutina", R.drawable.icon_rutina)
    )

    Scaffold(
        containerColor = colorResource(id = R.color.white),
        topBar = {
            SerenaBackTopBar(
                title = if (isEditing) "Editar mi nota" else "Nuevo día, Nueva vida", // Cambia el título si editas
                onBackClick = onBackClick,
                onActionClick = onNotificationClick
            )
        },

        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.white))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isFormValid = emocionSeleccionada != null && textoDiario.isNotBlank()

                // --- VALIDACIÓN VISUAL (Letras rojas) ---
                if (mostrarError) {
                    if (emocionSeleccionada == null) {
                        Text(
                            text = "* Por favor, selecciona cómo te sientes hoy",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    } else if (textoDiario.isBlank()) {
                        Text(
                            text = "* Por favor, escribe qué hay en tu mente",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Button(
                    onClick = {
                        if (isFormValid) {
                            mostrarError = false
                            val influenciasTexto = influenciasSeleccionadas.joinToString(", ")
                            val dbHelper = DatabaseHelper(context)
                            val usuarioId = 1

                            // DECIDIMOS SI ACTUALIZAR O REGISTRAR UNO NUEVO
                            val exito = if (isEditing && diarioIdToEdit != null) {
                                dbHelper.actualizarDiario(diarioIdToEdit, emocionSeleccionada!!, textoDiario, influenciasTexto)
                            } else {
                                dbHelper.registrarDiario(usuarioId, emocionSeleccionada!!, textoDiario, influenciasTexto) != -1L
                            }

                            if (exito) {
                                showSuccessDialog = true
                            } else {
                                Toast.makeText(context, "Error al guardar el diario", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            mostrarError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFormValid) colorResource(id = R.color.serena_green_dark) else colorResource(id = R.color.serena_green_dark).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_check),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditing) "Guardar cambios" else "Guardar en mi diario",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // --- HEADER PAUSA ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colorResource(id = R.color.serena_bg_input)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.serena_green_dark)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_3hojas),
                            contentDescription = null,
                            tint = colorResource(id = R.color.white),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(22.dp))
                    Column {
                        Text(
                            text = "MOMENTO DE PAUSA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.serena_green_dark),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "¿Cómo te sientes hoy?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.serena_text_dark)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECCIÓN 1: EMOCIONES ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Elige tu emoción actual",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.serena_text_dark)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in 0 until 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (j in 0 until 3) {
                            val index = i * 3 + j
                            if (index < listaEmociones.size) {
                                val emocion = listaEmociones[index]
                                val isSelected = emocionSeleccionada == emocion.nombre

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(90.dp)
                                        .clickable { emocionSeleccionada = emocion.nombre },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) colorResource(id = R.color.serena_streak_bg) else colorResource(id = R.color.serena_bg_input),
                                    border = if (isSelected) BorderStroke(1.dp, colorResource(id = R.color.serena_green_dark)) else null
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = emocion.iconoRes),
                                            contentDescription = emocion.nombre,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = emocion.nombre,
                                            fontSize = 13.sp,
                                            color = if (isSelected) colorResource(id = R.color.serena_green_dark) else colorResource(id = R.color.serena_text_dark),
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECCIÓN 2: TEXTO LIBRE ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "¿Qué hay en tu mente?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.serena_text_dark)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = textoDiario,
                onValueChange = { textoDiario = it },
                placeholder = {
                    Text(
                        text = "Escribe libremente aquí, sin juicios ni prisas...",
                        color = colorResource(id = R.color.serena_text_gray),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(id = R.color.serena_text_dark),
                    unfocusedTextColor = colorResource(id = R.color.serena_text_dark),
                    unfocusedContainerColor = colorResource(id = R.color.white),
                    focusedContainerColor = colorResource(id = R.color.white),
                    unfocusedBorderColor = colorResource(id = R.color.serena_search_border),
                    focusedBorderColor = colorResource(id = R.color.serena_green_dark),
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_candado),
                        contentDescription = "Privado",
                        tint = colorResource(id = R.color.serena_green_dark),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Cifrado y privado",
                        fontSize = 11.sp,
                        color = colorResource(id = R.color.serena_green_dark)
                    )
                }
                Text(
                    text = "${textoDiario.length} caracteres",
                    fontSize = 11.sp,
                    color = colorResource(id = R.color.serena_text_gray)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECCIÓN 3: INFLUENCIAS (3 POR FILA) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "¿Qué influyó en tu estado?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.serena_text_dark)
                )
                Text(
                    text = "Opcional",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.serena_text_gray)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (i in 0 until 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (j in 0 until 3) {
                            val index = i * 3 + j
                            if (index < listaInfluencias.size) {
                                val item = listaInfluencias[index]
                                val isTagSelected = influenciasSeleccionadas.contains(item.nombre)

                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            if (isTagSelected) colorResource(id = R.color.serena_streak_bg)
                                            else colorResource(id = R.color.serena_bg_input)
                                        )
                                        .clickable {
                                            influenciasSeleccionadas = if (isTagSelected) {
                                                influenciasSeleccionadas - item.nombre
                                            } else {
                                                influenciasSeleccionadas + item.nombre
                                            }
                                        }
                                        .padding(horizontal = 6.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = item.iconoRes),
                                        contentDescription = item.nombre,
                                        tint = if (isTagSelected) colorResource(id = R.color.serena_green_dark) else colorResource(id = R.color.serena_text_dark),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = item.nombre,
                                        fontSize = 12.sp,
                                        color = if (isTagSelected) colorResource(id = R.color.serena_green_dark) else colorResource(id = R.color.serena_text_dark),
                                        fontWeight = if (isTagSelected) FontWeight.SemiBold else FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }

        // --- CUADRO DE AVISO (DIALOG) DE ÉXITO ---
        if (showSuccessDialog) {
            Dialog(onDismissRequest = { /* No hacer nada si tocan afuera para obligar a dar en Continuar */ }) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Círculo con el check
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(colorResource(id = R.color.serena_green_dark).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_check),
                                contentDescription = "Éxito",
                                tint = colorResource(id = R.color.serena_green_dark),
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "¡Diario guardado!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.serena_text_dark)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Tu reflexión está a salvo. Cada vez que escribes, das un hermoso paso hacia tu tranquilidad mental. ¡Sigue así!",
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.serena_text_gray),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                showSuccessDialog = false
                                onSaveClick() // Al dar Continuar, recién regresamos a la pantalla anterior
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.serena_green_dark)),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Continuar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NuevoDiarioScreenPreview() {
    NuevoDiarioScreen()
}