package com.serena.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        if (!valida) sessionManager.clearSession()
        valida
    }

    NavHost(
        navController = navController,
        startDestination = if (sesionValida) AppDestinations.Inicio.route else AppDestinations.InicioSesion.route,
        modifier = modifier,
    ) {
        // Pantalla de Login
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
                onNavigateToChat = { navController.navigate(AppDestinations.Chat.route) },
                onNavigateToDiario = { navController.navigate(AppDestinations.Diario.route) },
                onNavigateToBotiquin = { navController.navigate(AppDestinations.Ejercicios.route) }
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

        // Pantalla de Diario (Muestra la lista)
        composable(AppDestinations.Diario.route) {
            DiarioScreen(
                onNewEntryClick = {
                    navController.navigate(AppDestinations.NuevoDiario.route)
                },
                onEntryClick = { diarioId ->
                    // Viaja al detalle pasando el ID exacto
                    navController.navigate("${AppDestinations.DetalleDiario.route}/$diarioId")
                }
            )
        }

        // Pantalla de Detalle de Diario (Lee una nota específica)
        composable(
            route = "${AppDestinations.DetalleDiario.route}/{diarioId}",
            arguments = listOf(navArgument("diarioId") { type = NavType.IntType })
        ) { backStackEntry ->
            val diarioId = backStackEntry.arguments?.getInt("diarioId") ?: -1
            DetalleDiarioScreen(
                diarioId = diarioId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate("editar_diario/$diarioId") }
            )
        }

        // Pantalla de Chat
        composable(AppDestinations.Chat.route) {
            ChatScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Pantalla de Nuevo Diario (Crear vacío)
        composable(AppDestinations.NuevoDiario.route) {
            NuevoDiarioScreen(
                diarioIdToEdit = null, // null porque es nuevo
                onBackClick = { navController.popBackStack() },
                onSaveClick = {
                    navController.navigate(AppDestinations.Diario.route) {
                        popUpTo(AppDestinations.Diario.route) { inclusive = true }
                    }
                }
            )
        }

        // NUEVA RUTA EXCLUSIVA PARA EDITAR (Reutiliza NuevoDiarioScreen)
        composable(
            route = "editar_diario/{diarioId}",
            arguments = listOf(navArgument("diarioId") { type = NavType.IntType })
        ) { backStackEntry ->
            val diarioId = backStackEntry.arguments?.getInt("diarioId") ?: -1
            NuevoDiarioScreen(
                diarioIdToEdit = diarioId, // Le pasamos el ID para que cargue los datos
                onBackClick = { navController.popBackStack() },
                onSaveClick = {
                    navController.popBackStack() // Regresa al detalle tras guardar
                }
            )
        }

        // Resto del botiquín...
        composable(AppDestinations.ERespiracion.route) { RespiracionScreen(onBackClick = { navController.popBackStack() }) }
        composable(AppDestinations.Ejercicios.route) { EjerciciosScreen(onBackClick = { navController.popBackStack() }, onNavigateToExercise = { ruta -> navController.navigate(ruta) }) }
        composable(AppDestinations.EAterrizaje.route) { AterrizajeScreen(onBackClick = { navController.popBackStack() }) }
        composable(AppDestinations.RefuerzoPositivo.route) { RefuerzoPositivoScreen(onBackClick = { navController.popBackStack() }) }
        composable(AppDestinations.ECatarsis.route) { CatarsisScreen(onBackClick = { navController.popBackStack() }) }
    }
}