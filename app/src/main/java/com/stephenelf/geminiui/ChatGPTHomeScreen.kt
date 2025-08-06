package com.stephenelf.geminiui

import android.R.attr.fontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.tooling.preview.Preview

data class DoctorGPT(
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviews: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorScheduleScreen() {
    val doctors = remember {
        listOf(
            DoctorGPT("Dr. Olivia Turner, M.D.", "Dermato-Endocrinology", 5.0, 60),
            DoctorGPT("Dr. Alexander Bennett, Ph.D.", "Dermato-Genetics", 4.5, 40),
            DoctorGPT("Dr. Sophia Martinez, Ph.D.", "Cosmetic Bioengineering", 5.0, 150),
            DoctorGPT("Dr. Michael Davidson, M.D.", "Nano-Dermatology", 4.8, 90)
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {
        // Header
        TopAppBar(
            title = { Text("Hi, Welcome Back") },
            colors = TopAppBarDefaults.topAppBarColors( Color(0xFF1976D2),Color.White)
        )

        // Calendar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("9 MON", "10 TUE", "11 WED", "12 THU", "13 FRI", "14 SAT").forEach { date ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(date.split(" ")[0],fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(date.split(" ")[1], color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        // Today's Appointment
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("11 Wednesday - Today", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "10 AM: Dr. Olivia Turner, M.D.\nTreatment and prevention of skin and photodermatitis.",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        // Doctor List
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(doctors) { doctor ->
                DoctorCard(doctor)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: DoctorGPT) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1976D2))
            ) {
                // Add an Image placeholder here
                Text(
                    "D",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(doctor.specialty, color = Color.Gray, fontSize = 14.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${doctor.rating}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("${doctor.reviews} reviews", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDoctorScheduleScreen() {
    DoctorScheduleScreen()
}