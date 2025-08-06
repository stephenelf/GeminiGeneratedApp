package com.stephenelf.geminiui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreenDeepseek() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Header section
        HeaderSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Favorite doctors title
        Text(
            text = "Doctors  Favorite",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Date row
        DateRow()

        Spacer(modifier = Modifier.height(16.dp))

        // Today's appointment section
        TodayAppointmentSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Doctors list
        DoctorsList()
    }
}

@Composable
fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hi, Welcome Back",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Text(
                text = "John Doe",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Profile image placeholder
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            // You would replace this with your actual image
            Image(
                painter = painterResource(id = R.drawable.doctor1),
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun DateRow() {
    val dates = listOf(
        "9 MON", "10 TUE", "11 WED", "12 THU", "13 FRI", "14 SAT"
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(dates) { date ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(60.dp)
                    .background(
                        color = if (date == "11 WED") Color(0xFF6200EE) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = date,
                    color = if (date == "11 WED") Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TodayAppointmentSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "9 AM ...... 11 Wednesday - Today ......",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "10 AM   Dr. Olivia Turner, M.D.",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "11 AM   Treatment and prevention of skin and photodermatitis.",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "12 AM ......",
                color = Color.Gray
            )
        }
    }
}

@Composable
fun DoctorsList() {
    val doctors = listOf(
        DoctorDeepseek("Dr. Olivia Turner, M.D.", "Dermato-Endocrinology", 5f, 60),
        DoctorDeepseek("Dr. Alexander Bennett, Ph.D.", "Dermato-Genetics", 4.5f, 40),
        DoctorDeepseek("Dr. Sophia Martinez, Ph.D.", "Cosmetic Bioengineering", 5f, 150),
        DoctorDeepseek("Dr. Michael Davidson, M.D.", "Nano-Dermatology", 4.8f, 90)
    )

    Column {
        doctors.forEach { doctor ->
            DoctorCard(doctor = doctor)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DoctorCard(doctor: DoctorDeepseek) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = doctor.specialization,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = doctor.rating.toString(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${doctor.patients}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

data class DoctorDeepseek(
    val name: String,
    val specialization: String,
    val rating: Float,
    val patients: Int
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenDeepseek()
}