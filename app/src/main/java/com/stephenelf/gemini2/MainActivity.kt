package com.stephenelf.gemini2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stephenelf.gemini2.presentation.gym_detail.GymDetailScreen
import com.stephenelf.gemini2.presentation.gym_list.GymListScreen
import com.stephenelf.gemini2.ui.theme.GeminiGeneratedApp2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiGeneratedApp2Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "gym_list_screen"
                    ) {
                        composable("gym_list_screen") {
                            GymListScreen(navController = navController)
                        }
                        composable("gym_detail_screen/{gymId}") {
                            // In a real app, you'd pass the gym object or ID
                            // and retrieve details. For simplicity, we'll pass some data.
                            GymDetailScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}