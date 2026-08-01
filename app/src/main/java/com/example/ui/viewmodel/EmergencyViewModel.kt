package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.models.Ambulance
import com.example.data.models.EmergencyContact
import com.example.data.models.Hospital
import com.example.data.models.IncidentReport
import com.example.data.models.PravaPaymentState
import com.example.data.models.Trip
import com.example.services.ai.AiAnalysisResult
import com.example.services.ai.AiEmergencyService
import com.example.services.payment.MockPravaSandboxService
import com.example.services.payment.PaymentService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Splash : Screen()
    object Onboarding : Screen()
    object Login : Screen()
    object SignUp : Screen()
    object Home : Screen()
    object StartJourney : Screen()
    object JourneyMonitoring : Screen()
    object AiAnalysis : Screen()
    object EmergencyCountdown : Screen()
    object HospitalSelection : Screen()
    object AmbulanceSelection : Screen()
    object CostEstimation : Screen()
    object PravaPayment : Screen()
    object PaymentSuccess : Screen()
    object LiveTracking : Screen()
    object IncidentReportView : Screen()
    object EmergencyContactsView : Screen()
    object AutoPaySetup : Screen()
    object Profile : Screen()
    object PaymentHistoryView : Screen()
    object SettingsView : Screen()
}

class EmergencyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.emergencyDao()
    private val aiService = AiEmergencyService()
    private val paymentService: PaymentService = MockPravaSandboxService()

    // Navigation State
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Database Flows
    val allTrips: StateFlow<List<Trip>> = dao.getAllTrips()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allIncidents: StateFlow<List<IncidentReport>> = dao.getAllIncidents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allContacts: StateFlow<List<EmergencyContact>> = dao.getAllContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Journey State
    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()

    private val _currentSpeed = MutableStateFlow(0f)
    val currentSpeed: StateFlow<Float> = _currentSpeed.asStateFlow()

    private val _currentGForce = MutableStateFlow(1.0f)
    val currentGForce: StateFlow<Float> = _currentGForce.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private var journeyTimerJob: Job? = null

    // AI Analysis State
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _aiResult = MutableStateFlow<AiAnalysisResult?>(null)
    val aiResult: StateFlow<AiAnalysisResult?> = _aiResult.asStateFlow()

    // Emergency Countdown State
    private val _countdownSeconds = MutableStateFlow(10)
    val countdownSeconds: StateFlow<Int> = _countdownSeconds.asStateFlow()
    private var countdownJob: Job? = null

    // Selections
    val availableHospitals = aiService.sampleHospitals
    private val _selectedHospital = MutableStateFlow<Hospital>(aiService.sampleHospitals.first())
    val selectedHospital: StateFlow<Hospital> = _selectedHospital.asStateFlow()

    val availableAmbulances = aiService.sampleAmbulances
    private val _selectedAmbulance = MutableStateFlow<Ambulance>(aiService.sampleAmbulances.first())
    val selectedAmbulance: StateFlow<Ambulance> = _selectedAmbulance.asStateFlow()

    // Prava Payment State
    private val _paymentState = MutableStateFlow<PravaPaymentState>(PravaPaymentState.Idle)
    val paymentState: StateFlow<PravaPaymentState> = _paymentState.asStateFlow()

    // User Profile / Settings State
    private val _userName = MutableStateFlow("Alex Rivers")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _bloodType = MutableStateFlow("O Positive (O+)")
    val bloodType: StateFlow<String> = _bloodType.asStateFlow()

    private val _allergies = MutableStateFlow("Penicillin, Latex")
    val allergies: StateFlow<String> = _allergies.asStateFlow()

    private val _insurancePolicy = MutableStateFlow("PRV-884920-SF")
    val insurancePolicy: StateFlow<String> = _insurancePolicy.asStateFlow()

    private val _autoPayLimit = MutableStateFlow(5000.0)
    val autoPayLimit: StateFlow<Double> = _autoPayLimit.asStateFlow()

    private val _isAutoPayActive = MutableStateFlow(true)
    val isAutoPayActive: StateFlow<Boolean> = _isAutoPayActive.asStateFlow()

    // Selected Incident for detailed view
    private val _selectedIncident = MutableStateFlow<IncidentReport?>(null)
    val selectedIncident: StateFlow<IncidentReport?> = _selectedIncident.asStateFlow()

    init {
        // Populate sample emergency contacts if database is empty
        viewModelScope.launch {
            dao.insertContact(EmergencyContact(name = "Sarah Rivers", relationship = "Spouse", phoneNumber = "+1 (555) 392-1049", isPrimary = true))
            dao.insertContact(EmergencyContact(name = "Dr. Robert Vance", relationship = "Primary Physician", phoneNumber = "+1 (555) 882-9910", isPrimary = false))
            
            // Populate sample past trip
            dao.insertTrip(Trip(startLocation = "San Francisco Downtown", destination = "Silicon Valley Bay", distanceKm = 32.4, status = "COMPLETED"))
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun startJourney() {
        _isMonitoring.value = true
        _elapsedSeconds.value = 0
        _currentSpeed.value = 65f
        _currentGForce.value = 1.0f
        _currentScreen.value = Screen.JourneyMonitoring

        journeyTimerJob?.cancel()
        journeyTimerJob = viewModelScope.launch {
            while (_isMonitoring.value) {
                delay(1000)
                _elapsedSeconds.value += 1
                // Fluctuate speed realistically
                _currentSpeed.value = (62..78).random().toFloat()
                _currentGForce.value = 0.95f + (0..10).random() * 0.02f
            }
        }
    }

    fun stopJourney() {
        _isMonitoring.value = false
        journeyTimerJob?.cancel()
        viewModelScope.launch {
            dao.insertTrip(
                Trip(
                    startLocation = "San Francisco Downtown",
                    destination = "Financial District",
                    distanceKm = (_elapsedSeconds.value * 0.018),
                    status = "COMPLETED"
                )
            )
        }
        _currentScreen.value = Screen.Home
    }

    fun simulateAccident() {
        _isMonitoring.value = false
        journeyTimerJob?.cancel()
        _currentSpeed.value = 0f
        _currentGForce.value = 18.4f

        _currentScreen.value = Screen.AiAnalysis
        _isAnalyzing.value = true

        viewModelScope.launch {
            val result = aiService.analyzeAccident(gForce = 18.4f, speedKmH = 78f, userResponded = false)
            _aiResult.value = result
            _isAnalyzing.value = false

            // Auto navigate to 10s countdown
            delay(1500)
            startCountdown()
        }
    }

    private fun startCountdown() {
        _countdownSeconds.value = 10
        _currentScreen.value = Screen.EmergencyCountdown

        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (_countdownSeconds.value > 0) {
                delay(1000)
                _countdownSeconds.value -= 1
            }
            // Countdown expired, proceed automatically to hospital selection / auto-dispatch
            triggerAutoEmergencyDispatch()
        }
    }

    fun cancelEmergencyCountdown() {
        countdownJob?.cancel()
        _currentScreen.value = Screen.Home
    }

    fun selectHospital(hospital: Hospital) {
        _selectedHospital.value = hospital
    }

    fun selectAmbulance(ambulance: Ambulance) {
        _selectedAmbulance.value = ambulance
    }

    fun triggerAutoEmergencyDispatch() {
        countdownJob?.cancel()
        _currentScreen.value = Screen.PravaPayment
        executePravaPayment()
    }

    fun executePravaPayment() {
        viewModelScope.launch {
            paymentService.processEmergencyAutoPay(
                merchantName = "Apex Emergency & Trauma Services",
                hospitalName = _selectedHospital.value.name,
                payableAmount = 600.00,
                insurancePolicyNo = _insurancePolicy.value,
                emergencyLimit = _autoPayLimit.value
            ).collect { state ->
                _paymentState.value = state
                if (state is PravaPaymentState.Success) {
                    // Create and save incident report
                    val incident = IncidentReport(
                        severity = _aiResult.value?.severity ?: "CRITICAL",
                        confidenceScore = _aiResult.value?.confidenceScore ?: 97,
                        reasoning = _aiResult.value?.reasoningText ?: "High-impact crash verified.",
                        hospitalName = _selectedHospital.value.name,
                        ambulanceDriver = _selectedAmbulance.value.driverName,
                        ambulanceVehicle = _selectedAmbulance.value.unitName,
                        ambulanceEta = "${_selectedAmbulance.value.etaMins} mins",
                        totalCost = 2950.00,
                        insuranceCovered = 2350.00,
                        netPaid = state.amount,
                        pravaTxId = state.transactionId,
                        status = "DISPATCHED"
                    )
                    dao.insertIncident(incident)
                    _selectedIncident.value = incident
                    delay(800)
                    _currentScreen.value = Screen.PaymentSuccess
                }
            }
        }
    }

    fun viewIncidentDetails(incident: IncidentReport) {
        _selectedIncident.value = incident
        _currentScreen.value = Screen.IncidentReportView
    }

    fun addContact(name: String, relationship: String, phone: String) {
        viewModelScope.launch {
            dao.insertContact(EmergencyContact(name = name, relationship = relationship, phoneNumber = phone))
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch {
            dao.deleteContact(id)
        }
    }

    fun updateAutoPayLimit(limit: Double) {
        _autoPayLimit.value = limit
    }

    fun toggleAutoPay(enabled: Boolean) {
        _isAutoPayActive.value = enabled
    }

    fun updateProfile(name: String, blood: String, allergyList: String, policyNo: String) {
        _userName.value = name
        _bloodType.value = blood
        _allergies.value = allergyList
        _insurancePolicy.value = policyNo
    }
}
