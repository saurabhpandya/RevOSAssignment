package com.ravos.data.vehicles

import androidx.room.*
import com.ravos.data.model.VehicleModel

@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehiclemodel")
    suspend fun getAll(): List<VehicleModel>

    @Query("SELECT * FROM vehiclemodel where zone_id = :zoneId")
    suspend fun getByZoneId(zoneId: String): List<VehicleModel>

    @Insert
    suspend fun insertAll(vehicles: List<VehicleModel>)

    @Delete
    suspend fun delete(deleteVehicle: VehicleModel)

    @Query("DELETE FROM vehiclemodel")
    suspend fun deleteAll()

    @Update
    suspend fun update(updateVehicle: VehicleModel)
}