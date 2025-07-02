package com.stephenelf.gemini2.presentation.gym_list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stephenelf.gemini2.domain.model.Gym
import kotlinx.coroutines.launch

@Composable
fun GymListScreen(
    navController: NavController,
    viewModel: GymListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) { padding ->
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
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
            if (state.gyms.isNotEmpty()) {
                val gyms = remember { mutableStateListOf(*state.gyms.toTypedArray()) }

                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    gyms.reversed().forEachIndexed { index, gym ->
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
    val swipeThreshold = screenWidth / 3

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
        elevation = 8.dp
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