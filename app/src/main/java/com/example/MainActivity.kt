package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.GuardianBottomBar
import com.example.ui.screens.AiAnalysisScreen
import com.example.ui.screens.AmbulanceSelectionScreen
import com.example.ui.screens.AutoPaySetupScreen
import com.example.ui.screens.CostEstimationScreen
import com.example.ui.screens.EmergencyContactsScreen
import com.example.ui.screens.EmergencyCountdownScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HospitalSelectionScreen
import com.example.ui.screens.IncidentReportScreen
import com.example.ui.screens.JourneyMonitoringScreen
import com.example.ui.screens.LiveTrackingScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PaymentSuccessScreen
import com.example.ui.screens.PravaPaymentScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.GuardianAITheme
import com.example.ui.viewmodel.EmergencyViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: EmergencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuardianAITheme {
                val currentScreen by viewModel.currentScreen.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        GuardianBottomBar(
                            currentScreen = currentScreen,
                            onNavigate = { screen -> viewModel.navigateTo(screen) }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            is Screen.Splash -> SplashScreen {
                                viewModel.navigateTo(Screen.Onboarding)
                            }

                            is Screen.Onboarding -> OnboardingScreen {
                                viewModel.navigateTo(Screen.Login)
                            }

                            is Screen.Login, is Screen.SignUp -> LoginScreen(
                                onLoginSuccess = { viewModel.navigateTo(Screen.Home) },
                                onGoToSignUp = { viewModel.navigateTo(Screen.Home) }
                            )

                            is Screen.Home -> HomeScreen(
                                viewModel = viewModel,
                                onStartJourney = { viewModel.startJourney() },
                                onSimulateAccident = { viewModel.simulateAccident() },
                                onOpenAutoPaySetup = { viewModel.navigateTo(Screen.AutoPaySetup) },
                                onOpenContacts = { viewModel.navigateTo(Screen.EmergencyContactsView) },
                                onOpenProfile = { viewModel.navigateTo(Screen.Profile) }
                            )

                            is Screen.StartJourney, is Screen.JourneyMonitoring -> JourneyMonitoringScreen(
                                viewModel = viewModel,
                                onSimulateAccident = { viewModel.simulateAccident() },
                                onEndJourney = { viewModel.stopJourney() }
                            )

                            is Screen.AiAnalysis -> AiAnalysisScreen(viewModel = viewModel)

                            is Screen.EmergencyCountdown -> EmergencyCountdownScreen(
                                viewModel = viewModel,
                                onCancelEmergency = { viewModel.cancelEmergencyCountdown() },
                                onManualDispatch = { viewModel.triggerAutoEmergencyDispatch() }
                            )

                            is Screen.HospitalSelection -> HospitalSelectionScreen(
                                viewModel = viewModel,
                                onHospitalSelected = { viewModel.navigateTo(Screen.AmbulanceSelection) }
                            )

                            is Screen.AmbulanceSelection -> AmbulanceSelectionScreen(
                                viewModel = viewModel,
                                onAmbulanceSelected = { viewModel.navigateTo(Screen.CostEstimation) }
                            )

                            is Screen.CostEstimation -> CostEstimationScreen(
                                viewModel = viewModel,
                                onProceedToPrava = { viewModel.triggerAutoEmergencyDispatch() }
                            )

                            is Screen.PravaPayment -> PravaPaymentScreen(viewModel = viewModel)

                            is Screen.PaymentSuccess -> PaymentSuccessScreen(
                                viewModel = viewModel,
                                onTrackAmbulance = { viewModel.navigateTo(Screen.LiveTracking) },
                                onViewIncidentReport = { viewModel.navigateTo(Screen.IncidentReportView) }
                            )

                            is Screen.LiveTracking -> LiveTrackingScreen(
                                viewModel = viewModel,
                                onBackToHome = { viewModel.navigateTo(Screen.Home) }
                            )

                            is Screen.IncidentReportView -> IncidentReportScreen(
                                viewModel = viewModel,
                                onBackToHome = { viewModel.navigateTo(Screen.Home) }
                            )

                            is Screen.EmergencyContactsView -> EmergencyContactsScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateTo(Screen.Home) }
                            )

                            is Screen.AutoPaySetup -> AutoPaySetupScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateTo(Screen.Home) }
                            )

                            is Screen.Profile -> ProfileScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateTo(Screen.Home) }
                            )

                            else -> HomeScreen(
                                viewModel = viewModel,
                                onStartJourney = { viewModel.startJourney() },
                                onSimulateAccident = { viewModel.simulateAccident() },
                                onOpenAutoPaySetup = { viewModel.navigateTo(Screen.AutoPaySetup) },
                                onOpenContacts = { viewModel.navigateTo(Screen.EmergencyContactsView) },
                                onOpenProfile = { viewModel.navigateTo(Screen.Profile) }
                            )
                        }
                    }
                }
            }
        }
    }
}
