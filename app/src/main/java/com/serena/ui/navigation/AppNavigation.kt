package com.serena.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.serena.ui.screens.AterrizajeScreen
import com.serena.ui.screens.CatarsisScreen
import com.serena.ui.screens.ChatScreen
import com.serena.ui.screens.DetalleDiarioScreen
import com.serena.ui.screens.DiarioScreen
import com.serena.ui.screens.EjerciciosScreen
import com.serena.ui.screens.InicioScreen
import com.serena.ui.screens.LoginScreen
import com.serena.ui.screens.NuevoDiarioScreen
import com.serena.ui.screens.PerfilScreen
import com.serena.ui.screens.RefuerzoPositivoScreen
import com.serena.ui.screens.RegisterScreen
import com.serena.ui.screens.RespiracionScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.InicioSesion.route,
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
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Inicio (Agregadas todas las lambdas que requiere InicioScreen)
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
                        popUpTo(AppDestinations.InicioSesion.route) { inclusive = true }
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
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = {
                    // Acción de edición
                },
                onDeleteClick = {
                    navController.popBackStack()
                },
                onShareClick = {
                    // Acción de compartir
                }
            )
        }

        // Pantalla de Chat
        composable(AppDestinations.Chat.route) {
            ChatScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Nuevo Diario
        composable(AppDestinations.NuevoDiario.route) {
            NuevoDiarioScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNotificationClick = {
                    // Notificaciones
                },
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
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Menú principal del Botiquín Emocional
        composable(AppDestinations.Ejercicios.route) {
            EjerciciosScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToExercise = { rutaEjercicio ->
                    navController.navigate(rutaEjercicio)
                }
            )
        }

        // Pantalla de Aterrizaje 5-4-3-2-1
        composable(AppDestinations.EAterrizaje.route) {
            AterrizajeScreen(
                onBackClick = {
                    navController.popBackStack()
                }
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