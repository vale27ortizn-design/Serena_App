package com.serena.ui.navigation

sealed class AppDestinations(
    val route: String
) {
    data object Registro: AppDestinations("registro")
    data object InicioSesion: AppDestinations("inicioSesion")

    data object Inicio: AppDestinations("inicio")


    data object Ejercicios: AppDestinations("ejercicios")
    data object ERespiracion: AppDestinations("eRespiracion")
    data object EAterrizaje: AppDestinations("eAterrizaje")
    data object ECatarsis: AppDestinations("eCatarsis")
    data object RefuerzoPositivo: AppDestinations("refuerzoPositivo")

    data object Perfil: AppDestinations("perfil")

    data object Chat: AppDestinations("chat")

    data object Diario: AppDestinations("diario")
    data object DetalleDiario: AppDestinations("detalleDiario")
    data object NuevoDiario: AppDestinations("nuevoDiario")
}