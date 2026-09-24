package com.serena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R
import com.serena.ui.theme.SerenaAppTheme


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
){
    //declarar variables
    var email by remember { mutableStateOf( "") }
    var clave by remember { mutableStateOf( "") }
    var claveVisible by remember { mutableStateOf(false) }

    var errorEmail by remember { mutableStateOf(false) }
    var errorClave by remember { mutableStateOf(false) }

    //interfaz
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.verde_100))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenid@ de nuevo",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.negro_100)
        )
        Text(
            text = "Comienza tu viaje de entendimiento, clama mental y escucha interior",
            color = colorResource(R.color.gris_txt),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Correo electrónico",
            color = colorResource(R.color.negro_100)
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (email.isNotBlank()) {
                    errorEmail = false
                }
            },
            label = {
                Text(
                    text = "ejemplo@gmail.com",
                    color = colorResource(R.color.gris_txt)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.icon_email),
                    contentDescription = null,
                    tint = colorResource(R.color.gris_icon),
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = errorEmail,
            supportingText = {
                if (errorEmail) {
                    if (email.isEmpty()) {
                        Text(
                            text = "El correo es obligatorio"
                        )
                    } else if (!email.contains("@")) {
                        Text(
                            text = "Ingrese un correo válido"
                        )
                    }
                } else {
                    null
                }
            },
        )

        Text(
            text = "Contraseña",
            color = colorResource(R.color.negro_100)
        )
        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it
                if (clave.isNotBlank()) {
                    errorClave = false
                }
            },
            label = {
                Text(
                    text = "Escribe tu clave aquí",
                    color = colorResource(R.color.gris_txt)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.icon_candado),
                    contentDescription = null,
                    tint = colorResource(R.color.gris_icon),
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (claveVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        claveVisible = !claveVisible
                    }
                ) {
                    Icon(
                        painter = if (claveVisible) {
                            painterResource(R.drawable.icon_ojocerrado)

                        } else {
                            painterResource(R.drawable.icon_ojoabierto)
                        },
                        contentDescription = "",
                        tint = colorResource(R.color.gris_icon)
                    )
                }
            },

            isError = errorClave,
            supportingText = {
                if (errorClave) {
                    when {
                        clave.isEmpty() -> {
                            Text(
                                text = "La contraseña es obligatoria"
                            )
                        }
                        //---------------------------------------------------------------------------
                        clave.length < 8 -> {
                            Text(
                                text = "La contraseña debe tener más de 8 caracteres"
                            )
                        }
                        //---------------------------------------------------------------------------
                        !clave.any {
                            it.isUpperCase()
                        } -> {
                            Text(
                                text = "La contraseña debe tener al menos una letra mayúscula"
                            )
                        }

                        !clave.any {
                            it.isDigit()
                        } -> {
                            Text(
                                text = "La contraseña debe tener al menos un número"
                            )
                        }
                        //---------------------------------------------------------------------------
                        !clave.any {
                            !it.isLetterOrDigit()
                        } -> {
                            Text(
                                text = "La contraseña debe tener al menos un caracter especial"
                            )
                        }
                    }
                }
            }
        )
        Button(
            onClick = {
                // logica
                if (email.isEmpty() || !email.contains("@")) {
                    errorEmail = true
                }
                if (clave.isEmpty() || clave.length < 8 || !clave.any { it.isUpperCase() } || !clave.any { it.isDigit() } || !clave.any { !it.isLetterOrDigit() }) {
                    errorClave = true
                }
            }
        ) {
            colorResource(R.color.verde_300)
            Text(
                text = "Iniciar Sesión",
                color = colorResource(R.color.white)
            )

            Icon(
                painter = painterResource(id = R.drawable.icon_flechalogin),
                contentDescription = "Flecha de inicio de sesión"
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    SerenaAppTheme {
        LoginScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}