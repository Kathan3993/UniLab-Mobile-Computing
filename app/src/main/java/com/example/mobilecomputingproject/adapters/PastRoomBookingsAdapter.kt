package com.example.mobilecomputingproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.RoomBooking

class PastRoomBookingsAdapter(
    private var pastRoomBookingList: List<RoomBooking>,
    var context: Context,
    var fragmentManager: FragmentManager?
) : RecyclerView.Adapter<PastRoomBookingsAdapter.PastRoomBookingViewHolder>() {
    class PastRoomBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewPDName: TextView = itemView.findViewById(R.id.textViewPDName)
        var textViewPDLibrary: TextView = itemView.findViewById(R.id.textViewPDLibrary)
        var textViewPDRoomNum: TextView = itemView.findViewById(R.id.textViewPDRoomNum)
        var textViewPDDate: TextView = itemView.findViewById(R.id.textViewPDDate)
        var textViewPDPeopleNum: TextView = itemView.findViewById(R.id.textViewPDPeopleNum)
        var textViewPDBookingSlot: TextView = itemView.findViewById(R.id.textViewPDBookingSlot)
        var textViewPDStatus: TextView = itemView.findViewById(R.id.textViewPDStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastRoomBookingViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.past_room_booking_card_design, parent, false)

        return PastRoomBookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PastRoomBookingViewHolder, position: Int) {
        holder.textViewPDName.text = pastRoomBookingList[position].userId
        holder.textViewPDLibrary.text = pastRoomBookingList[position].library
        holder.textViewPDRoomNum.text = pastRoomBookingList[position].room
        holder.textViewPDDate.text = pastRoomBookingList[position].date
        holder.textViewPDPeopleNum.text = pastRoomBookingList[position].participants
        holder.textViewPDBookingSlot.text = pastRoomBookingList[position].slot
        holder.textViewPDStatus.text = pastRoomBookingList[position].status
    }

    override fun getItemCount(): Int {
        return pastRoomBookingList.size
    }
}