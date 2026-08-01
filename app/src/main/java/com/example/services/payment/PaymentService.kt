package com.example.services.payment

import com.example.data.models.PravaPaymentState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

interface PaymentService {
    fun processEmergencyAutoPay(
        merchantName: String,
        hospitalName: String,
        payableAmount: Double,
        insurancePolicyNo: String,
        emergencyLimit: Double
    ): Flow<PravaPaymentState>
}

class MockPravaSandboxService : PaymentService {
    override fun processEmergencyAutoPay(
        merchantName: String,
        hospitalName: String,
        payableAmount: Double,
        insurancePolicyNo: String,
        emergencyLimit: Double
    ): Flow<PravaPaymentState> = flow {
        emit(PravaPaymentState.VerifyingSignature)
        delay(1200)

        emit(PravaPaymentState.AuthorizingSandbox)
        delay(1500)

        emit(PravaPaymentState.ReservingDeposit)
        delay(1400)

        val generatedTxId = "TXN-PRAVA-" + UUID.randomUUID().toString().take(9).uppercase()
        emit(
            PravaPaymentState.Success(
                transactionId = generatedTxId,
                amount = payableAmount,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}
