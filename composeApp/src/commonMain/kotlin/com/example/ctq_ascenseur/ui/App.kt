package com.example.ctq_ascenseur.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ctq_ascenseur.ui.screens.HomeScreen
import com.example.ctq_ascenseur.ui.screens.SettingsScreen
import com.example.ctq_ascenseur.ui.theme.CTQAscenseurTheme
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        CTQAscenseurTheme {
            val navController = rememberNavController()
            
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        onNavigateToSettings = { navController.navigate("settings") },
                        onStartNewReport = { /* Navigate to Form */ },
                        onViewReport = { id -> /* Navigate to Details */ }
                    )
                }
                composable("settings") {
                    SettingsScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
