package com.ravos.data.vehicles

import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel

interface VehicleDataProvider {
    suspend fun getVehicles(): ArrayList<VehicleModel>
    suspend fun getVehicles(zoneId: String): ArrayList<VehicleModel>

    suspend fun insertAll(arylstVehicle: ArrayList<VehicleModel>)

    suspend fun udpateVehicle(updateVehicle: VehicleModel)

    suspend fun deleteVehicle(deleteVehicle: VehicleModel)
}