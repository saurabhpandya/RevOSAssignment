package com.ravos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ravos.data.booking.BookingInfoDao
import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel
import com.ravos.data.tariff.TariffDao
import com.ravos.data.vehicles.VehicleDao
import com.ravos.data.zone.ZoneDao


@Database(
    entities = [ZoneModel::class, VehicleModel::class, BookingInfoModel::class, TariffModel::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun zoneDao(): ZoneDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun bookingInfoDao(): BookingInfoDao
    abstract fun tariffDao(): TariffDao
}