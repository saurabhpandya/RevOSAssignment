package com.ravos.data.tariff

import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
import com.ravos.db.AppDatabase

class TariffDBDataProvider(private val appDatabase: AppDatabase) : TariffDataProvider {
    private val TAG = this::class.java.canonicalName

    override suspend fun getTariffs() =
        appDatabase.tariffDao().getAll() as ArrayList<TariffModel>

    override suspend fun getTariff(bookingId: String) =
        appDatabase.tariffDao().getById(bookingId) as ArrayList<TariffModel>

    override suspend fun insertAll(arylstTariff: ArrayList<TariffModel>) {
        appDatabase.tariffDao().deleteAll()
        appDatabase.tariffDao().insertAll(arylstTariff)
    }

    override suspend fun udpateTariff(updateBooking: TariffModel) {

    }

    override suspend fun deleteTariff(deleteBooking: TariffModel) {

    }
}