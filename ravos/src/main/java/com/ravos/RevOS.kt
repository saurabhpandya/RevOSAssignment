package com.ravos

import android.content.Context
import android.util.Log
import com.fidato.phablecareassignment.db.DatabaseBuilder
import com.ravos.data.booking.BookingInfoDBDataProvider
import com.ravos.data.booking.BookingInfoRepository
import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel
import com.ravos.data.tariff.TariffDBDataProvider
import com.ravos.data.tariff.TariffRepository
import com.ravos.data.vehicles.VehicleDBDataProvider
import com.ravos.data.vehicles.VehicleRepository
import com.ravos.data.zone.ZoneDBDataProvider
import com.ravos.data.zone.ZoneRepository

object RevOS {

    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    suspend fun getZones(): ArrayList<ZoneModel> {
        var arylstZones = ArrayList<ZoneModel>()
        val zoneDataProvider = ZoneDBDataProvider(DatabaseBuilder.getInstance(context))
        val zoneRepository = ZoneRepository(zoneDataProvider)
        arylstZones = zoneRepository.getZones()
        return arylstZones
    }

    suspend fun getVehicles(): ArrayList<VehicleModel> {
        val vehicleDataProvider = VehicleDBDataProvider(DatabaseBuilder.getInstance(context))
        val vehicleRepository = VehicleRepository(vehicleDataProvider)
        val arylstZoneVehicles = vehicleRepository.getVehicles()
        return arylstZoneVehicles
    }

    suspend fun getVehicles(zoneId: String): ArrayList<VehicleModel> {
        val vehicleDataProvider = VehicleDBDataProvider(DatabaseBuilder.getInstance(context))
        val vehicleRepository = VehicleRepository(vehicleDataProvider)
        val arylstZoneVehicles = vehicleRepository.getVehicles(zoneId)
        return arylstZoneVehicles
    }

    suspend fun getBookings(): ArrayList<BookingInfoModel> {
        val bookingInfoDataProvider =
            BookingInfoDBDataProvider(DatabaseBuilder.getInstance(context))
        val bookingRepository = BookingInfoRepository(bookingInfoDataProvider)
        val arylstBookings = bookingRepository.getBookings()
        return arylstBookings
    }

    suspend fun getActiveBooking(): BookingInfoModel {
        val bookingInfoDataProvider =
            BookingInfoDBDataProvider(DatabaseBuilder.getInstance(context))
        val bookingRepository = BookingInfoRepository(bookingInfoDataProvider)
        val activeBooking = bookingRepository.getActiveBooking()
        Log.d("RevOS", "getActiveBooking:$activeBooking")
        return activeBooking
    }

    suspend fun getBooking(bookingId: String): ArrayList<BookingInfoModel> {
        val bookingInfoDataProvider =
            BookingInfoDBDataProvider(DatabaseBuilder.getInstance(context))
        val bookingRepository = BookingInfoRepository(bookingInfoDataProvider)
        val arylstBookings = bookingRepository.getBooking(bookingId)
        return arylstBookings
    }

    suspend fun saveBooking(bookingInfoModel: BookingInfoModel): LongArray {
        val bookingInfoDataProvider =
            BookingInfoDBDataProvider(DatabaseBuilder.getInstance(context))
        val bookingRepository = BookingInfoRepository(bookingInfoDataProvider)
        val arylstBookings = ArrayList<BookingInfoModel>()
        arylstBookings.add(bookingInfoModel)
        return bookingRepository.saveBooking(arylstBookings)
    }

    suspend fun updateBooking(bookingInfoModel: BookingInfoModel) {
        val bookingInfoDataProvider =
            BookingInfoDBDataProvider(DatabaseBuilder.getInstance(context))
        val bookingRepository = BookingInfoRepository(bookingInfoDataProvider)
        return bookingRepository.updateBooking(bookingInfoModel)
    }

    suspend fun getTariffs(): ArrayList<TariffModel> {
        var arylstTariffs = ArrayList<TariffModel>()
        val tariffDataProvider = TariffDBDataProvider(DatabaseBuilder.getInstance(context))
        val tariffRepository = TariffRepository(tariffDataProvider)
        arylstTariffs = tariffRepository.getTariffs()
        return arylstTariffs
    }

    suspend fun getTariff(tariffId: String): ArrayList<TariffModel> {
        var arylstTariffs = ArrayList<TariffModel>()
        val tariffDataProvider = TariffDBDataProvider(DatabaseBuilder.getInstance(context))
        val tariffRepository = TariffRepository(tariffDataProvider)
        arylstTariffs = tariffRepository.getTariff(tariffId)
        return arylstTariffs
    }

    private suspend fun saveTariffs() {
        val tariffDataProvider = TariffDBDataProvider(DatabaseBuilder.getInstance(context))
        val tariffRepository = TariffRepository(tariffDataProvider)
        tariffRepository.saveTariff()
    }

}