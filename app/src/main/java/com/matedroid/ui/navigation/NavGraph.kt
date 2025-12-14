package com.matedroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matedroid.ui.screens.settings.SettingsScreen

sealed class Screen(val route: String) {
    data object Settings : Screen("settings")
    data object Dashboard : Screen("dashboard")
    data object Charges : Screen("charges")
    data object ChargeDetail : Screen("charges/{chargeId}") {
        fun createRoute(chargeId: Int) = "charges/$chargeId"
    }
    data object Drives : Screen("drives")
    data object DriveDetail : Screen("drives/{driveId}") {
        fun createRoute(driveId: Int) = "drives/$driveId"
    }
    data object Battery : Screen("battery")
    data object Updates : Screen("updates")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Settings.route
    ) {
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Settings.route) { inclusive = true }
                    }
                }
            )
        }

        // Dashboard and other screens will be added in subsequent phases
    }
}
