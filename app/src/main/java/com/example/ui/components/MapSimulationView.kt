package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianGlassBorder
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSuccess

@Composable
fun MapSimulationView(
    modifier: Modifier = Modifier,
    heightDp: Int = 220,
    showAmbulanceRoute: Boolean = false,
    label: String = "LIVE GPS TELEMETRY"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mapPulse")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, GuardianGlassBorder, RoundedCornerShape(20.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Grid lines background
            val gridStep = 40.dp.toPx()
            var x = 0f
            while (x < width) {
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 1f
                )
                x += gridStep
            }
            var y = 0f
            while (y < height) {
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
                y += gridStep
            }

            // Simulated Highway Road Paths
            val mainRoadPath = Path().apply {
                moveTo(width * 0.1f, height * 0.85f)
                cubicTo(width * 0.35f, height * 0.8f, width * 0.45f, height * 0.3f, width * 0.9f, height * 0.2f)
            }

            // Draw road outline
            drawPath(
                path = mainRoadPath,
                color = Color(0xFF334155),
                style = Stroke(width = 24f)
            )

            // Draw road dashed line
            drawPath(
                path = mainRoadPath,
                color = Color(0xFF94A3B8),
                style = Stroke(
                    width = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
                )
            )

            if (showAmbulanceRoute) {
                // Secondary Ambulance Emergency Route
                val ambulancePath = Path().apply {
                    moveTo(width * 0.85f, height * 0.85f)
                    lineTo(width * 0.65f, height * 0.5f)
                    lineTo(width * 0.45f, height * 0.3f)
                }

                drawPath(
                    path = ambulancePath,
                    color = GuardianDanger,
                    style = Stroke(
                        width = 8f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), pulseProgress * 30f)
                    )
                )

                // Ambulance Marker (Red flashing dot)
                val ambX = width * (0.85f - pulseProgress * 0.4f)
                val ambY = height * (0.85f - pulseProgress * 0.55f)

                drawCircle(
                    color = GuardianDanger.copy(alpha = 0.4f),
                    radius = 24f,
                    center = Offset(ambX, ambY)
                )
                drawCircle(
                    color = GuardianDanger,
                    radius = 12f,
                    center = Offset(ambX, ambY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(ambX, ambY)
                )
            } else {
                // Vehicle User Marker
                val userX = width * 0.45f
                val userY = height * 0.5f

                drawCircle(
                    color = GuardianPrimary.copy(alpha = 0.3f),
                    radius = 30f * (1f + pulseProgress * 0.5f),
                    center = Offset(userX, userY)
                )
                drawCircle(
                    color = GuardianPrimary,
                    radius = 14f,
                    center = Offset(userX, userY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 6f,
                    center = Offset(userX, userY)
                )
            }

            // Destination Hospital Marker
            val hospX = width * 0.85f
            val hospY = height * 0.22f
            drawCircle(
                color = GuardianSuccess,
                radius = 16f,
                center = Offset(hospX, hospY)
            )
            drawCircle(
                color = Color.White,
                radius = 6f,
                center = Offset(hospX, hospY)
            )
        }

        // Overlay Label
        Box(
            modifier = Modifier
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xDD0F172A))
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = label,
                color = GuardianSuccess,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}
