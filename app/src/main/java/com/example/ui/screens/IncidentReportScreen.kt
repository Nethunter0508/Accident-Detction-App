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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.GuardianBackgroundDark
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianGlassBorder
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSuccess
import com.example.ui.theme.GuardianTextSecondaryDark
import com.example.ui.theme.PravaGold
import com.example.ui.viewmodel.EmergencyViewModel

@Composable
fun IncidentReportScreen(
    viewModel: EmergencyViewModel,
    onBackToHome: () -> Unit
) {
    val incident by viewModel.selectedIncident.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToHome,
                        modifier = Modifier.testTag("report_back_btn")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("AUTONOMOUS INCIDENT REPORT", color = GuardianPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text("Official Crash Audit & Log", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // AI Reasoning Summary Card
            item {
                GlassCard(backgroundColor = Color(0xFF1E1B4B), borderColor = PravaGold) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = PravaGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SYNTHESIZED AI ANALYSIS", color = PravaGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = incident?.reasoning ?: "Vehicle experienced a high-impact rollover collision (18.4g). Occupant unresponsive. AI confidence 97%. Apex Level-1 Trauma Center automatically dispatched.",
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            // Telemetry Log
            item {
                GlassCard {
                    Text("CRASH TELEMETRY SPECIFICATION", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Deceleration Force:", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                        Text("18.4 G-Force (Critical)", color = GuardianDanger, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pre-Crash Velocity:", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                        Text("78.2 km/h", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Rollover Sensor:", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                        Text("TRIGGERED (90° tilt)", color = GuardianDanger, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("User Verification:", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                        Text("Unresponsive (10s auto-dispatch)", color = GuardianSuccess, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Assigned Response Unit & Hospital
            item {
                GlassCard {
                    Text("DISPATCH LOGISTICS", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Hospital: ${incident?.hospitalName ?: "Apex Trauma Center"}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Ambulance: ${incident?.ambulanceVehicle ?: "RapidResponse ICU Unit #7"}", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                    Text("Driver: ${incident?.ambulanceDriver ?: "Marcus Vance"}", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                }
            }

            // Prava Payment Audit
            item {
                GlassCard {
                    Text("PRAVA SANDBOX PAYMENT AUDIT", color = PravaGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Prava Tx Hash:", color = GuardianTextSecondaryDark, fontSize = 12.sp)
                        Text(incident?.pravaTxId ?: "TXN-PRAVA-883920194", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Net Authorized:", color = GuardianTextSecondaryDark, fontSize = 12.sp)
                        Text("\$${incident?.netPaid?.toInt() ?: 600}.00", color = PravaGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Button(
                    onClick = { /* Simulated download */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("download_report_pdf_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GuardianPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export Official PDF Report", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
