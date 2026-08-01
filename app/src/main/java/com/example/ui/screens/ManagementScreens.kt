package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwitchAccessShortcut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun EmergencyContactsScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit
) {
    val contacts by viewModel.allContacts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newRel by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("contacts_back_btn")) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("EMERGENCY CONTACTS", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            GlassCard {
                Text("AUTOMATED SMS DISPATCH PROTOCOL", color = GuardianPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Primary contact will receive high-priority SMS containing crash GPS link & hospital ETA upon crash detection.",
                    color = GuardianTextSecondaryDark,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showAddDialog) {
                GlassCard {
                    Text("ADD NEW CONTACT", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Full Name", color = GuardianTextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth().testTag("contact_name_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newRel,
                        onValueChange = { newRel = it },
                        label = { Text("Relationship (e.g. Spouse, Parent)", color = GuardianTextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth().testTag("contact_rel_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPhone,
                        onValueChange = { newPhone = it },
                        label = { Text("Phone Number", color = GuardianTextSecondaryDark) },
                        modifier = Modifier.fillMaxWidth().testTag("contact_phone_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = {
                                if (newName.isNotBlank() && newPhone.isNotBlank()) {
                                    viewModel.addContact(newName, newRel, newPhone)
                                    showAddDialog = false
                                    newName = ""
                                    newRel = ""
                                    newPhone = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GuardianPrimary)
                        ) {
                            Text("Save Contact")
                        }
                    }
                }
            } else {
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("add_contact_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GuardianSecondary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Emergency Contact", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(contacts) { contact ->
                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(GuardianPrimary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = GuardianPrimary)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(contact.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("${contact.relationship} • ${contact.phoneNumber}", color = GuardianTextSecondaryDark, fontSize = 13.sp)
                                }
                            }
                            IconButton(onClick = { viewModel.deleteContact(contact.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = GuardianDanger)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AutoPaySetupScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit
) {
    val autoPayLimit by viewModel.autoPayLimit.collectAsState()
    val isAutoPayActive by viewModel.isAutoPayActive.collectAsState()
    val insurancePolicy by viewModel.insurancePolicy.collectAsState()

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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("autopay_back_btn")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PRAVA AUTOPAY SETUP", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Emergency AutoPay Authorization", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Enable Prava Sandbox instant release", color = GuardianTextSecondaryDark, fontSize = 12.sp)
                        }
                        Switch(
                            checked = isAutoPayActive,
                            onCheckedChange = { viewModel.toggleAutoPay(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PravaGold, checkedTrackColor = PravaGold.copy(alpha = 0.3f)),
                            modifier = Modifier.testTag("autopay_toggle_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassCard {
                    Text("PRE-APPROVED EMERGENCY LIMIT", color = PravaGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("\$${autoPayLimit.toInt()}.00", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Slider(
                        value = autoPayLimit.toFloat(),
                        onValueChange = { viewModel.updateAutoPayLimit(it.toDouble()) },
                        valueRange = 1000f..10000f,
                        steps = 9,
                        colors = SliderDefaults.colors(thumbColor = PravaGold, activeTrackColor = PravaGold),
                        modifier = Modifier.testTag("autopay_limit_slider")
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("\$1,000", color = GuardianTextSecondaryDark, fontSize = 11.sp)
                        Text("\$10,000", color = GuardianTextSecondaryDark, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassCard {
                    Text("LINKED INSURANCE POLICY", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(insurancePolicy, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Coverage Status: VERIFIED & ACTIVE", color = GuardianSuccess, fontSize = 12.sp)
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(52.dp).testTag("save_autopay_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = PravaGold),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Save Prava AutoPay Settings", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun ProfileScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit
) {
    val name by viewModel.userName.collectAsState()
    val blood by viewModel.bloodType.collectAsState()
    val allergies by viewModel.allergies.collectAsState()
    val policy by viewModel.insurancePolicy.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("profile_back_btn")) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("MEDICAL PROFILE & HEALTH PASSPORT", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            GlassCard {
                Text("FULL NAME", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            GlassCard {
                Text("BLOOD TYPE (CRITICAL FOR PARAMEDICS)", color = GuardianDanger, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(blood, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            GlassCard {
                Text("KNOWN ALLERGIES", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(allergies, color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            GlassCard {
                Text("INSURANCE PROVIDER & POLICY", color = GuardianTextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("BlueShield Health Protection", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Policy No: $policy", color = GuardianPrimary, fontSize = 13.sp)
            }
        }
    }
}
