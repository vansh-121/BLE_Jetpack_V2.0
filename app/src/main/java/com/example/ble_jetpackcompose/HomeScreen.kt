package com.example.ble_jetpackcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.animation.core.*
import androidx.compose.material.icons.filled.Settings
import kotlin.io.encoding.Base64

object AppColors {
    val Background = Color(0xFFFAFAFA)
    val CardBackground = Color.White
    val ShimmerHighlight = Color(0xFFF5F5F5)
    val ShimmerBackground = Color(0xFFEEEEEE)
    val Divider = Color(0xFFF0F0F0)
}

@Composable
fun MainScreen() {
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        // Modern Top Bar
        TopAppBar(
            backgroundColor = AppColors.CardBackground,
            contentColor = Color.Black,
            elevation = 0.dp // Modern apps often use 0 elevation
        ) {
            Text(
                text = "BLE Sense",
                fontFamily = helveticaFont,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Main Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Nearby Devices Section
                DevicesSection(
                    title = "Nearby devices",
                    isLoading = isLoading,
                    itemCount = 4
                )
            }

            item {
                // Game Devices Section
                DevicesSection(
                    title = "Game Devices",
                    isLoading = isLoading,
                    itemCount = 3,
                    showMoreOptions = true
                )
            }
        }

        // Bottom Navigation
        BottomNavigation(
            backgroundColor = AppColors.CardBackground,
            elevation = 16.dp
        ) {
            BottomNavigationItem(
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.bluetooth),
                        contentDescription = "Bluetooth",
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = true,
                onClick = { },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = Color.Gray
            )
            BottomNavigationItem(
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.gamepad),
                        contentDescription = "Gameplay",
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = false,
                onClick = { },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = Color.Gray
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp)
                    )

                },
                selected = false,
                onClick = { },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@Composable
fun DevicesSection(
    title: String,
    isLoading: Boolean,
    itemCount: Int,
    showMoreOptions: Boolean = false
) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = helveticaFont,
                    fontWeight = FontWeight.Bold
                )
                if (showMoreOptions) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        modifier = Modifier.clickable { },
                        tint = Color.Gray
                    )
                }

            }

            repeat(itemCount) { index ->
                if (isLoading) {
                    ShimmerDeviceItem(isLast = index == itemCount - 1)
                } else {
                    DeviceItem(isLast = index == itemCount - 1)
                }
            }
        }
    }
}

@Composable
fun ShimmerDeviceItem(isLast: Boolean) {
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shimmer for device icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        shimmerBrush(
                            targetValue = translateAnim.value,
                            showShimmer = true
                        )
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                // Shimmer for title
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            shimmerBrush(
                                targetValue = translateAnim.value,
                                showShimmer = true
                            )
                        )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Shimmer for subtitle
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            shimmerBrush(
                                targetValue = translateAnim.value,
                                showShimmer = true
                            )
                        )
                )
            }
        }

        if (!isLast) {
            Divider(
                color = AppColors.Divider,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun DeviceItem(isLast: Boolean) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Device Name",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Connected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        if (!isLast) {
            Divider(
                color = AppColors.Divider,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

fun shimmerBrush(targetValue: Float, showShimmer: Boolean): androidx.compose.ui.graphics.Brush {
    return if (showShimmer) {
        androidx.compose.ui.graphics.Brush.linearGradient(
            colors = listOf(
                AppColors.ShimmerBackground,
                AppColors.ShimmerHighlight,
                AppColors.ShimmerBackground,
            ),
            start = androidx.compose.ui.geometry.Offset(targetValue - 400f, targetValue - 400f),
            end = androidx.compose.ui.geometry.Offset(targetValue, targetValue)
        )
    } else {
        androidx.compose.ui.graphics.Brush.linearGradient(
            colors = listOf(AppColors.ShimmerBackground, AppColors.ShimmerBackground),
            start = androidx.compose.ui.geometry.Offset.Zero,
            end = androidx.compose.ui.geometry.Offset.Infinite
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}