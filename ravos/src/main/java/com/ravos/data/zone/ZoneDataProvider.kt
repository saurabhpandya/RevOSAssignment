package com.ravos.data.zone

import com.ravos.data.model.ZoneModel

interface ZoneDataProvider {

    suspend fun getZones(): ArrayList<ZoneModel>

    suspend fun insertAll(arylstZones: ArrayList<ZoneModel>)

    suspend fun udpateZone(updateZone: ZoneModel)

    suspend fun deleteZone(deleteZone: ZoneModel)

}