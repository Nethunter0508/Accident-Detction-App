package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PravaPaymentState
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPulseAnimation
import com.example.ui.theme.GuardianBackgroundDark
import com.example.ui.theme.GuardianGlassBorder
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSuccess
import com.example.ui.theme.GuardianTextSecondaryDark
import com.example.ui.theme.PravaGold
import com.example.ui.viewmodel.EmergencyViewModel

@Composable
fun PravaPaymentScreen(viewModel: EmergencyViewModel) {
    val paymentState by viewModel.paymentState.collectAsState()
    val hospital by viewModel.selectedHospital.collectAsState()
    val autoPayLimit by viewModel.autoPayLimit.collectAsState()
    val insurancePolicy by viewModel.insurancePolicy.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        GuardianBackgroundDark,
                        Color(0xFF1E1B4B),
                        GuardianBackgroundDark
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = PravaGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PRAVA EMERGENCY AUTOPAY",
                        color = PravaGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Guardian AI Verified Emergency",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Using your Emergency AutoPay authorization",
                    color = GuardianTextSecondaryDark,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Central Animated Card
            GlassCard(
                backgroundColor = Color(0xEE1E293B),
                borderColor = PravaGold.copy(alpha = 0.6f),
                elevation = 12.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TOTAL AUTOPAY DEPOSIT", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("\$600.00", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PravaGold.copy(alpha = 0.2f))
                            .border(1.dp, PravaGold, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("SANDBOX LIVE", color = PravaGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Detail Items
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Merchant:", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                        Text("Apex Emergency Services", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Assigned Hospital:", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                        Text(hospital.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Insurance Policy:", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                        Text(insurancePolicy, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pre-Approved Limit:", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                        Text("\$${autoPayLimit.toInt()}.00", color = GuardianSuccess, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Timeline Steps
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "AUTONOMOUS TRANSACTION TIMELINE",
                    color = GuardianTextSecondaryDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                // Step 1: Signature Verification
                TimelineStepItem(
                    title = "1. Guardian AI Signature Handshake",
                    subtitle = "Cryptographic incident signature verified",
                    isActive = paymentState !is PravaPaymentState.Idle,
                    isCompleted = paymentState is PravaPaymentState.AuthorizingSandbox || paymentState is PravaPaymentState.ReservingDeposit || paymentState is PravaPaymentState.Success
                )

                // Step 2: Prava Sandbox Authorizing
                TimelineStepItem(
                    title = "2. Prava Sandbox Authorizing Limit",
                    subtitle = "Validating pre-approved balance",
                    isActive = paymentState is PravaPaymentState.AuthorizingSandbox || paymentState is PravaPaymentState.ReservingDeposit || paymentState is PravaPaymentState.Success,
                    isCompleted = paymentState is PravaPaymentState.ReservingDeposit || paymentState is PravaPaymentState.Success
                )

                // Step 3: Hospital Deposit Secured
                TimelineStepItem(
                    title = "3. Deposit Secured & Ambulance Dispatched",
                    subtitle = "Reserving emergency trauma bed",
                    isActive = paymentState is PravaPaymentState.ReservingDeposit || paymentState is PravaPaymentState.Success,
                    isCompleted = paymentState is PravaPaymentState.Success
                )
            }

            // Footer info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = PravaGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "End-to-End Encrypted Prava Protocol",
                    color = PravaGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TimelineStepItem(
    title: String,
    subtitle: String,
    isActive: Boolean,
    isCompleted: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) Color(0xFF1E293B) else Color.Transparent)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> GuardianSuccess
                        isActive -> PravaGold
                        else -> Color.Gray.copy(alpha = 0.2f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            } else if (isActive) {
                CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
            } else {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                color = if (isActive || isCompleted) Color.White else Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = GuardianTextSecondaryDark,
                fontSize = 12.sp
            )
        }
    }
}
