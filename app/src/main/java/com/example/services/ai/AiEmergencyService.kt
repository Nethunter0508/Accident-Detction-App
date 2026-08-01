package com.example.services.ai

import com.example.BuildConfig
import com.example.data.models.Ambulance
import com.example.data.models.Hospital
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AiAnalysisResult(
    val severity: String,
    val confidenceScore: Int,
    val reasoningText: String,
    val hospitalRecommendation: Hospital,
    val ambulanceRecommendation: Ambulance,
    val riskScore: String,
    val incidentSummary: String
)

class AiEmergencyService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val sampleHospitals = listOf(
        Hospital(
            id = "h1",
            name = "Apex Trauma & Critical Care Center",
            distanceKm = 2.4,
            etaMins = 5,
            traumaCapability = "Level 1 Trauma Center • 24/7 Surgical ICU",
            rating = 4.9f,
            priceDeposit = 2500.0,
            isAiRecommended = true
        ),
        Hospital(
            id = "h2",
            name = "Stanford Emergency Medical Hub",
            distanceKm = 4.8,
            etaMins = 9,
            traumaCapability = "Level 1 Trauma • Neurosurgery Dept",
            rating = 4.8f,
            priceDeposit = 2800.0,
            isAiRecommended = false
        ),
        Hospital(
            id = "h3",
            name = "Bay Area Community Hospital",
            distanceKm = 6.1,
            etaMins = 12,
            traumaCapability = "Level 2 Trauma • General Emergency",
            rating = 4.5f,
            priceDeposit = 1800.0,
            isAiRecommended = false
        ),
        Hospital(
            id = "h4",
            name = "Mercy Emergency & Cardiac Institute",
            distanceKm = 8.3,
            etaMins = 16,
            traumaCapability = "Level 2 Trauma • Acute Care",
            rating = 4.6f,
            priceDeposit = 2100.0,
            isAiRecommended = false
        ),
        Hospital(
            id = "h5",
            name = "Pacific Heights Care Facility",
            distanceKm = 11.0,
            etaMins = 21,
            traumaCapability = "Urgent Care & Stabilization",
            rating = 4.2f,
            priceDeposit = 1500.0,
            isAiRecommended = false
        )
    )

    val sampleAmbulances = listOf(
        Ambulance(
            id = "a1",
            unitName = "RapidResponse ICU Unit #7",
            driverName = "Marcus Vance (Paramedic Specialist)",
            vehicleType = "Mobile ICU Ambulance (ALS)",
            etaMins = 4,
            equipmentLevel = "Full Mechanical Ventilator + Defibrillator"
        ),
        Ambulance(
            id = "a2",
            unitName = "Metro Emergency Unit #12",
            driverName = "Sarah Jenkins",
            vehicleType = "Standard Trauma Van (BLS)",
            etaMins = 7,
            equipmentLevel = "Oxygen & Basic Cardiac Monitoring"
        )
    )

    suspend fun analyzeAccident(
        gForce: Float,
        speedKmH: Float,
        userResponded: Boolean
    ): AiAnalysisResult = withContext(Dispatchers.IO) {
        // Simulated AI delay to showcase AI reasoning thinking animation
        delay(2000)

        var geminiReasoning: String? = null
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNotEmpty() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                geminiReasoning = fetchGeminiReasoning(apiKey, gForce, speedKmH, userResponded)
            } catch (e: Exception) {
                geminiReasoning = null
            }
        }

        val reasoning = geminiReasoning ?: "Vehicle experienced a high-impact collision ($gForce g) at $speedKmH km/h. Occupant unresponsive to automated safety verification. Emergency confidence is 97%. Apex Level-1 Trauma Center and RapidResponse ICU Unit selected for immediate dispatch."

        AiAnalysisResult(
            severity = "CRITICAL - HIGH IMPACT",
            confidenceScore = 97,
            reasoningText = reasoning,
            hospitalRecommendation = sampleHospitals.first(),
            ambulanceRecommendation = sampleAmbulances.first(),
            riskScore = "9.8 / 10",
            incidentSummary = "Severe kinetic decelerative force recorded ($gForce g). High probability of occupant trauma. Emergency protocol activated automatically."
        )
    }

    private fun fetchGeminiReasoning(
        apiKey: String,
        gForce: Float,
        speedKmH: Float,
        userResponded: Boolean
    ): String {
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val prompt = "You are Guardian AI, an advanced emergency response AI system. Synthesize a 3-sentence technical emergency analysis for a vehicle collision detected at $gForce G-force and $speedKmH km/h. User response: ${if (userResponded) "Responsive" else "Unresponsive"}. Explain confidence score 97%, severity, and trauma hospital selection."

        val jsonBody = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody(jsonMediaType))
            .build()

        val response = client.newCall(request).execute()
        val bodyStr = response.body?.string() ?: return ""
        val responseJson = JSONObject(bodyStr)
        val candidates = responseJson.optJSONArray("candidates")
        val content = candidates?.optJSONObject(0)?.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        return parts?.optJSONObject(0)?.optString("text") ?: ""
    }
}
