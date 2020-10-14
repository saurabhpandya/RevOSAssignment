package com.ravos.data.tariff

import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel

interface TariffDataProvider {
    suspend fun getTariffs(): ArrayList<TariffModel>
    suspend fun getTariff(tariffId: String): ArrayList<TariffModel>

    suspend fun insertAll(arylstTariffs: ArrayList<TariffModel>)

    suspend fun udpateTariff(updateTariff: TariffModel)

    suspend fun deleteTariff(deleteTariff: TariffModel)
}