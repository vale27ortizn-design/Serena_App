package com.serena

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.serena.ui.screens.SplashScreen
import com.serena.ui.theme.SerenaAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SerenaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen(
                        modifier = Modifier.padding(innerPadding),

                    )
                }
            }
        }

        // Usa lifecycleScope y delay (no congela la pantalla)
        lifecycleScope.launch {
            delay(3000) // Espera 3 segundos de forma asíncrona
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // Cierra el Splash para que no vuelva al presionar atrás
        }
    }
}


