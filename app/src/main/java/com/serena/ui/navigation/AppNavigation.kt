package com.serena.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.serena.data.preferences.SessionManager
import com.serena.ui.screens.*
import com.serena.data.local.DatabaseHelper

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val sesionValida = remember {
        val valida = sessionManager.isLogged() &&
                DatabaseHelper(context).existeUsuario(sessionManager.getUserId())
        if (!valida) sessionManager.clearSession()   // limpia sesión
        valida
    }

    NavHost(
        navController = navController,
        startDestination = if (sesionValida) {
            AppDestinations.Inicio.route
        } else {
            AppDestinations.InicioSesion.route
        },
        modifier = modifier,
    ) {  // Pantalla de Login
        composable(AppDestinations.InicioSesion.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.Inicio.route) {
                        popUpTo(AppDestinations.InicioSesion.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppDestinations.Registro.route)
                }
            )
        }

        // Pantalla de Registro
        composable(AppDestinations.Registro.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // Pantalla de Inicio
        composable(AppDestinations.Inicio.route) {
            InicioScreen(
                onNavigateToChat = {
                    navController.navigate(AppDestinations.Chat.route)
                },
                onNavigateToDiario = {
                    navController.navigate(AppDestinations.Diario.route)
                },
                onNavigateToBotiquin = {
                    navController.navigate(AppDestinations.Ejercicios.route)
                }
            )
        }

        // Pantalla de Perfil
        composable(AppDestinations.Perfil.route) {
            PerfilScreen(
                onLogoutClick = {
                    navController.navigate(AppDestinations.InicioSesion.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Diario
        composable(AppDestinations.Diario.route) {
            DiarioScreen(
                onNewEntryClick = {
                    navController.navigate(AppDestinations.NuevoDiario.route)
                },
                onEntryClick = {
                    navController.navigate(AppDestinations.DetalleDiario.route)
                }
            )
        }

        // Pantalla de Detalle de Diario
        composable(AppDestinations.DetalleDiario.route) {
            DetalleDiarioScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Pantalla de Chat
        composable(AppDestinations.Chat.route) {
            ChatScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Pantalla de Nuevo Diario
        composable(AppDestinations.NuevoDiario.route) {
            NuevoDiarioScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = {
                    navController.navigate(AppDestinations.Diario.route) {
                        popUpTo(AppDestinations.Diario.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Respiración
        composable(AppDestinations.ERespiracion.route) {
            RespiracionScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Menú principal del Botiquín Emocional
        composable(AppDestinations.Ejercicios.route) {
            EjerciciosScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToExercise = { rutaEjercicio ->
                    navController.navigate(rutaEjercicio)
                }
            )
        }

        // Pantalla de Aterrizaje 5-4-3-2-1
        composable(AppDestinations.EAterrizaje.route) {
            AterrizajeScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Pantalla de Refuerzo Positivo
        composable(AppDestinations.RefuerzoPositivo.route) {
            RefuerzoPositivoScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Pantalla de Catarsis Simbólica
        composable(AppDestinations.ECatarsis.route) {
            CatarsisScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}