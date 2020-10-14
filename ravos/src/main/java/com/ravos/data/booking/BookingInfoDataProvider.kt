package com.ravos.data.booking

import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel

interface BookingInfoDataProvider {

    suspend fun getBookings(): ArrayList<BookingInfoModel>

    suspend fun getBookingByStatus(status: String): BookingInfoModel

    suspend fun getBooking(bookingId: String): ArrayList<BookingInfoModel>

    suspend fun insertAll(arylstBookings: ArrayList<BookingInfoModel>): LongArray

    suspend fun udpateBooking(updateBooking: BookingInfoModel)

    suspend fun deleteBooking(deleteBooking: BookingInfoModel)
}