package com.ravos.data.zone

import com.ravos.data.model.ZoneModel
import com.ravos.db.AppDatabase
import org.json.JSONException

class ZoneDBDataProvider(private val appDatabase: AppDatabase) : ZoneDataProvider {

    override suspend fun getZones(): ArrayList<ZoneModel> =
        appDatabase.zoneDao().getAll() as ArrayList<ZoneModel>

    override suspend fun insertAll(arylstZones: ArrayList<ZoneModel>) {
        try {
            appDatabase.zoneDao().deleteAll()
            appDatabase.zoneDao().insertAll(arylstZones)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override suspend fun udpateZone(updateZone: ZoneModel) {

    }

    override suspend fun deleteZone(deleteZone: ZoneModel) {

    }


}