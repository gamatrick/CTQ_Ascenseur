package com.example.ctq_ascenseur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ctq_ascenseur.data.AppDatabase
import com.example.ctq_ascenseur.ui.MainViewModel
import com.example.ctq_ascenseur.ui.MainViewModelFactory
import com.example.ctq_ascenseur.ui.screens.HomeScreen
import com.example.ctq_ascenseur.ui.screens.InspectionFormScreen
import com.example.ctq_ascenseur.ui.theme.CTQAscenseurTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(this)
        val dao = database.appDao()
        
        setContent {
            CTQAscenseurTheme {
                CTQApp(dao)
            }
        }
    }
}

@Composable
fun CTQApp(dao: com.example.ctq_ascenseur.data.dao.AppDao) {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(dao))

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onStartInspection = { inspectionId ->
                    navController.navigate("inspection/$inspectionId")
                }
            )
        }
        composable(
            route = "inspection/{inspectionId}",
            arguments = listOf(navArgument("inspectionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val inspectionId = backStackEntry.arguments?.getString("inspectionId") ?: return@composable
            InspectionFormScreen(
                inspectionId = inspectionId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
