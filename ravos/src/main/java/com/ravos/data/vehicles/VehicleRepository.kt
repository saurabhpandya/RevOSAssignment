package com.ravos.data.vehicles

import com.fasterxml.jackson.databind.ObjectMapper
import com.ravos.RevOS
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneVehicleModel
import com.ravos.utility.JsonExtractor

class VehicleRepository(private val vehicleDataProvider: VehicleDBDataProvider) {
    private val TAG = this::class.java.canonicalName

    suspend fun getVehicles(): ArrayList<VehicleModel> {
        saveVehicles()
        return vehicleDataProvider.getVehicles()
    }

    suspend fun getVehicles(zoneId: String): ArrayList<VehicleModel> {
        saveVehicles()
        return vehicleDataProvider.getVehicles(zoneId)
    }

    suspend fun saveVehicles() {
        val jsonExtractor = JsonExtractor(RevOS.context)
        val zoneVechicleJsonData =
            jsonExtractor.loadJSONFromAsset("revos_zone_vehicles.json")
        if (!zoneVechicleJsonData.isNullOrEmpty()) {
            val objectMapper = ObjectMapper()
            val zoneVehiclesModel =
                objectMapper.readValue(zoneVechicleJsonData, ZoneVehicleModel::class.java)
            if (!zoneVehiclesModel.vehicles.isEmpty()) {
                val zoneVehicles = zoneVehiclesModel.vehicles
                vehicleDataProvider.insertAll(zoneVehicles)
            }
        }
    }


}