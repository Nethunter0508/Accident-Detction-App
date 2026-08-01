package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
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
fun HospitalSelectionScreen(
    viewModel: EmergencyViewModel,
    onHospitalSelected: () -> Unit
) {
    val selectedHospital by viewModel.selectedHospital.collectAsState()
    val hospitals = viewModel.availableHospitals

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocalHospital, contentDescription = null, tint = GuardianPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SELECT TRAUMA CENTER",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Guardian AI identified 5 trauma centers near collision site.",
                    color = GuardianTextSecondaryDark,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(460.dp)
                ) {
                    items(hospitals) { hospital ->
                        val isSelected = hospital.id == selectedHospital.id
                        GlassCard(
                            borderColor = if (isSelected) GuardianPrimary else GuardianGlassBorder,
                            backgroundColor = if (isSelected) Color(0xFF1E1B4B) else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.clickable { viewModel.selectHospital(hospital) }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = hospital.name,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (hospital.isAiRecommended) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(PravaGold)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("AI BEST MATCH", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = hospital.traumaCapability,
                                        color = GuardianTextSecondaryDark,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("${hospital.distanceKm} km away", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text("ETA ${hospital.etaMins} mins", color = GuardianSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (isSelected) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GuardianPrimary)
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onHospitalSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("confirm_hospital_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = GuardianPrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Confirm Trauma Center & Select Ambulance", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun AmbulanceSelectionScreen(
    viewModel: EmergencyViewModel,
    onAmbulanceSelected: () -> Unit
) {
    val selectedAmbulance by viewModel.selectedAmbulance.collectAsState()
    val ambulances = viewModel.availableAmbulances

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
                Text(
                    text = "DISPATCH AMBULANCE UNIT",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Select high-priority ICU emergency vehicle.",
                    color = GuardianTextSecondaryDark,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ambulances.forEach { amb ->
                        val isSelected = amb.id == selectedAmbulance.id
                        GlassCard(
                            borderColor = if (isSelected) GuardianPrimary else GuardianGlassBorder,
                            backgroundColor = if (isSelected) Color(0xFF1E1B4B) else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.clickable { viewModel.selectAmbulance(amb) }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = amb.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "Driver: ${amb.driverName}", color = GuardianTextSecondaryDark, fontSize = 12.sp)
                                    Text(text = "Equipment: ${amb.equipmentLevel}", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                                    Text(text = "ETA: ${amb.etaMins} mins", color = GuardianSuccess, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                if (isSelected) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GuardianPrimary)
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onAmbulanceSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("confirm_ambulance_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = GuardianPrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Proceed to Cost Estimation", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun CostEstimationScreen(
    viewModel: EmergencyViewModel,
    onProceedToPrava: () -> Unit
) {
    val hospital by viewModel.selectedHospital.collectAsState()
    val ambulance by viewModel.selectedAmbulance.collectAsState()
    val autoPayLimit by viewModel.autoPayLimit.collectAsState()

    val ambulanceFee = 450.00
    val hospitalDeposit = hospital.priceDeposit
    val totalEstimated = ambulanceFee + hospitalDeposit
    val insuranceCovered = 2350.00
    val netPayable = totalEstimated - insuranceCovered

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
                Text(
                    text = "EMERGENCY COST ESTIMATION",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Transparent breakdown pre-authorized via Prava AutoPay.",
                    color = GuardianTextSecondaryDark,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                GlassCard {
                    Text("ITEMIZED FEES", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("ICU Ambulance Dispatch (${ambulance.name})", color = Color.White, fontSize = 13.sp)
                        Text("\$450.00", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Trauma Hospital Reservation Deposit", color = Color.White, fontSize = 13.sp)
                        Text("\$${hospitalDeposit.toInt()}.00", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GuardianGlassBorder))
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Emergency Services:", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("\$${totalEstimated.toInt()}.00", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Insurance Coverage Deduction:", color = GuardianSuccess, fontSize = 13.sp)
                        Text("-\$${insuranceCovered.toInt()}.00", color = GuardianSuccess, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GuardianGlassBorder))
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Net Payable via Prava AutoPay:", color = PravaGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("\$${netPayable.toInt()}.00", color = PravaGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Button(
                onClick = onProceedToPrava,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("trigger_prava_autopay_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = PravaGold),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Authorize Prava AutoPay Sandbox (\$${netPayable.toInt()}.00)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}
