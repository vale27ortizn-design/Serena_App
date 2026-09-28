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
    // estados para guardar lo que escribe el usuario
    var nombres by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }

    // colores del tema de la app
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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // mensaje flotante de arriba
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = colorLightGreen
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_email),
                        contentDescription = null,
                        tint = colorDarkGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tú puedes, no te rindas",
                        fontSize = 11.sp,
                        color = colorDarkGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // titulo y texto de bienvenida
        Text(
            text = "Únete a Serena",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorTextDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Completa tus datos para crear tu\nespacio personal de bienestar.",
            color = colorTextGray,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // inputs de datos personales
        RegisterTextField(
            label = "Nombres",
            value = nombres,
            onValueChange = { nombres = it },
            placeholder = "Ingresa tus nombres",
            iconResId = R.drawable.icon_email
        )

        Spacer(modifier = Modifier.height(8.dp))

        RegisterTextField(
            label = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            placeholder = "ejemplo@correo.com",
            keyboardType = KeyboardType.Email,
            iconResId = R.drawable.icon_email
        )

        Spacer(modifier = Modifier.height(8.dp))

        // input especial para la contraseña (con toggle de ojito)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Contraseña",
                color = colorTextDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
            TextField(
                value = clave,
                onValueChange = { clave = it },
                placeholder = { Text("Crea una contraseña segura", color = colorTextGray, fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_candado),
                        contentDescription = null,
                        tint = colorTextGray,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { claveVisible = !claveVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (claveVisible) R.drawable.icon_ojoabierto else R.drawable.icon_ojocerrado
                            ),
                            contentDescription = null,
                            tint = colorTextGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (claveVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorInputBackground,
                    unfocusedContainerColor = colorInputBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // accion principal para guardar datos
        Button(
            onClick = { onRegisterSuccess() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = colorPrimaryGreen)
        ) {
            Text(text = "Guardar", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Tus datos están protegidos en tu espacio seguro de Serena",
            fontSize = 10.sp,
            color = colorTextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // enlace para volver al login si ya tiene cuenta
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = colorTextGray)) { append("¿Ya tienes una cuenta? ") }
                withStyle(SpanStyle(color = colorDarkGreen, fontWeight = FontWeight.Bold)) { append("Inicia sesión") }
            },
            modifier = Modifier.clickable { onNavigateToLogin() },
            fontSize = 12.sp
        )
    }
}

// componente reutilizable para no repetir codigo en los textfields normales
@Composable
private fun RegisterTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    iconResId: Int,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val colorInputBackground = Color(0xFFF3F4EE)
    val colorTextDark = Color(0xFF333333)
    val colorTextGray = Color(0xFF7A7A7A)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = colorTextDark,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = colorTextGray, fontSize = 12.sp) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = colorTextGray,
                    modifier = Modifier.size(18.dp)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorInputBackground,
                unfocusedContainerColor = colorInputBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, heightDp = 700)
@Composable
fun RegisterScreenPreview() {
    SerenaAppTheme {
        RegisterScreen()
    }
}