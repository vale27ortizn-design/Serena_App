package com.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.ui.theme.SerenaAppTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    // Estados para los valores de los campos
    var nombres by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }

    // Estados para los errores
    var errorNombres by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf(false) }
    var errorClave by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .verticalScroll(rememberScrollState())
            .imePadding() // Añadido para que el teclado no tape los campos
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mensaje superior flotante
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = colorResource(R.color.serena_green_light)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_hoja),
                        contentDescription = null,
                        tint = colorResource(R.color.serena_green_dark),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tú puedes, no te rindas",
                        fontSize = 11.sp,
                        color = colorResource(R.color.serena_green_dark),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Título y Subtítulo
        Text(
            text = "Únete a Serena",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.serena_text_dark)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Completa tus datos para crear tu\nespacio personal de bienestar.",
            color = colorResource(R.color.serena_text_gray),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo: Nombres
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Nombres",
                color = colorResource(R.color.serena_text_dark),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
            TextField(
                value = nombres,
                onValueChange = {
                    nombres = it
                    if (nombres.isNotBlank()) errorNombres = false
                },
                placeholder = {
                    Text(
                        text = "Ingresa tus nombres",
                        color = colorResource(R.color.serena_text_gray),
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_usuario),
                        contentDescription = "Icono Usuario",
                        tint = colorResource(R.color.serena_icon_gray),
                        modifier = Modifier.size(16.dp)
                    )
                },
                singleLine = true,
                isError = errorNombres,
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.serena_text_dark),
                    unfocusedTextColor = colorResource(R.color.serena_text_dark),
                    focusedContainerColor = colorResource(R.color.serena_bg_input),
                    unfocusedContainerColor = colorResource(R.color.serena_bg_input),
                    errorContainerColor = colorResource(R.color.serena_bg_input),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (errorNombres) {
                        Text(
                            text = "El nombre es obligatorio",
                            color = Color.Red,
                            fontSize = 10.sp
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Correo electrónico
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Correo electrónico",
                color = colorResource(R.color.serena_text_dark),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    if (email.isNotBlank()) errorEmail = false
                },
                placeholder = {
                    Text(
                        text = "ejemplo@correo.com",
                        color = colorResource(R.color.serena_text_gray),
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.icon_email),
                        contentDescription = "Icono Email",
                        tint = colorResource(R.color.serena_icon_gray),
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = errorEmail,
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.serena_text_dark),
                    unfocusedTextColor = colorResource(R.color.serena_text_dark),
                    focusedContainerColor = colorResource(R.color.serena_bg_input),
                    unfocusedContainerColor = colorResource(R.color.serena_bg_input),
                    errorContainerColor = colorResource(R.color.serena_bg_input),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (errorEmail) {
                        Text(
                            text = if (email.isEmpty()) "El correo es obligatorio" else "Ingrese un correo válido",
                            color = Color.Red,
                            fontSize = 10.sp
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Contraseña
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Contraseña",
                color = colorResource(R.color.serena_text_dark),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
            TextField(
                value = clave,
                onValueChange = {
                    clave = it
                    if (clave.isNotBlank()) errorClave = false
                },
                placeholder = {
                    Text(
                        text = "Crea una contraseña segura",
                        color = colorResource(R.color.serena_text_gray),
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.icon_candado),
                        contentDescription = "Icono Candado",
                        tint = colorResource(R.color.serena_icon_gray),
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { claveVisible = !claveVisible }) {
                        Icon(
                            painter = painterResource(
                                if (claveVisible) R.drawable.icon_ojoabierto else R.drawable.icon_ojocerrado
                            ),
                            contentDescription = "Ver Contraseña",
                            tint = colorResource(R.color.serena_icon_gray),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (claveVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = errorClave,
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.serena_text_dark),
                    unfocusedTextColor = colorResource(R.color.serena_text_dark),
                    focusedContainerColor = colorResource(R.color.serena_bg_input),
                    unfocusedContainerColor = colorResource(R.color.serena_bg_input),
                    errorContainerColor = colorResource(R.color.serena_bg_input),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (errorClave) {
                        val errorMessage = when {
                            clave.isEmpty() -> "La contraseña es obligatoria"
                            clave.length < 8 -> "Debe tener más de 8 caracteres"
                            !clave.any { it.isUpperCase() } -> "Debe tener una letra mayúscula"
                            !clave.any { it.isDigit() } -> "Debe tener al menos un número"
                            !clave.any { !it.isLetterOrDigit() } -> "Debe tener un caracter especial"
                            else -> ""
                        }
                        if (errorMessage.isNotEmpty()) Text(text = errorMessage, color = Color.Red, fontSize = 10.sp)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))


        //Guardar
        Button(
            onClick = {
                val isValidNombres = nombres.isNotBlank()
                val isValidEmail = email.isNotBlank() && email.contains("@")
                val isValidClave = clave.length >= 8 &&
                        clave.any { it.isUpperCase() } &&
                        clave.any { it.isDigit() } &&
                        clave.any { !it.isLetterOrDigit() }

                errorNombres = !isValidNombres
                errorEmail = !isValidEmail
                errorClave = !isValidClave

                // Solo si todo es válido, navega o ejecuta el registro
                if (isValidNombres && isValidEmail && isValidClave) {
                    onRegisterSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.serena_green_primary)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Guardar",
                color = colorResource(R.color.white),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Tus datos están protegidos en tu espacio seguro de Serena",
            fontSize = 10.sp,
            color = colorResource(R.color.serena_text_gray),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Redirección a Login
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = colorResource(R.color.serena_text_gray))) {
                    append("¿Ya tienes una cuenta? ")
                }
                withStyle(
                    SpanStyle(
                        color = colorResource(R.color.serena_green_dark),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Inicia sesión")
                }
            },
            modifier = Modifier
                .clickable { onNavigateToLogin() }
                .padding(bottom = 8.dp),
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, heightDp = 750)
@Composable
fun RegisterScreenPreview() {
    SerenaAppTheme {
        RegisterScreen()
    }
}