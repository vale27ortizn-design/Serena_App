package com.serena

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.serena.ui.components.SerenaBottomNavBar
import com.serena.ui.navigation.AppDestinations
import com.serena.ui.navigation.AppNavigation
import com.serena.ui.theme.SerenaAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Barras del sistema negras
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.BLACK),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.BLACK)
        )

        setContent {
            SerenaAppTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                val hideBottomBarRoutes = listOf(
                    AppDestinations.InicioSesion.route,
                    AppDestinations.Registro.route,
                    AppDestinations.Chat.route
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .systemBarsPadding()
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = colorResource(R.color.white),
                        contentWindowInsets = WindowInsets(0.dp),
                        bottomBar = {
                            if (currentRoute != null && currentRoute !in hideBottomBarRoutes) {
                                SerenaBottomNavBar(navController = navController)
                            }
                        }
                    ) { innerPadding ->
                        // Ya no requiere pasar parámetros extra, AppNavigation maneja todo internamente
                        AppNavigation(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}