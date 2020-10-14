package com.revosassignment.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mobiquityassignment.base.BaseFragment
import com.mobiquityassignment.base.ViewModelFactory
import com.ravos.data.booking.BOOKING_STATUS
import com.ravos.data.model.VehicleModel
import com.revosassignment.R
import com.revosassignment.constant.Constants.Companion.BUNDLE_VEHICLE_INFO
import com.revosassignment.constant.Constants.Companion.BUNDLE_ZONE_INFO
import com.revosassignment.data.dashboard.DashboardNetworkDataProvider
import com.revosassignment.databinding.FragmentDashboardBinding
import com.revosassignment.ui.dashboard.viewmodel.DashboardViewModel
import com.revosassignment.utility.OnItemClickListener
import com.revosassignment.utility.Status
import com.revosassignment.utility.Utility
import com.revosassignment.utility.Utility.Companion.getMinutesFromMills
import com.revosassignment.utility.showToast

class DashboardFragment : BaseFragment(), OnMapReadyCallback, LocationListener,
    OnItemClickListener, View.OnClickListener {

    private val TAG = this::class.java.canonicalName

    lateinit var binding: FragmentDashboardBinding

    lateinit var viewModel: DashboardViewModel

    private lateinit var mMap: GoogleMap

    private var locationManager: LocationManager? = null

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    internal val UPI_PAYMENT = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initMap()
        setupBottomSheet()
        setupViewModel()
        setupRecyclerView()
        getData()
        setupObserver()
        setListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (locationManager != null)
            locationManager!!.removeUpdates(this)

    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkForPermission()
    }

    private fun setupBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.imgvwExpandCollapse.setImageResource(R.drawable.ic_collapse)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.imgvwExpandCollapse.setImageResource(R.drawable.ic_expand)
                    }
                }
            }

        })
    }

    private fun checkForPermission() {
        Dexter.withContext(requireActivity())
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        initLocationListener()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            }).check()
    }

    private fun initLocationListener() {
        // setuping locatiomanager to perfrom location related operations
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Requesting locationmanager for location updates
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        Log.d(TAG, "initLocationListener")
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 1, 1f, this
        )
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                DashboardNetworkDataProvider(),
                this.requireActivity().application
            )
        ).get(DashboardViewModel::class.java)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    private fun setupRecyclerView() {
        setupRecyclerViewForZones()
        setupRecyclerViewForZoneVehicles()
    }

    private fun setupRecyclerViewForZones() {
        binding.rcyclvwZones.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        viewModel.zonesAdapter.onItemClickListner = this
        binding.rcyclvwZones.adapter = viewModel.zonesAdapter
    }

    private fun setupRecyclerViewForZoneVehicles() {
        binding.rcyclvwVehicles.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        viewModel.zoneVehiclesAdapter.onItemClickListner = this
        binding.rcyclvwVehicles.adapter = viewModel.zoneVehiclesAdapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true

    }

    override fun onLocationChanged(location: Location) {
        if (viewModel.moveCamera) {
            val latlng = LatLng(location.latitude, location.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10.0f))
        }

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    private fun getData() {
        viewModel.getActiveBooking()
    }

    override fun onItemClickListner(pos: Int, isZone: Boolean) {
        if (isZone) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.txtvwZoneDetails.visibility = View.VISIBLE
            viewModel.selectedZoneModel = viewModel.getZoneList()[pos]
            viewModel.getVehicles(viewModel.selectedZoneModel.id)
        } else {
            val selectedVehicleModel = viewModel.getZoneVehicleList()[pos]
            navigateToVehicleInfo(selectedVehicleModel)
        }
    }

    private fun navigateTo(dashboardNavigation: DashboardNavigation, bundle: Bundle?) {
        when (dashboardNavigation) {
            DashboardNavigation.VEHICLE_INFO -> {
                view?.findNavController()
                    ?.navigate(R.id.action_dashboardFragment_to_vehicleInfoFragment, bundle)
            }
            DashboardNavigation.BOOK_HISTORY -> {
                view?.findNavController()
                    ?.navigate(R.id.action_dashboardFragment_to_bookingHistoryFragment, bundle)
            }
        }
    }

    private fun navigateToVehicleInfo(selectedVehicleModel: VehicleModel) {
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_VEHICLE_INFO, selectedVehicleModel)
        bundle.putParcelable(BUNDLE_ZONE_INFO, viewModel.selectedZoneModel)
        navigateTo(DashboardNavigation.VEHICLE_INFO, bundle)
    }

    private fun navigateToBookingHistory() {
        navigateTo(DashboardNavigation.BOOK_HISTORY, null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_book_history -> {
                navigateToBookingHistory()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setupMarker() {

        // Clears the previously touched position
        mMap.clear()

        val latLngBound =
            LatLngBounds.Builder()

        for (zone in viewModel.getZoneList()) {
            val zoneLocation = LatLng(zone.latitude!!, zone.longitude!!)
            latLngBound.include(zoneLocation)
            // Creating a marker
            val markerOptions = MarkerOptions()
            // Setting the position for the marker
            markerOptions.position(zoneLocation)
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(zone.location)
            // Placing a marker on the touched position
            mMap.addMarker(markerOptions)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBound.build(), 100))
    }

    private fun setupObserver() {
        viewModel.zoneLiveData.observe(viewLifecycleOwner, Observer {
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
                    if (it.data != null) {
                        if (it.data) {
                            setupMarker()
                        }
                    }
                }
            }
        })
        viewModel.activeBookingInfoLiveData.observe(viewLifecycleOwner, Observer {
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
                    if (it.data != null) {

                        if (this::mMap.isInitialized)
                            mMap.clear()

                        if (!it.data) {
                            viewModel.moveCamera = false
                            viewModel.getZones()
                            binding.cnstrntlytActiveBooking.visibility = View.GONE
                            binding.imgvwExpandCollapse.visibility = View.VISIBLE
                        } else {
                            viewModel.moveCamera = true
                            binding.imgvwExpandCollapse.visibility = View.GONE
                            binding.cnstrntlytActiveBooking.visibility = View.VISIBLE
                            setActiveBookingData()
                            viewModel.getTariff(viewModel.activeBookingInfoModel?.tariffId!!)
                        }
                    }
                }
            }
        })

        viewModel.completeRideLiveData.observe(viewLifecycleOwner, Observer {
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
                    if (it.data!!) {
                        viewModel.getActiveBooking()
                    }
                }
            }
        })

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
                }
            }
        })

    }

    private fun setListener() {
        binding.btnCompleteRide.setOnClickListener(this)
    }

    private fun setActiveBookingData() {
        if (viewModel.activeBookingInfoModel != null) {
            binding.txtvwBookingId.text =
                "Booking Id: ${viewModel.activeBookingInfoModel!!.bookingId.toString()}"
            binding.txtvwBookingStatus.text = viewModel.activeBookingInfoModel!!.status
            binding.txtvwBookingUsername.text =
                "Name: ${viewModel.activeBookingInfoModel!!.userName}"
            binding.txtvwVehicleId.text =
                "Vehicle Id: ${viewModel.activeBookingInfoModel!!.vehicleId}"
            binding.txtvwZoneId.text = "Zone Id: ${viewModel.activeBookingInfoModel!!.zoneId}"

            if (viewModel.activeBookingInfoModel!!.status.equals(
                    BOOKING_STATUS.INITIATED.name,
                    true
                )
            ) {
                binding.txtvwBookingTime.text = viewModel.activeBookingInfoModel!!.ride_start_time
            } else {
                binding.txtvwBookingTime.text = viewModel.activeBookingInfoModel!!.ride_end_time
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_complete_ride -> {
                showCompleteDialog()

            }
        }
    }

    private fun showPaymentInfo() {
        val calculation = viewModel.calculateRideFare()
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Payment Summary")
        val message =
            "Total Ride minutes : ${calculation.first}\nTotal Payment for Ride: ${calculation.second}"
        builder.setMessage(message)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                val note =
                    "Ride Charge for ${viewModel.activeBookingInfoModel?.bookingId} from ${viewModel.activeBookingInfoModel?.userName}"
                val amount = "${calculation.second}"
                payUsingUpi("1", "saurabhpandya7@okaxis", "Saurabh", note)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->

            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun showCompleteDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Complete Ride")
        builder.setMessage("Are you sure you want to complete ride?")
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                showPaymentInfo()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->

            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        if (null != chooser.resolveActivity(requireActivity().packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            requireActivity().showToast("No UPI app found, please install one to continue")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            UPI_PAYMENT -> {
                if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                    if (data != null) {
                        val trxt = data.getStringExtra("response")
                        Log.d("UPI", "onActivityResult: $trxt")
                        val dataList = ArrayList<String>()
                        dataList.add(trxt)
                        upiPaymentDataOperation(dataList)
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null")
                        val dataList = ArrayList<String>()
                        dataList.add("nothing")
                        upiPaymentDataOperation(dataList)
                    }
                } else {
                    Log.d(
                        "UPI",
                        "onActivityResult: " + "Return data is null"
                    )
                    //when user simply back without payment
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
//                    upiPaymentDataOperation(dataList)
                }
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        var str: String? = data[0]
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
        var paymentCancel = ""
        if (str == null) str = "discard"
        var status = ""
        var approvalRefNo = ""
        val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in response.indices) {
            val equalStr =
                response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (equalStr.size >= 2) {
                if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                    status = equalStr[1].toLowerCase()
                } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                    approvalRefNo = equalStr[1]
                }
            } else {
                paymentCancel = "Payment cancelled by user."
            }
        }

        if (status == "success") {
            //Code to handle successful transaction here.
            requireActivity().showToast("Transaction successful.")
            Log.d("UPI", "responseStr: $approvalRefNo")
            viewModel.completeBooking()
        } else if ("Payment cancelled by user." == paymentCancel) {
            requireActivity().showToast("Payment cancelled by user.")
        } else {
            requireActivity().showToast("Transaction failed.Please try again")
        }

    }

}

enum class DashboardNavigation {
    VEHICLE_INFO,
    BOOK_HISTORY
}