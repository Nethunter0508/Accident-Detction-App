package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GuardianBackgroundDark
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianTextSecondaryDark
import com.example.ui.viewmodel.Screen

@Composable
fun GuardianBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    // Only show bottom bar on standard main screens
    if (currentScreen is Screen.Splash ||
        currentScreen is Screen.Onboarding ||
        currentScreen is Screen.Login ||
        currentScreen is Screen.SignUp ||
        currentScreen is Screen.JourneyMonitoring ||
        currentScreen is Screen.AiAnalysis ||
        currentScreen is Screen.EmergencyCountdown ||
        currentScreen is Screen.PravaPayment
    ) {
        return
    }

    NavigationBar(
        containerColor = Color(0xFF0F172A),
        tonalElevation = 8.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavigationBarItem(
            selected = currentScreen is Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GuardianPrimary,
                selectedTextColor = GuardianPrimary,
                unselectedIconColor = GuardianTextSecondaryDark,
                unselectedTextColor = GuardianTextSecondaryDark,
                indicatorColor = GuardianPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_home_btn")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.AutoPaySetup,
            onClick = { onNavigate(Screen.AutoPaySetup) },
            icon = { Icon(Icons.Default.CreditCard, contentDescription = "AutoPay") },
            label = { Text("AutoPay", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GuardianPrimary,
                selectedTextColor = GuardianPrimary,
                unselectedIconColor = GuardianTextSecondaryDark,
                unselectedTextColor = GuardianTextSecondaryDark,
                indicatorColor = GuardianPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_autopay_btn")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.EmergencyContactsView,
            onClick = { onNavigate(Screen.EmergencyContactsView) },
            icon = { Icon(Icons.Default.Phone, contentDescription = "Contacts") },
            label = { Text("Contacts", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GuardianPrimary,
                selectedTextColor = GuardianPrimary,
                unselectedIconColor = GuardianTextSecondaryDark,
                unselectedTextColor = GuardianTextSecondaryDark,
                indicatorColor = GuardianPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_contacts_btn")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Profile,
            onClick = { onNavigate(Screen.Profile) },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GuardianPrimary,
                selectedTextColor = GuardianPrimary,
                unselectedIconColor = GuardianTextSecondaryDark,
                unselectedTextColor = GuardianTextSecondaryDark,
                indicatorColor = GuardianPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_profile_btn")
        )
    }
}
