package com.stephenelf.gemini2.presentation.gym_list

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.stephenelf.gemini2.domain.model.Gym
import kotlinx.coroutines.launch
import kotlin.math.abs

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GymListScreen(
    navController: NavController,
    viewModel: GymListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF1A1A1A))
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
            if (state.gyms.isNotEmpty()) {
                val gyms = state.gyms.toMutableList()

                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    gyms.reversed().forEachIndexed { index, gym->
                        GymCard(
                            gym = gym,
                            modifier = Modifier.offset(
                                x = if (index == gyms.size - 1) 0.dp else (index * 8).dp,
                                y = if (index == gyms.size - 1) 0.dp else (index * 8).dp
                            ),
                            onSwipe = { swipedRight ->
                                val message = if (swipedRight) "Liked ${gym.name}" else "Passed on ${gym.name}"
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                }
                                gyms.remove(gym)
                            },
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("gym", gym)
                                navController.navigate("gym_detail_screen/${gym.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GymCard(gym: Gym, modifier: Modifier = Modifier, onSwipe: (Boolean) -> Unit, onClick: () -> Unit) {
    var offsetX by remember { mutableStateOf(0f) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val swipeThreshold = screenWidth / 4

    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, animationSpec = tween(durationMillis = 300))
    val rotation by animateFloatAsState(targetValue = (offsetX / 60).coerceIn(-15f, 15f))

    Card(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        when {
                            offsetX > swipeThreshold.toPx() -> onSwipe(true) // Swiped Right
                            offsetX < -swipeThreshold.toPx() -> onSwipe(false) // Swiped Left
                        }
                        offsetX = 0f // Reset
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                    }
                )
            }
            .graphicsLayer(
                translationX = animatedOffsetX,
                rotationZ = rotation
            )
            .clip(MaterialTheme.shapes.medium),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GlideImage(
                model = "https://picsum.photos/800/1200?random=${gym.id}",
                contentDescription = "Gym Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 600f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = gym.name,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onClick)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = gym.address, color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Activities: ${gym.activities}", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
            Text(
                text = when {
                    rotation > 5 -> "LIKE"
                    rotation < -5 -> "NOPE"
                    else -> ""
                },
                color = if (rotation > 5) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center).graphicsLayer(alpha = (rotation.coerceIn(-15f, 15f) / 15f).let(::abs) )
            )
        }
    }
}