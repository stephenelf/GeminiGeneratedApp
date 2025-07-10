package com.stephenelf.hexagonal.presentation.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stephenelf.hexagonal.domain.model.Gym
import com.stephenelf.hexagonal.presentation.viewmodel.GymsViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun GymsScreen(viewModel: GymsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val swipedCardIds by viewModel.swipedCardIds

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.fetchGyms() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }

            state.error?.let {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            val gymsToShow = state.gyms.filter { it.id !in swipedCardIds }

            if (!state.isLoading && gymsToShow.isEmpty() && state.error == null) {
                Text("No more gyms to show!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            gymsToShow.forEachIndexed { index, gym ->
                SwipeableGymCard(
                    gym = gym,
                    onSwipe = { viewModel.onCardSwiped(gym.id) },
                    // Only the top card is interactive
                    isTopCard = index == gymsToShow.size - 1
                )
            }
        }
    }
}

@Composable
fun SwipeableGymCard(gym: Gym, onSwipe: () -> Unit, isTopCard: Boolean) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.value
    val swipeThreshold = screenWidth * 0.5f

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 300),
        label = "offsetXAnimation"
    )

    val rotationZ = (animatedOffsetX / screenWidth) * 20f
    val cardAlpha = 1f - (abs(animatedOffsetX) / screenWidth) * 0.5f

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .graphicsLayer(
                translationX = animatedOffsetX,
                rotationZ = rotationZ,
                alpha = cardAlpha
            )
            .pointerInput(isTopCard) {
                if (isTopCard) {
                    detectDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (abs(offsetX) > swipeThreshold) {
                                    // Animate out of screen
                                    offsetX = if (offsetX > 0) screenWidth * 2 else -screenWidth * 2
                                    onSwipe()
                                } else {
                                    // Animate back to center
                                    offsetX = 0f
                                }
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                    }
                }
            }
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = gym.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = gym.facilityType,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoRow(icon = Icons.Default.LocationOn, text = gym.address)
                gym.phone?.let {
                    InfoRow(icon = Icons.Default.Phone, text = it)
                }
                InfoRow(icon = Icons.Default.Info, text = "Lat: ${gym.lat}, Lon: ${gym.lon}")
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
