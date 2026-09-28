package com.serena.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.serena.R
import com.serena.ui.theme.SerenaAppTheme

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    // Lottie
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.white))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_serena),
                contentDescription = "Logo Serena",
                modifier = Modifier.size(200.dp),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Tu espacio seguro para\nrespirar y sentir",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(R.color.serena_text_gray),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        // Animacion
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            if (LocalInspectionMode.current) {
                // Vista previa
                Text(
                    text = "...",
                    fontSize = 24.sp,
                    color = colorResource(R.color.serena_green_dark)
                )
            } else {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 700)
@Composable
fun SplashScreenPreview() {
    SerenaAppTheme {
        SplashScreen()
    }
}