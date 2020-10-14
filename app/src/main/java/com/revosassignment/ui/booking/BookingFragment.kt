package com.revosassignment.ui.booking

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mobiquityassignment.base.BaseFragment
import com.mobiquityassignment.base.ViewModelFactory
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel
import com.revosassignment.R
import com.revosassignment.constant.Constants
import com.revosassignment.data.dashboard.DashboardNetworkDataProvider
import com.revosassignment.databinding.FragmentBookingBinding
import com.revosassignment.ui.booking.viewmodel.BookingViewModel
import com.revosassignment.utility.Status
import com.revosassignment.utility.showToast
import java.text.SimpleDateFormat
import java.util.*


class BookingFragment : BaseFragment(), View.OnClickListener {
    private val TAG = this::class.java.canonicalName

    lateinit var binding: FragmentBookingBinding

    lateinit var viewModel: BookingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setListener()
        setupObserver()
        getData()
        setData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                DashboardNetworkDataProvider(),
                this.requireActivity().application
            )
        ).get(BookingViewModel::class.java)
        binding.vm = viewModel
        binding.bookingInfoModel = viewModel.bookingInfoModel
        binding.executePendingBindings()
    }

    private fun setListener() {
        binding.tiedttxtDob.setOnClickListener(this)
    }

    private fun setupObserver() {
        viewModel.saveBookingInfoLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.ERROR -> {
                    showProgress(false)
                    requireActivity().showToast(it.message!!)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    navigateToBookHistory()
                }
            }
        })
    }

    private fun getData() {
        if (arguments != null) {
            viewModel.zoneModel = arguments?.getParcelable<ZoneModel>(Constants.BUNDLE_ZONE_INFO)!!
            viewModel.vehicleModel =
                arguments?.getParcelable<VehicleModel>(Constants.BUNDLE_VEHICLE_INFO)!!
            viewModel.tariffModel =
                arguments?.getParcelable<TariffModel>(Constants.BUNDLE_TARIFF)!!
        }
    }

    private fun setData() {
        binding.txtvwZoneLocation.text = "Location: ${viewModel.zoneModel.location}"

        binding.txtvwVehicleId.text = "Vehicle Id: ${viewModel.vehicleModel.id}"

        binding.txtvwVehicleBatteryStatus.text =
            "Battery Status: ${viewModel.vehicleModel.battery_per}%"

        var kmRange = ""
        if (viewModel.vehicleModel.km_range!! > 999) {
            var kmRangeDouble = viewModel.vehicleModel.km_range!! / 1000
            kmRange = "Range: ${kmRangeDouble} km"
        } else {
            kmRange = "Range: ${viewModel.vehicleModel.km_range} m"
        }
        binding.txtvwVehicleRunningRange.text = kmRange
        setTariffData(viewModel.tariffModel)

        viewModel.bookingInfoModel.tariffId = viewModel.tariffModel.id
        viewModel.bookingInfoModel.vehicleId = viewModel.vehicleModel.id
        viewModel.bookingInfoModel.zoneId = viewModel.zoneModel.id

    }

    private fun setTariffData(tariff: TariffModel) {
        binding.txtvwRideBaseFare.text = "Base Fare : ₹ ${tariff.base_fare}"
        binding.txtvwRideChargePerMin.text = "Every Minute : ₹ ${tariff.per_minute}"
        binding.txtvwRidePauseChargesVal.text =
            "Every ${tariff.pause_charge_per} : ₹ ${tariff.pause_charge}"
    }

    private fun openCalender() {
        val myCalendar: Calendar = Calendar.getInstance()
        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US)
                val timeStamp: String = sdf.format(myCalendar.time)
                Log.d(TAG, "selected Date: $timeStamp")
                viewModel.bookingInfoModel.dob = timeStamp
                binding.tiedttxtDob.setText(timeStamp)
            }

        val today = Date()
        val calMinDate: Calendar = Calendar.getInstance()
        calMinDate.time = today
        calMinDate.add(Calendar.YEAR, -55)
        val datePickerDialog = DatePickerDialog(
            requireActivity(), date, calMinDate[Calendar.YEAR], calMinDate[Calendar.MONTH],
            calMinDate[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = calMinDate.time.time

        val calMaxDate: Calendar = Calendar.getInstance()
        calMaxDate.time = today
        calMaxDate.add(Calendar.YEAR, -16)
        datePickerDialog.datePicker.maxDate = calMaxDate.time.time

        datePickerDialog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tiedttxt_dob -> {
                openCalender()
            }
        }
    }

    private fun navigateTo(bookingNavigation: BookingNavigation, bundle: Bundle?) {
        when (bookingNavigation) {
            BookingNavigation.BOOKING_HISTORY -> {
                view?.findNavController()
                    ?.navigate(R.id.action_bookingFragment_to_bookingHistoryFragment, bundle)
            }
        }
    }

    private fun navigateToBookHistory() {
        navigateTo(BookingNavigation.BOOKING_HISTORY, null)
    }

}

enum class BookingNavigation {
    BOOKING_HISTORY
}