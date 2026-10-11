package com.serena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.serena.ui.components.SerenaBottomNavBar
import com.serena.ui.navigation.AppDestinations
import com.serena.ui.navigation.AppNavigation
import com.serena.ui.screens.LoginScreen
import com.serena.ui.theme.SerenaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SerenaAppTheme {
                val navController = rememberNavController()
                val currentDestination by navController.currentBackStackEntryAsState()
                val currentRoute = currentDestination?.destination?.route

                // rutas donde el bottomNavBar no se ven
                val hideBottomBarRoutes = listOf(
                    AppDestinations.InicioSesion.route,
                    AppDestinations.Registro.route,
                    AppDestinations.Chat.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute !in hideBottomBarRoutes) {
                            SerenaBottomNavBar(
                                navController = navController
                            )
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SerenaAppTheme {
        LoginScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingFullScreenPreview() {
    SerenaAppTheme {
        LoginScreen(
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}