package com.ravos.data.tariff

import com.fasterxml.jackson.databind.ObjectMapper
import com.ravos.RevOS
import com.ravos.data.model.*
import com.ravos.utility.JsonExtractor

class TariffRepository(private val tariffDataProvider: TariffDBDataProvider) {
    private val TAG = this::class.java.canonicalName

    suspend fun getTariffs(): ArrayList<TariffModel> {
        saveTariff()
        return tariffDataProvider.getTariffs()
    }

    suspend fun getTariff(tariffId: String): ArrayList<TariffModel> {
        saveTariff()
        return tariffDataProvider.getTariff(tariffId)
    }

    suspend fun saveTariff() {
        val jsonExtractor = JsonExtractor(RevOS.context)
        val tariffJsonData = jsonExtractor.loadJSONFromAsset("revos_tariff.json")
        if (!tariffJsonData.isNullOrEmpty()) {
            val objectMapper = ObjectMapper()
            val tariffModel =
                objectMapper.readValue(tariffJsonData, TariffListModel::class.java)
            if (!tariffModel.tariffs.isEmpty()) {
                val tariffs = tariffModel.tariffs
                tariffDataProvider.insertAll(tariffs)
            }
        }

    }


}