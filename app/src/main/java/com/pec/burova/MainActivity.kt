package com.pec.burova

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pec.burova.ui.screens.*
import com.pec.burova.ui.theme.QRForStudentsTheme
import com.pec.burova.ui.viewmodels.StudentViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRForStudentsTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(viewModel) {
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                    composable("main") {
                        MainQRScreen(viewModel) {
                            navController.navigate("details")
                        }
                    }
                    composable("details") {
                        DetailScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onNavigateToMap = { navController.navigate("map") },
                            onNavigateToProfile = { navController.navigate("profile") }
                        )
                    }
                    composable("map") {
                        MapScreen(onBack = { navController.popBackStack() })
                    }
                    composable("profile") {
                        ProfileScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}