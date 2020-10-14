package com.revosassignment.ui.booking.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobiquityassignment.base.BaseViewModel
import com.ravos.RevOS
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel
import com.revosassignment.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehicleInfoViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = this::class.java.canonicalName

    var zoneModel = ZoneModel()
    var vehicleModel = VehicleModel("")
    var tariffModel = TariffModel("")

    private var arylstTariff = ArrayList<TariffModel>()

    var tariffLiveData = MutableLiveData<Resource<TariffModel>>()

    fun getTariffs() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            tariffLiveData.value = Resource.loading(null)
        }
        try {
            arylstTariff = RevOS.getTariffs()
            Log.d(TAG, "Tariff : $arylstTariff")
            if (!arylstTariff.isEmpty()) {
                tariffModel = arylstTariff[0]
                withContext(Dispatchers.Main) {
                    tariffLiveData.value = Resource.success(tariffModel)
                }
            } else {
                withContext(Dispatchers.Main) {
                    tariffLiveData.value = Resource.error(null, "No tariff found")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                tariffLiveData.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

}