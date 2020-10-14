package com.revosassignment.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
import com.revosassignment.databinding.FragmentVehicleInfoBinding
import com.revosassignment.ui.booking.viewmodel.VehicleInfoViewModel
import com.revosassignment.ui.dashboard.DashboardNavigation
import com.revosassignment.utility.Status
import com.revosassignment.utility.showToast
import kotlinx.android.synthetic.main.fragment_vehicle_info.view.*

class VehicleInfoFragment : BaseFragment(), View.OnClickListener {

    private val TAG = this::class.java.canonicalName

    lateinit var binding: FragmentVehicleInfoBinding

    lateinit var viewModel: VehicleInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVehicleInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setListener()
        getData()
        setupObserver()
        setData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                DashboardNetworkDataProvider(),
                this.requireActivity().application
            )
        ).get(VehicleInfoViewModel::class.java)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    private fun setListener() {
        binding.btnProceed.setOnClickListener(this)
    }

    private fun getData() {
        if (arguments != null) {
            viewModel.zoneModel = arguments?.getParcelable<ZoneModel>(Constants.BUNDLE_ZONE_INFO)!!
            viewModel.vehicleModel =
                arguments?.getParcelable<VehicleModel>(Constants.BUNDLE_VEHICLE_INFO)!!
        }
        viewModel.getTariffs()
    }

    private fun setupObserver() {
        viewModel.tariffLiveData.observe(viewLifecycleOwner, Observer {
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
                    setTariffData(it.data!!)
                }
            }
        })
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
    }

    private fun setTariffData(tariff: TariffModel) {
        binding.txtvwRideBaseFare.text = "Base Fare : ₹ ${tariff.base_fare}"
        binding.txtvwRideChargePerMin.text = "Every Minute : ₹ ${tariff.per_minute}"
        binding.txtvwRidePauseChargesVal.text =
            "Every ${tariff.pause_charge_per} : ₹ ${tariff.pause_charge}"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_proceed -> {
                navigateToBookRide()
            }
        }
    }

    private fun navigateTo(vehicleInfoNavigation: VehicleInfoNavigation, bundle: Bundle?) {
        when (vehicleInfoNavigation) {
            VehicleInfoNavigation.BOOK_RIDE -> {
                view?.findNavController()
                    ?.navigate(R.id.action_vehicleInfoFragment_to_bookingFragment, bundle)
            }

        }
    }

    private fun navigateToBookRide() {
        val bundle = Bundle()
        bundle.putParcelable(Constants.BUNDLE_VEHICLE_INFO, viewModel.vehicleModel)
        bundle.putParcelable(Constants.BUNDLE_ZONE_INFO, viewModel.zoneModel)
        bundle.putParcelable(Constants.BUNDLE_TARIFF, viewModel.tariffModel)
        navigateTo(VehicleInfoNavigation.BOOK_RIDE, bundle)
    }

}

enum class VehicleInfoNavigation {
    BOOK_RIDE
}