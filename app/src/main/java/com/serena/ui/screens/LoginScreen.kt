package com.serena.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
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
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }

    var errorEmail by remember { mutableStateOf(false) }
    var errorClave by remember { mutableStateOf(false) }

    val colorPrimaryGreen = Color(0xFF81A282)
    val colorLightGreen = Color(0xFFE9F0E8)
    val colorDarkGreen = Color(0xFF5E7F65)
    val colorInputBackground = Color(0xFFF3F4EE)
    val colorTextDark = Color(0xFF333333)
    val colorTextGray = Color(0xFF7A7A7A)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = colorResource(R.color.verde_100)
            )
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = colorLightGreen,
                modifier = Modifier.clickable { }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_email),
                        contentDescription = "Hoja",
                        modifier = Modifier.size(12.dp),
                        tint = colorDarkGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tómate un respiro",
                        fontSize = 11.sp,
                        color = colorDarkGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.logo_serena),
                contentDescription = "Logo Serena",
                modifier = Modifier.size(150.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.logo_serena),
                    contentDescription = "Corazón",
                    tint = colorDarkGreen,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bienvenid@ de nuevo",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorTextDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Comienza tu viaje de entendimiento,\ncalma mental y escucha interior.",
            color = colorTextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp // junte un poco las lineas
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Correo electrónico",
                color = colorTextDark,
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
                        color = colorTextGray,
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.icon_email),
                        contentDescription = "Icono Email",
                        tint = colorTextGray,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = errorEmail,
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorInputBackground,
                    unfocusedContainerColor = colorInputBackground,
                    errorContainerColor = colorInputBackground,
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

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Contraseña",
                color = colorTextDark,
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
                        text = "Escribe tu clave aquí",
                        color = colorTextGray,
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.icon_candado),
                        contentDescription = "Icono Candado",
                        tint = colorTextGray,
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
                            tint = colorTextGray,
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
                    focusedContainerColor = colorInputBackground,
                    unfocusedContainerColor = colorInputBackground,
                    errorContainerColor = colorInputBackground,
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isEmpty() || !email.contains("@")) errorEmail = true
                if (clave.isEmpty() || clave.length < 8 || !clave.any { it.isUpperCase() } || !clave.any { it.isDigit() } || !clave.any { !it.isLetterOrDigit() }) {
                    errorClave = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = colorPrimaryGreen),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.icon_flechalogin),
                    contentDescription = "Flecha",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorTextGray)) {
                    append("¿No tienes una cuenta? ")
                }
                withStyle(style = SpanStyle(color = colorDarkGreen, fontWeight = FontWeight.Bold)) {
                    append("Regístrate aquí")
                }
            },
            modifier = Modifier
                .clickable { onNavigateToRegister() }
                .padding(bottom = 8.dp),
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true, heightDp = 700)
@Composable
fun LoginScreenPreview() {
    SerenaAppTheme {
        LoginScreen()
    }
}