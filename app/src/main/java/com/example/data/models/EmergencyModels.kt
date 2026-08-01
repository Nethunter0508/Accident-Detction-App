package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val startLocation: String = "San Francisco Downtown",
    val destination: String = "Palo Alto Tech Hub",
    val distanceKm: Double = 14.2,
    val maxGforce: Float = 1.0f,
    val status: String = "COMPLETED" // MONITORING, COMPLETED, ACCIDENT_DETECTED
)

@Entity(tableName = "incident_reports")
data class IncidentReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val severity: String = "CRITICAL",
    val confidenceScore: Int = 97,
    val reasoning: String = "Vehicle experienced a high-impact rollover collision (18.4g). User did not respond to safety prompt. Emergency confidence is 97%. Selected Apex Level-1 Trauma Center.",
    val hospitalName: String = "Apex Trauma & Critical Care Center",
    val hospitalAddress: String = "450 Medical Heights Center, SF",
    val ambulanceDriver: String = "Marcus Vance (ICU Specialist)",
    val ambulanceVehicle: String = "RapidResponse ICU Unit #7",
    val ambulanceEta: String = "4 mins",
    val totalCost: Double = 2950.00,
    val insuranceCovered: Double = 2350.00,
    val netPaid: Double = 600.00,
    val pravaTxId: String = "TXN-PRAVA-883920194",
    val status: String = "DISPATCHED"
)

@Entity(tableName = "emergency_contacts")
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val isPrimary: Boolean = false
)

data class Hospital(
    val id: String,
    val name: String,
    val distanceKm: Double,
    val etaMins: Int,
    val traumaCapability: String,
    val rating: Float,
    val priceDeposit: Double,
    val isAiRecommended: Boolean = false
)

data class Ambulance(
    val id: String,
    val unitName: String,
    val driverName: String,
    val vehicleType: String,
    val etaMins: Int,
    val equipmentLevel: String
) {
    val name: String get() = unitName
}

sealed class PravaPaymentState {
    object Idle : PravaPaymentState()
    object VerifyingSignature : PravaPaymentState()
    object AuthorizingSandbox : PravaPaymentState()
    object ReservingDeposit : PravaPaymentState()
    data class Success(val transactionId: String, val amount: Double, val timestamp: Long) : PravaPaymentState()
    data class Error(val message: String) : PravaPaymentState()
}
