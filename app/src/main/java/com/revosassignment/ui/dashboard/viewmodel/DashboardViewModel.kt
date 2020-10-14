package com.revosassignment.ui.dashboard.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobiquityassignment.base.BaseViewModel
import com.ravos.RevOS
import com.ravos.data.booking.BOOKING_STATUS
import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel
import com.revosassignment.data.dashboard.DashboardRepository
import com.revosassignment.ui.dashboard.adapter.ZoneVehiclesAdapter
import com.revosassignment.ui.dashboard.adapter.ZonesAdapter
import com.revosassignment.utility.Resource
import com.revosassignment.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(
    application: Application,
    dashboardRepository: DashboardRepository
) : BaseViewModel(application) {
    private val TAG = this::class.java.canonicalName

    private var arylstZones = ArrayList<ZoneModel>()
    val zonesAdapter = ZonesAdapter(arylstZones)

    private var arylstZoneVehicles = ArrayList<VehicleModel>()
    val zoneVehiclesAdapter = ZoneVehiclesAdapter(arylstZoneVehicles)

    var selectedZoneModel = ZoneModel()

    var activeBookingInfoLiveData = MutableLiveData<Resource<Boolean>>()

    var activeBookingInfoModel: BookingInfoModel? = null

    var completeRideLiveData = MutableLiveData<Resource<Boolean>>()

    var zoneLiveData = MutableLiveData<Resource<Boolean>>()

    var moveCamera = false

    var tariffLiveData = MutableLiveData<Resource<Boolean>>()

    var tariffModel = TariffModel(id = "")

    fun getZones() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            activeBookingInfoLiveData.value = Resource.loading(null)
        }
        try {
            arylstZones = RevOS.getZones()
            Log.d(TAG, "Zones : $arylstZones")
            withContext(Dispatchers.Main) {
                zonesAdapter.setZones(arylstZones)
                zoneLiveData.value = Resource.success(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                zoneLiveData.value = Resource.error(null, e.localizedMessage)
            }
        }

    }

    fun getZoneList() = arylstZones

    fun getVehicles(zoneId: String) = viewModelScope.launch(Dispatchers.IO) {
        arylstZoneVehicles = RevOS.getVehicles(zoneId)
        Log.d(TAG, "Zone Vehicles : $arylstZoneVehicles")
        withContext(Dispatchers.Main) {
            zoneVehiclesAdapter.setZoneVehicles(arylstZoneVehicles)
        }
    }

    fun getZoneVehicleList() = arylstZoneVehicles

    fun getActiveBooking() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            activeBookingInfoLiveData.value = Resource.loading(null)
        }
        try {

            activeBookingInfoModel = RevOS.getActiveBooking()
            Log.d(TAG, "getActiveBooking:activeBookingInfo::$activeBookingInfoModel")

            withContext(Dispatchers.Main) {
                if (activeBookingInfoModel == null) {
                    activeBookingInfoLiveData.value = Resource.success(false)
                } else {
                    activeBookingInfoLiveData.value = Resource.success(true)
                }


            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                activeBookingInfoLiveData.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    fun completeBooking() =
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                completeRideLiveData.value = Resource.loading(null)
            }
            try {
                activeBookingInfoModel?.status = BOOKING_STATUS.COMPLETED.name
                activeBookingInfoModel?.ride_end_time = Utility.getCurrentDate()
                RevOS.updateBooking(activeBookingInfoModel!!)

                withContext(Dispatchers.Main) {
                    completeRideLiveData.value = Resource.success(true)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    completeRideLiveData.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    fun getTariff(tariffId: String) = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            tariffLiveData.value = Resource.loading(null)
        }
        try {
            val arylstTariffs = RevOS.getTariff(tariffId)
            if (!arylstTariffs.isNullOrEmpty()) {
                tariffModel = arylstTariffs[0]
            }

            withContext(Dispatchers.Main) {
                tariffLiveData.value = Resource.success(true)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                tariffLiveData.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    fun calculateRideFare(): Pair<Int, Double> {
        val strStartDate = activeBookingInfoModel?.ride_start_time
        val strEndDate = Utility.getCurrentDate()
        val dateStart = Utility.convertStringToDate(strStartDate!!)
        val dateEnd = Utility.convertStringToDate(strEndDate)
        val diffTime = dateEnd.time - dateStart.time
        val minutes = Utility.getMinutesFromMills(diffTime)
        Log.d(TAG, "showPaymentInfo:Duration in Minutes: $minutes")

        var totalFare = 0.0
        if (tariffModel.base_fare != null) {
            if (tariffModel.per_minute != null) {
                val rideFare = minutes * tariffModel.per_minute!!
                totalFare = tariffModel.base_fare!! + rideFare
            }
        }
        return Pair(minutes, totalFare)
    }

}