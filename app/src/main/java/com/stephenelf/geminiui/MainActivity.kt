package com.stephenelf.geminiui

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stephenelf.geminiui.ui.theme.DoctorAppointmentTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is a workaround to get a drawable resource ID.
        // In a real app, you would use a proper resource management system.
        R.drawable.ic_launcher_background
        setContent {
            DoctorAppointmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = { BottomNavigationBar(navController) }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavigationGraph(navController)
                        }
                    }
                }
            }
        }
    }
}

// Data class for Doctor
data class Doctor(
    val name: String,
    val specialty: String,
    val image: Int,
    val rating: Double,
    val reviews: Int
)

// Sample data
val doctors = listOf(
    Doctor("Dr. Olivia Turner, M.D.", "Dermato-Endocrinology", R.drawable.doctor1, 4.5, 30),
    Doctor("Dr. Alexander Bennett, Ph.D.", "Dermato-Genetics", R.drawable.doctor2, 5.0, 40),
    Doctor("Dr. Michael Davidson, M.D.", "Solar Dermatology", R.drawable.doctor3, 4.8, 90),
    Doctor("Dr. Sophia Martinez, Ph.D.", "Cosmetic Bioengineering", R.drawable.doctor4, 5.0, 150)
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Doctors,
        NavigationItem.Profile,
        NavigationItem.Schedule
    )
    NavigationBar(
        containerColor = Color(0xFF4A80F0)
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title, tint = Color.White) },
                label = { Text(item.title, color = Color.White) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

sealed class NavigationItem(var route: String, var icon: androidx.compose.ui.graphics.vector.ImageVector, var title: String) {
    object Home : NavigationItem("home", Icons.Default.Home, "Home")
    object Doctors : NavigationItem("doctors", Icons.Default.List, "Doctors")
    object Profile : NavigationItem("profile", Icons.Default.Person, "Profile")
    object Schedule : NavigationItem("schedule", Icons.Default.DateRange, "Schedule")
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(NavigationItem.Doctors.route) {
            DoctorsScreen(navController)
        }
        composable(NavigationItem.Profile.route) {
            DoctorProfileScreen(navController, doctors[1])
        }
        composable(NavigationItem.Schedule.route) {
            DoctorProfileScreen2(navController, doctors[0])
        }
        composable("doctorDetail/{doctorName}") { backStackEntry ->
            val doctorName = backStackEntry.arguments?.getString("doctorName")
            val doctor = doctors.find { it.name == doctorName }
            doctor?.let {
                DoctorProfileScreen(navController, it)
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Header()
            Spacer(modifier = Modifier.height(20.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(20.dp))
            CategoryTabs()
            Spacer(modifier = Modifier.height(20.dp))
            AppointmentCard()
            Spacer(modifier = Modifier.height(20.dp))
            Text("Top Doctors", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(doctors) { doctor ->
            DoctorCard(doctor = doctor, navController = navController)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.doctor2),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Hi, WelcomeBack", style = MaterialTheme.typography.bodyMedium)
                Text("John Doe", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
        Row {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            Spacer(modifier = Modifier.width(10.dp))
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text("Search") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray,
            disabledContainerColor = Color.LightGray,
        )
    )
}

@Composable
fun CategoryTabs() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        CategoryTab("Doctors", Icons.Default.List, true)
        CategoryTab("Favorite", Icons.Default.Star, false)
    }
}

@Composable
fun CategoryTab(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = title, tint = if (isSelected) Color(0xFF4A80F0) else Color.Gray)
        Text(title, color = if (isSelected) Color(0xFF4A80F0) else Color.Gray)
    }
}

@Composable
fun AppointmentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("11 Wednesday - Today", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("10 AM", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Dr. Olivia Turner, M.D.", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    Text("Treatment and prevention of skin and photodermatitis.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("doctorDetail/${doctor.name}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = doctor.image),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(doctor.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                Text(doctor.specialty, style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.Yellow)
                    Text("${doctor.rating}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(Icons.Default.Person, contentDescription = "Reviews", tint = Color.Gray)
                    Text("${doctor.reviews}", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.Gray)
        }
    }
}

@Composable
fun DoctorsScreen(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Doctors", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(bottom = 16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(doctors) { doctor ->
                DoctorListItem(doctor = doctor, navController = navController)
            }
        }
    }
}

@Composable
fun DoctorListItem(doctor: Doctor, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("doctorDetail/${doctor.name}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = doctor.image),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(doctor.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                Text(doctor.specialty, style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A80F0))) {
                    Text("Info")
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.DateRange, contentDescription = "Calendar")
                Icon(Icons.Default.Info, contentDescription = "Info")
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
            }
        }
    }
}

@Composable
fun DoctorProfileScreen(navController: NavController, doctor: Doctor) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            DoctorHeader(doctor)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Profile", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Career Path", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Highlights", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun DoctorHeader(doctor: Doctor) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = doctor.image),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(doctor.name, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
            Text(doctor.specialty, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.Yellow)
                Text("${doctor.rating}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.Person, contentDescription = "Reviews", tint = Color.Gray)
                Text("${doctor.reviews} reviews", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun DoctorProfileScreen2(navController: NavController, doctor: Doctor) {
    Column(modifier = Modifier.padding(16.dp)) {
        DoctorHeader(doctor)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Profile", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
        Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        CalendarView()
    }
}

@Composable
fun CalendarView() {
    val calendar = Calendar.getInstance()
    val days = (1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)).toList()
    val today = calendar.get(Calendar.DAY_OF_MONTH)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
            Text("MONTH", style = MaterialTheme.typography.headlineSmall)
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach {
                Text(it, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(days) { day ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (day == today) Color(0xFF4A80F0) else Color.LightGray)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = if (day == today) Color.White else Color.Black
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DoctorAppointmentTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(navController)
            }
        }
    }
}
