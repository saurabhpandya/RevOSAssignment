package com.revosassignment.ui.booking

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquityassignment.base.BaseFragment
import com.mobiquityassignment.base.ViewModelFactory
import com.ravos.data.booking.BOOKING_STATUS
import com.revosassignment.data.dashboard.DashboardNetworkDataProvider
import com.revosassignment.databinding.FragmentBookingHistoryBinding
import com.revosassignment.ui.booking.viewmodel.BookingHistoryViewModel
import com.revosassignment.utility.OnItemClickListner
import com.revosassignment.utility.Status
import com.revosassignment.utility.showToast


class BookingHistoryFragment : BaseFragment(), OnItemClickListner {

    private val TAG = this::class.java.canonicalName

    lateinit var binding: FragmentBookingHistoryBinding

    lateinit var viewModel: BookingHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        initRecyclerView()
        setupObserver()
        getData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                DashboardNetworkDataProvider(),
                this.requireActivity().application
            )
        ).get(BookingHistoryViewModel::class.java)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    private fun initRecyclerView() {
        binding.rcyclrvwBookingHistory.layoutManager = LinearLayoutManager(activity)

        viewModel.bookingHistoryAdapter.onItemClickListner = this
        binding.rcyclrvwBookingHistory.adapter = viewModel.bookingHistoryAdapter

    }

    private fun setupObserver() {
        viewModel.bookingHistoryInfoLiveData.observe(viewLifecycleOwner, Observer {
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
                    if (!it.data.isNullOrEmpty()) {
                        binding.rcyclrvwBookingHistory.visibility = View.VISIBLE
                        binding.noData.visibility = View.GONE
                        viewModel.bookingHistoryAdapter.setBookings(it.data)
                    } else {
                        binding.rcyclrvwBookingHistory.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
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
                        viewModel.getBooking()
                    }
                }
            }
        })
    }

    private fun getData() {
        viewModel.getBooking()
    }

    override fun onItemClickListner(position: Int) {
        if (viewModel.arylstBookingHistory[position].status.equals(
                BOOKING_STATUS.INITIATED.name,
                true
            )
        ) {
            showCompleteDialog(position)
        }

    }

    private fun showCompleteDialog(position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Complete Ride")
        builder.setMessage("Are you sure you want to complete ride?")
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                viewModel.completeBooking(position)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->

            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }


}