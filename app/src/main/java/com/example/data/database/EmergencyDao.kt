package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.EmergencyContact
import com.example.data.models.IncidentReport
import com.example.data.models.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyDao {
    @Query("SELECT * FROM trips ORDER BY timestamp DESC")
    fun getAllTrips(): Flow<List<Trip>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip): Long

    @Query("SELECT * FROM incident_reports ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentReport): Long

    @Query("SELECT * FROM emergency_contacts ORDER BY isPrimary DESC, id ASC")
    fun getAllContacts(): Flow<List<EmergencyContact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContact): Long

    @Query("DELETE FROM emergency_contacts WHERE id = :id")
    suspend fun deleteContact(id: Long)
}
