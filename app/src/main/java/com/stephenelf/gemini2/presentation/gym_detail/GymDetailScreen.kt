package com.stephenelf.gemini2.presentation.gym_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stephenelf.gemini2.domain.model.Gym

@Composable
fun GymDetailScreen(
    navController: NavController
) {
    val gym = navController.previousBackStackEntry?.savedStateHandle?.get<Gym>("gym")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = gym?.name ?: "Gym Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF2C2C2C))
                .padding(16.dp)
        ) {
            if (gym != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    DetailItem("Name", gym.name)
                    DetailItem("Address", gym.address)
                    DetailItem("Phone", gym.phone)
                    DetailItem("Activities", gym.activities)
                    DetailItem("Schedule", gym.scheduleUrl, isLink = true)
                }
            } else {
                Text("Gym details not available.", color = Color.White)
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        if(isLink) {
            // In a real app, you would make this a clickable link
            Text(text = value, fontSize = 18.sp, color = Color.Cyan)
        } else {
            Text(text = value, fontSize = 18.sp, color = Color.White)
        }
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
    }
}