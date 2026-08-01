package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.MapSimulationView
import com.example.ui.theme.GuardianBackgroundDark
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianGlassBorder
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSuccess
import com.example.ui.theme.GuardianTextSecondaryDark
import com.example.ui.viewmodel.EmergencyViewModel

@Composable
fun LiveTrackingScreen(
    viewModel: EmergencyViewModel,
    onBackToHome: () -> Unit
) {
    val hospital by viewModel.selectedHospital.collectAsState()
    val ambulance by viewModel.selectedAmbulance.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToHome,
                        modifier = Modifier.testTag("tracking_back_btn")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("LIVE AMBULANCE DISPATCH", color = GuardianDanger, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text("Real-Time Telemetry Tracking", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Route Map
                MapSimulationView(
                    heightDp = 300,
                    showAmbulanceRoute = true,
                    label = "AMBULANCE GPS DISPATCH • EN ROUTE"
                )

                Spacer(modifier = Modifier.height(18.dp))

                // ETA Card
                GlassCard(backgroundColor = Color(0xFF1E1B4B), borderColor = GuardianDanger) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ESTIMATED ARRIVAL TIME", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("4 MINUTES AWAY", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(GuardianDanger),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Driver & Destination Info
                GlassCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GuardianPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.LocalHospital, contentDescription = null, tint = GuardianPrimary, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(ambulance.driverName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Unit: ${ambulance.name} • ${ambulance.vehicleType}", color = GuardianTextSecondaryDark, fontSize = 12.sp)
                            Text("Target: ${hospital.name}", color = GuardianSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Action Call Button
            Button(
                onClick = { /* Simulated call */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("call_driver_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = GuardianSuccess),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Call Ambulance Driver Direct", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
