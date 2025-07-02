package com.stephenelf.gemini2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.stephenelf.gemini2.ui.theme.GeminiGeneratedApp2Theme

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