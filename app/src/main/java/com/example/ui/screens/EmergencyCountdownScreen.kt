package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlowingPulseAnimation
import com.example.ui.theme.GuardianBackgroundDark
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSuccess
import com.example.ui.theme.GuardianTextSecondaryDark
import com.example.ui.viewmodel.EmergencyViewModel

@Composable
fun EmergencyCountdownScreen(
    viewModel: EmergencyViewModel,
    onCancelEmergency: () -> Unit,
    onManualDispatch: () -> Unit
) {
    val secondsLeft by viewModel.countdownSeconds.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Warning
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = GuardianDanger)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HIGH IMPACT CRASH DETECTED",
                        color = GuardianDanger,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "ARE YOU SAFE?",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "If you do not respond, Guardian AI will automatically dispatch an ambulance & initiate Prava Emergency AutoPay.",
                    color = GuardianTextSecondaryDark,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            // Central Ring Countdown Timer
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                GlowingPulseAnimation(sizeDp = 240.dp, color = GuardianDanger)

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val sweep = (secondsLeft / 10f) * 360f
                    drawArc(
                        color = GuardianDanger.copy(alpha = 0.2f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 16f)
                    )
                    drawArc(
                        color = GuardianDanger,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = 16f)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$secondsLeft",
                        color = Color.White,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "SECONDS",
                        color = GuardianTextSecondaryDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Action Buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Cancel Emergency Button
                Button(
                    onClick = onCancelEmergency,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .testTag("i_am_safe_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GuardianSuccess),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "I AM SAFE • CANCEL EMERGENCY",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Immediate Manual Dispatch Option
                TextButton(
                    onClick = onManualDispatch,
                    modifier = Modifier.testTag("dispatch_now_btn")
                ) {
                    Text("I Need Help Now • Dispatch Immediately", color = GuardianDanger, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
