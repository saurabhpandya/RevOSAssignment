package com.revosassignment.ui.booking.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ravos.data.booking.BOOKING_STATUS
import com.ravos.data.model.BookingInfoModel
import com.revosassignment.R
import com.revosassignment.databinding.RawBookingBinding
import com.revosassignment.utility.OnItemClickListner

class BookingHistoryAdapter(var arylstBookings: ArrayList<BookingInfoModel>) :
    RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder>() {


    lateinit var onItemClickListner: OnItemClickListner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawBookingBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_booking,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstBookings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstBookings.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setBookings(arylstBookings: ArrayList<BookingInfoModel>) {
        this.arylstBookings = arylstBookings
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(bookingInfoModel: BookingInfoModel) {
            binding.txtvwBookingId.text = "Booking Id: ${bookingInfoModel.bookingId.toString()}"
            binding.txtvwBookingStatus.text = bookingInfoModel.status
            binding.txtvwBookingUsername.text = "Name: ${bookingInfoModel.userName}"
            binding.txtvwVehicleId.text = "Vehicle Id: ${bookingInfoModel.vehicleId}"
            binding.txtvwZoneId.text = "Zone Id: ${bookingInfoModel.zoneId}"

            if (bookingInfoModel.status.equals(BOOKING_STATUS.INITIATED.name, true)) {
                binding.txtvwBookingTime.text = bookingInfoModel.ride_start_time
            } else {
                binding.txtvwBookingTime.text = bookingInfoModel.ride_end_time
            }
        }
    }

}
