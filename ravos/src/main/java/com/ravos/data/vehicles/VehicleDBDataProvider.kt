package com.ravos.data.vehicles

import com.ravos.data.model.VehicleModel
import com.ravos.db.AppDatabase

class VehicleDBDataProvider(private val appDatabase: AppDatabase) : VehicleDataProvider {
    private val TAG = this::class.java.canonicalName

    override suspend fun getVehicles() =
        appDatabase.vehicleDao().getAll() as ArrayList<VehicleModel>

    override suspend fun getVehicles(zoneId: String) =
        appDatabase.vehicleDao().getByZoneId(zoneId) as ArrayList<VehicleModel>

    override suspend fun insertAll(arylstVehicle: ArrayList<VehicleModel>) {
        appDatabase.vehicleDao().deleteAll()
        appDatabase.vehicleDao().insertAll(arylstVehicle)
    }

    override suspend fun udpateVehicle(updateVehicle: VehicleModel) {

    }

    override suspend fun deleteVehicle(deleteVehicle: VehicleModel) {

    }
}