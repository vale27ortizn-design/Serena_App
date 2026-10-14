package com.serena.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.serena.R
import com.serena.ui.navigation.AppDestinations
import androidx.compose.ui.text.font.FontFamily
import com.serena.ui.theme.NunitoSans

private data class NavBarItem(
    val label: String,
    @DrawableRes val iconRes: Int,
    val route: String
)

private val navBarItems = listOf(
    NavBarItem(
        "Inicio",
        R.drawable.icon_inicio,
        AppDestinations.Inicio.route
    ),
    NavBarItem(
        "Diario",
        R.drawable.icon_diario,
        AppDestinations.Diario.route
    ),
    NavBarItem(
        "Chat",
        R.drawable.icon_chat,
        AppDestinations.Chat.route
    ),
    NavBarItem(
        "Perfil",
        R.drawable.icon_perfil,
        AppDestinations.Perfil.route
    )
)

@Composable
fun SerenaBottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = NunitoSans
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val activeColor = colorResource(R.color.serena_green_dark)
    val inactiveColor = colorResource(R.color.serena_text_gray)

    NavigationBar(
        modifier = modifier,
        containerColor = colorResource(R.color.white),
        tonalElevation = 8.dp,
        // Los insets del sistema ya los maneja MainActivity (Box con systemBarsPadding)
        windowInsets = WindowInsets(0.dp)
    ) {
        navBarItems.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) navController.navigateToTopLevel(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = null // el label ya describe el ítem
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = activeColor,
                    selectedTextColor = activeColor,
                    unselectedIconColor = inactiveColor,
                    unselectedTextColor = inactiveColor,
                    indicatorColor = colorResource(R.color.serena_green_light) // la píldora
                )
            )
        }
    }
}

private fun NavHostController.navigateToTopLevel(route: String) {
    navigate(route) {
        popUpTo(AppDestinations.Inicio.route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}