package com.stephenelf.geminimvi.ui.gyms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stephenelf.geminimvi.data.Gym
import kotlinx.coroutines.launch
import kotlin.collections.asReversed
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun GymsScreen(viewModel: GymsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error!!)
            }
        } else {
            SwipableGymCards(
                gyms = state.gyms,
                onSwipe = { gym ->
                    viewModel.handleEvent(GymsEvent.SwipeGym(gym))
                }
            )
        }
    }
}

@Composable
fun SwipableGymCards(
    gyms: List<Gym>,
    onSwipe: (Gym) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        gyms.asReversed().forEachIndexed { index, gym ->
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
            val rotationZ by animateFloatAsState(targetValue = (offsetX / 60).coerceIn(-15f, 15f))

            Card(
                modifier = Modifier
                    .offset(x = offsetX.dp, y = offsetY.dp)
                    .pointerInput(gym) {
                        detectDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    if (offsetX > screenWidth.toPx() / 4 || offsetX < -screenWidth.toPx() / 4) {
                                        onSwipe(gym)
                                    }
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .graphicsLayer(
                        rotationZ = rotationZ
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                GymCardContent(gym = gym)
            }
        }
    }
}

@Composable
fun GymCardContent(gym: Gym) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = gym.name ?: "No Name", style = MaterialTheme.typography.headlineMedium)
        Text(text = gym.address ?: "No Address", style = MaterialTheme.typography.bodyLarge)
        Text(text = gym.phone ?: "No Phone", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Type: ${gym.type ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
    }
}
