package com.serena.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.serena.ui.components.SerenaTab
import com.serena.ui.screens.ChatScreen
import com.serena.ui.screens.DetalleDiarioScreen
import com.serena.ui.screens.DiarioScreen
import com.serena.ui.screens.InicioScreen
import com.serena.ui.screens.LoginScreen
import com.serena.ui.screens.NuevoDiarioScreen
import com.serena.ui.screens.PerfilScreen
import com.serena.ui.screens.RegisterScreen
import com.serena.ui.screens.RespiracionScreen

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
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("menu-inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("registro")
                }
            )
        }

        // Pantalla de Registro
        composable("registro") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Inicio
        composable("menu-inicio") {
            InicioScreen(
                onNavigateToTab = { tab ->
                    when (tab) {
                        SerenaTab.INICIO -> {}
                        SerenaTab.PERFIL -> {
                            navController.navigate("perfil")
                        }
                        SerenaTab.DIARIO -> {
                            navController.navigate("diario")
                        }
                        SerenaTab.CHAT -> {
                            navController.navigate("chat") // <-- DESCOMENTADO AQUÍ
                        }
                    }
                },
                onStartBreathingClick = {
                    navController.navigate("respiracion")
                }
            )
        }

        // Pantalla de Perfil
        composable("perfil") {
            PerfilScreen(
                onNavigateToTab = { tab ->
                    when (tab) {
                        SerenaTab.INICIO -> {
                            navController.navigate("menu-inicio")
                        }
                        SerenaTab.DIARIO -> {
                            navController.navigate("diario")
                        }
                        SerenaTab.CHAT -> {
                            navController.navigate("chat") // <-- DESCOMENTADO AQUÍ
                        }
                        SerenaTab.PERFIL -> {}
                    }
                },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Diario
        composable("diario") {
            DiarioScreen(
                onNavigateToTab = { tab ->
                    when (tab) {
                        SerenaTab.INICIO -> {
                            navController.navigate("menu-inicio")
                        }
                        SerenaTab.DIARIO -> {}
                        SerenaTab.CHAT -> {
                            navController.navigate("chat") // <-- DESCOMENTADO AQUÍ
                        }
                        SerenaTab.PERFIL -> {
                            navController.navigate("perfil")
                        }
                    }
                },
                onNewEntryClick = {
                    navController.navigate("nuevo-diario")
                },
                onEntryClick = {
                    navController.navigate("detalle-diario")
                }
            )
        }

        // Pantalla de Detalle de Diario
        composable("detalle-diario") {
            DetalleDiarioScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = {
                    // Aquí irá la navegación a la pantalla de edición
                },
                onDeleteClick = {
                    navController.popBackStack()
                },
                onShareClick = {
                    // Lógica para abrir el menú de compartir
                }
            )
        }

        // Pantalla de Chat
        composable("chat") {
            ChatScreen (
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Nuevo Diario
        composable("nuevo-diario") {
            NuevoDiarioScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNotificationClick = {
                    // notis
                },
                onSaveClick = {
                    navController.navigate("diario") { popUpTo("diario") { inclusive = true } }
                }
            )
        }

        // Pantalla de Respiración
        composable("respiracion") {
            RespiracionScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}