package com.ravos.data.booking

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.ravos.RevOS
import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneVehicleModel
import com.ravos.utility.JsonExtractor

class BookingInfoRepository(private val bookingInfoDataProvider: BookingInfoDBDataProvider) {
    private val TAG = this::class.java.canonicalName

    suspend fun getBookings(): ArrayList<BookingInfoModel> {
        return bookingInfoDataProvider.getBookings()
    }

    suspend fun getActiveBooking(): BookingInfoModel {
        val status = BOOKING_STATUS.INITIATED.name
        Log.d(TAG, "BookingInfoRepository:getActiveBooking:status::$status")
        return bookingInfoDataProvider.getBookingByStatus(status)
    }

    suspend fun getBooking(bookingId: String): ArrayList<BookingInfoModel> {
        return bookingInfoDataProvider.getBooking(bookingId)
    }

    suspend fun saveBooking(arylstBooking: ArrayList<BookingInfoModel>): LongArray {
        return bookingInfoDataProvider.insertAll(arylstBooking)
    }

    suspend fun updateBooking(updateBooking: BookingInfoModel) {
        return bookingInfoDataProvider.udpateBooking(updateBooking)
    }


}

enum class BOOKING_STATUS {
    INITIATED,
    COMPLETED
}