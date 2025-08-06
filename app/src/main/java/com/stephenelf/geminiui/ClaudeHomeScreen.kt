package com.stephenelf.geminiui



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stephenelf.geminiui.NavigationItem

data class CalendarDay(
    val day: Int,
    val dayOfWeek: String,
    val isSelected: Boolean = false,
    val isToday: Boolean = false
)

data class DoctorClaude(
    val name: String,
    val specialty: String,
    val rating: Float,
    val reviewCount: Int,
    val imageRes: Int = 0, // placeholder for image resource
    val isFavorite: Boolean = false
)

data class AppointmentClaude(
    val time: String,
    val doctor: String,
    val specialty: String,
    val type: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalAppScreen() {
    val calendarDays = listOf(
        CalendarDay(9, "MON"),
        CalendarDay(10, "TUE"),
        CalendarDay(11, "WED", isSelected = true, isToday = true),
        CalendarDay(12, "THU"),
        CalendarDay(13, "FRI"),
        CalendarDay(14, "SAT")
    )

    val todayAppointment = AppointmentClaude(
        time = "10 AM",
        doctor = "Dr. Olivia Turner, M.D.",
        specialty = "Dermato-Endocrinology",
        type = "Treatment and prevention of skin and photodermatitis..."
    )

    val doctors = listOf(
        DoctorClaude("Dr. Olivia Turner, M.D.", "Dermato-Endocrinology", 5.0f, 60, isFavorite = true),
        DoctorClaude("Dr. Alexander Bennett, Ph.D.", "Dermato-Genetics", 4.5f, 40),
        DoctorClaude("Dr. Sophia Martinez, Ph.D.", "Cosmetic Bioengineering", 5.0f, 150),
        DoctorClaude("Dr. Michael Davidson, M.D.", "Nano-Dermatology", 4.8f, 90, isFavorite = true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FF))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Hi, WelcomeBack",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "John Doe",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF8F9FF)
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Navigation Icons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NavigationItem(Icons.Default.Person, "Doctors", true)
                    NavigationItem(Icons.Default.Favorite, "Favorite", false)
                    NavigationItem(Icons.Default.Search, "", false)
                }
            }

            // Calendar
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(calendarDays) { day ->
                                CalendarDayItem(day)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Today's appointment
                        Text(
                            text = "11 Wednesday - Today",
                            fontSize = 14.sp,
                            color = Color(0xFF4A90E2),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AppointmentCard(todayAppointment)
                    }
                }
            }

            // Doctors List
            items(doctors) { doctor ->
                DoctorCard(doctor)
            }
        }

        // Bottom Navigation
        BottomNavigation()
    }
}

@Composable
fun NavigationItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (isSelected) Color(0xFF4A90E2) else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = if (isSelected) Color(0xFF4A90E2) else Color.Gray
            )
        }
    }
}

@Composable
fun CalendarDayItem(day: CalendarDay) {
    Card(
        modifier = Modifier
            .width(50.dp)
            .height(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (day.isSelected) Color(0xFF4A90E2) else Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.day.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (day.isSelected) Color.White else Color.Black
            )
            Text(
                text = day.dayOfWeek,
                fontSize = 10.sp,
                color = if (day.isSelected) Color.White else Color.Gray
            )
        }
    }
}

@Composable
fun AppointmentCard(appointment: AppointmentClaude) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = appointment.time,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.width(60.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = appointment.doctor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = appointment.type,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Row {
                Icon(
                    Icons.Default.Face,
                    contentDescription = "Video call",
                    tint = Color(0xFF4A90E2),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Message",
                    tint = Color(0xFF4A90E2),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: DoctorClaude) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Doctor Image Placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = doctor.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A90E2)
                )
                Text(
                    text = doctor.specialty,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = doctor.rating.toString(),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        Icons.Default.Email,
                        contentDescription = "Reviews",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = doctor.reviewCount.toString(),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Info",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    if (doctor.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (doctor.isFavorite) Color(0xFF4A90E2) else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavigation() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A90E2)),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                Icons.Default.Home,
                contentDescription = "Home",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                Icons.Default.Email,
                contentDescription = "Messages",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Calendar",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicalAppScreenPreview() {
    MaterialTheme {
        MedicalAppScreen()
    }
}