package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPulseAnimation
import com.example.ui.theme.GuardianBackgroundDark
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianGlassBorder
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSecondary
import com.example.ui.theme.GuardianSuccess
import com.example.ui.theme.GuardianTextSecondaryDark
import com.example.ui.theme.PravaGold
import com.example.ui.viewmodel.EmergencyViewModel

@Composable
fun AiAnalysisScreen(viewModel: EmergencyViewModel) {
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val aiResult by viewModel.aiResult.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isAnalyzing) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GlowingPulseAnimation(sizeDp = 180.dp, color = GuardianPrimary)
                Spacer(modifier = Modifier.height(30.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = PravaGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GUARDIAN AI SYNTHESIZING INCIDENT",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Analyzing G-Force telemetry (18.4g), acoustic rollover patterns, & occupant responsiveness...",
                    color = GuardianTextSecondaryDark,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            aiResult?.let { result ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = PravaGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI EMERGENCY DIAGNOSTIC COMPLETE",
                                color = PravaGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Confidence Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(GuardianDanger.copy(alpha = 0.15f))
                                .border(1.dp, GuardianDanger, RoundedCornerShape(20.dp))
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "SEVERITY", color = GuardianDanger, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text(text = result.severity, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(GuardianDanger),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${result.confidenceScore}%",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // AI Reasoning Card
                        GlassCard {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = GuardianPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI REASONING EXPLANATION", color = GuardianPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = result.reasoningText,
                                color = Color.White,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Hospital & Ambulance Recommendation
                        GlassCard {
                            Text("AUTOMATICALLY SELECTED MATCHES", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = result.hospitalRecommendation.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "${result.hospitalRecommendation.distanceKm} km away • ETA ${result.hospitalRecommendation.etaMins} mins", color = GuardianSuccess, fontSize = 12.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GuardianPrimary)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("AI CHOICE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Ambulance: ${result.ambulanceRecommendation.name}", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Initiating Emergency Verification Countdown...",
                            color = GuardianTextSecondaryDark,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
