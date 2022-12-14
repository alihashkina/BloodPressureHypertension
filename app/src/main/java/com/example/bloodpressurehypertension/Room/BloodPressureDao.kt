package com.example.bloodpressurehypertension.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BloodPressureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBloodPressure(bloodPressureItem: BloodPressureItem)

    @Delete
    suspend fun deleteBloodPressure(bloodPressureItem: BloodPressureItem)

    @Query("SELECT * FROM blood_pressure_db ORDER BY years, month, days, hours, minute ASC")
    fun fetchAllBloodPressure(): Flow<List<BloodPressureItem>>
}