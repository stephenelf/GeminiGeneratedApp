package com.stephenelf.geminigeneratedapp.presentation.gyms

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stephenelf.geminigeneratedapp.domain.model.Gym
import kotlinx.coroutines.launch
import kotlin.collections.asReversed
import kotlin.math.abs


@Composable
fun GymsScreen(viewModel: GymsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val gyms = viewModel.gymsList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }

        if (!uiState.isLoading && gyms.isEmpty()) {
            Text("No more gyms found!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        } else {
            SwipeableCardStack(
                items = gyms,
                onSwipe = { viewModel.swipeCard() }
            )
        }
    }
}

@Composable
fun SwipeableCardStack(
    items: List<Gym>,
    onSwipe: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        contentAlignment = Alignment.Center
    ) {
        items.asReversed().forEachIndexed { index, gym ->
            val isTopCard = index == items.size - 1
            SwipeableGymCard(
                gym = gym,
                isTopCard = isTopCard,
                onSwipe = {
                    scope.launch {
                        onSwipe()
                    }
                }
            )
        }
    }
}

@Composable
fun SwipeableGymCard(
    gym: Gym,
    isTopCard: Boolean,
    onSwipe: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 300)
    )

    val rotation by derivedStateOf { (animatedOffsetX / 50).coerceIn(-15f, 15f) }
    val swipeThreshold = 300f

    val cardModifier = if (isTopCard) {
        Modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                    },
                    onDragEnd = {
                        if (abs(offsetX) > swipeThreshold) {
                            onSwipe()
                        }
                        // Animate back to center if not swiped
                        offsetX = 0f
                    }
                )
            }
    } else {
        Modifier
    }

    Card(
        modifier = cardModifier
            .fillMaxWidth()
            .height(500.dp)
            .graphicsLayer(
                translationX = animatedOffsetX,
                rotationZ = rotation
            )
            .clip(MaterialTheme.shapes.large),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = gym.location,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = gym.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = gym.type,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = gym.address,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = gym.phone, style = MaterialTheme.typography.bodyMedium)
                }

                Text(
                    text = gym.status.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (gym.status.equals("open", ignoreCase = true)) Color(0xFF008000) else Color.Red
                )
            }
        }
    }
}