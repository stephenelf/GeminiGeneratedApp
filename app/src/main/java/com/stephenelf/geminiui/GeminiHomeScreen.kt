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
import com.stephenelf.geminiui.ui.theme.DoctorAppointmentTheme

// Data classes to hold information for our UI elements
data class Doctor2(
    val name: String,
    val specialty: String,
    val rating: Float,
    val reviews: Int,
    val imageResId: Int
)

data class Appointment(
    val doctorName: String,
    val description: String,
    val time: String
)

data class DateModel(
    val dayOfWeek: String,
    val dayOfMonth: String,
    val isSelected: Boolean = false
)

@Composable
fun HomeScreen2(userImageResId: Int) {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F4FF))
        ) {
            item { TopBar(userImageResId) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { SearchAndFilter() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { CalendarView2() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { AppointmentCard(
                Appointment(
                    doctorName = "Dr. Olivia Turner, M.D.",
                    description = "Treatment and prevention of skin and photodermatitis.",
                    time = "11 AM"
                )
            ) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { DoctorList() }
        }
    }
}

@Composable
fun TopBar(userImageResId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = userImageResId),
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Hi, WelcomeBack", fontSize = 14.sp, color = Color.Gray)
                Text(text = "John Doe", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row {
            IconButton(onClick = { /* TODO: Notification action */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Gray)
            }
            IconButton(onClick = { /* TODO: Settings action */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun SearchAndFilter() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            TabButton(text = "Doctors", icon = Icons.Default.Home, isSelected = true)
            Spacer(modifier = Modifier.width(16.dp))
            TabButton(text = "Favorite", icon = Icons.Default.Favorite, isSelected = false)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun TabButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean) {
    val backgroundColor = if (isSelected) Color(0xFFE0E7FF) else Color.Transparent
    val contentColor = if (isSelected) Color(0xFF4F46E5) else Color.Gray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = text, tint = contentColor)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = contentColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun CalendarView2() {
    val dates = listOf(
        DateModel("MON", "9"),
        DateModel("TUE", "10"),
        DateModel("WED", "11", isSelected = true),
        DateModel("THU", "12"),
        DateModel("FRI", "13"),
        DateModel("SAT", "14")
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dates) { date ->
            DateCard(date)
        }
    }
}

@Composable
fun DateCard(date: DateModel) {
    val backgroundColor = if (date.isSelected) Color(0xFF4F46E5) else Color.White
    val textColor = if (date.isSelected) Color.White else Color.Black

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if(date.isSelected) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = date.dayOfMonth, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(text = date.dayOfWeek, fontSize = 14.sp, color = textColor)
        }
    }
}


@Composable
fun AppointmentCard(appointment: Appointment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E7FF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("11 Wednesday - Today", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(appointment.time, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Divider(modifier = Modifier
                    .height(40.dp)
                    .width(1.dp), color = Color.LightGray)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(appointment.doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(appointment.description, fontSize = 12.sp, color = Color.Gray)
                }
                Row {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Confirm", tint = Color(0xFF4F46E5))
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun DoctorList() {
    val doctors = listOf(
        Doctor2("Dr. Olivia Turner, M.D.", "Dermato-Endocrinology", 5.0f, 60, R.drawable.doctor1),
        Doctor2("Dr. Alexander Bennett, Ph.D.", "Dermato-Genetics", 4.5f, 40, R.drawable.doctor2),
        Doctor2("Dr. Sophia Martinez, Ph.D.", "Cosmetic Bioengineering", 5.0f, 150, R.drawable.doctor3),
        Doctor2("Dr. Michael Davidson, M.D.", "Nano-Dermatology", 4.8f, 90, R.drawable.doctor4)
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        doctors.forEach { doctor ->
            DoctorCard(doctor)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor2) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = doctor.imageResId),
                contentDescription = "Doctor Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(doctor.specialty, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                    Text("${doctor.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.AccountCircle, contentDescription = "Reviews", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Text("${doctor.reviews}", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Call, contentDescription = "More Info", tint = Color.Gray)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.Gray)
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color(0xFF4F46E5),
        contentColor = Color.White,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = true,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Phone, contentDescription = "Chat") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Calendar") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            )
        )
    }
}

// Note: You will need to add placeholder drawable resources for this preview to work.
// Create drawable resources named: user_placeholder, doctor1_placeholder, etc.
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    DoctorAppointmentTheme {
        // A placeholder resource ID. In a real app, this would come from R.drawable
        val placeholderResId = 1
        HomeScreen2(R.drawable.doctor1)
    }
}