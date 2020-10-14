package com.ravos.data.zone

import com.fasterxml.jackson.databind.ObjectMapper
import com.ravos.RevOS
import com.ravos.data.model.ZoneModel
import com.ravos.data.model.ZonesModel
import com.ravos.utility.JsonExtractor

class ZoneRepository(private val zoneDataProvider: ZoneDBDataProvider) {

    private val TAG = this::class.java.canonicalName

    suspend fun getZones(): ArrayList<ZoneModel> {
        saveZones()
        return zoneDataProvider.getZones()
    }

    suspend fun saveZones() {
        val jsonExtractor = JsonExtractor(RevOS.context)
        val zonesJsonData = jsonExtractor.loadJSONFromAsset("revos_zones.json")
        if (!zonesJsonData.isNullOrEmpty()) {
            val objectMapper = ObjectMapper()
            val zonesModel = objectMapper.readValue(zonesJsonData, ZonesModel::class.java)
            val arylstZones = zonesModel.zones
            zoneDataProvider.insertAll(arylstZones)
        }
    }

}