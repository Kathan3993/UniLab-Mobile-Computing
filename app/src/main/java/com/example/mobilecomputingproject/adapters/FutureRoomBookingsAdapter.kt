package com.example.mobilecomputingproject.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.RoomBooking
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


class FutureRoomBookingsAdapter(
    private var futureRoomBookingList: List<RoomBooking>,
    var context: Context,
    var fragmentManager: FragmentManager?
) : RecyclerView.Adapter<FutureRoomBookingsAdapter.FutureRoomBookingViewHolder>() {
    class FutureRoomBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewPDName: TextView = itemView.findViewById(R.id.textViewPDName)
        var textViewPDLibrary: TextView = itemView.findViewById(R.id.textViewPDLibrary)
        var textViewPDRoomNum: TextView = itemView.findViewById(R.id.textViewPDRoomNum)
        var textViewPDDate: TextView = itemView.findViewById(R.id.textViewPDDate)
        var textViewPDPeopleNum: TextView = itemView.findViewById(R.id.textViewPDPeopleNum)
        var textViewPDBookingSlot: TextView = itemView.findViewById(R.id.textViewPDBookingSlot)
        var buttonCancelBooking: Button = itemView.findViewById(R.id.buttonCancelBooking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureRoomBookingViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.future_room_booking_card_design, parent, false)

        return FutureRoomBookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: FutureRoomBookingViewHolder, position: Int) {
        holder.textViewPDName.text = futureRoomBookingList[position].userId
        holder.textViewPDLibrary.text = futureRoomBookingList[position].room
        holder.textViewPDRoomNum.text = futureRoomBookingList[position].room
        holder.textViewPDDate.text = futureRoomBookingList[position].date
        holder.textViewPDPeopleNum.text = futureRoomBookingList[position].participants
        holder.textViewPDBookingSlot.text = futureRoomBookingList[position].slot
        var database = FirebaseFirestore.getInstance()
        var email = futureRoomBookingList[position].email
        var list = futureRoomBookingList[position].listOfParticipants

        holder.buttonCancelBooking.setOnClickListener {
            val cancelDialog = Dialog(context, R.style.dialog_popup)
            cancelDialog.setContentView(R.layout.room_booking_cancellation_dialog)
            cancelDialog.show()
            val yesButton: Button = cancelDialog.findViewById(R.id.buttonYes)
            val noButton: Button = cancelDialog.findViewById(R.id.buttonNo)
            yesButton.setOnClickListener {
                database.collection("RoomBooking")
                    .whereEqualTo("date", futureRoomBookingList[position].date)
                    .whereEqualTo("slot", futureRoomBookingList[position].slot)
                    .whereEqualTo("userId", futureRoomBookingList[position].email)
                    .get().addOnSuccessListener { result ->
                        println("result ${result.documents}")
                        for (document in result) {
                            database.collection("RoomBooking").document(document.id)
                                .update("status", "Cancelled").addOnSuccessListener {
                                    cancelDialog.dismiss()
                                    Toast.makeText(context,"Booking cancelled", Toast.LENGTH_SHORT).show()

                                }
                        }
                    }
            }
            noButton.setOnClickListener {
                cancelDialog.hide()
            }
        }
    }

    override fun getItemCount(): Int {
        return futureRoomBookingList.size
    }

}