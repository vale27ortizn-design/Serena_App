package com.serena.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.serena.ui.screens.LoginScreen
import com.serena.ui.screens.RegisterScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier,
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("menu-inicio")
                },
                onNavigateToRegister = {
                    navController.navigate("registro")
                }
            )
        }

        composable("registro") {
            RegisterScreen (
                onRegisterSuccess = {
                    navController.popBackStack() // al login tras guardar
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("menu-inicio") {
            // Inicio()
        }
    }
}
