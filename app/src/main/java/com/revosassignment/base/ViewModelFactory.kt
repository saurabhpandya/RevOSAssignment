package com.mobiquityassignment.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revosassignment.data.dashboard.DashboardNetworkDataProvider
import com.revosassignment.data.dashboard.DashboardRepository
import com.revosassignment.ui.booking.viewmodel.BookingHistoryViewModel
import com.revosassignment.ui.booking.viewmodel.BookingViewModel
import com.revosassignment.ui.booking.viewmodel.VehicleInfoViewModel
import com.revosassignment.ui.dashboard.viewmodel.DashboardViewModel

class ViewModelFactory
<T>(private val dataProvider: T, private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(
                application,
                DashboardRepository(dataProvider as DashboardNetworkDataProvider)
            ) as T
        } else if (modelClass.isAssignableFrom(VehicleInfoViewModel::class.java)) {
            return VehicleInfoViewModel(
                application
            ) as T
        } else if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(
                application
            ) as T
        } else if (modelClass.isAssignableFrom(BookingHistoryViewModel::class.java)) {
            return BookingHistoryViewModel(
                application
            ) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}