package com.example.mobilecomputingproject.adapters

import android.app.Dialog
import android.content.ContentValues.TAG
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.BorrowRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * Adapter class for data binding of Active Borrow Requests
 */
class ActiveBorrowRequestsAdapter(
    private var activeBorrowRequestsList: List<BorrowRequest>,
    var context: Context,
    var user_id: String?
) :
    RecyclerView.Adapter<ActiveBorrowRequestsAdapter.ActiveBorrowRequestsViewHolder>() {

    // Database instance creation
    val database = FirebaseFirestore.getInstance()

    /**
     * Provides reference to the type of view variables used.
     */
    class ActiveBorrowRequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewABRLaptopTypeVal: TextView =
            itemView.findViewById(R.id.textViewABRLaptopTypeVal)
        var textViewABRConfigurationVal: TextView =
            itemView.findViewById(R.id.textViewABRConfigurationVal)
        var textViewABRLoanTypeVal: TextView = itemView.findViewById(R.id.textViewABRLoanTypeVal)
        var textViewABRLibraryVal: TextView = itemView.findViewById(R.id.textViewABRLibraryVal)
        var textViewABRStatusVal: TextView = itemView.findViewById(R.id.textViewABRStatusVal)
        var textViewABRDateVal: TextView = itemView.findViewById(R.id.textViewABRDateVal)
        var buttonCancelBorrowRequest: Button =
            itemView.findViewById(R.id.buttonCancelBorrowRequest)
    }

    // Creates a new view
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActiveBorrowRequestsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.active_borrow_requests_card_design, parent, false)
        return ActiveBorrowRequestsViewHolder(view)
    }

    // Replaces the content of the view with data entered by the user
    override fun onBindViewHolder(holder: ActiveBorrowRequestsViewHolder, position: Int) {
        holder.textViewABRLaptopTypeVal.text = activeBorrowRequestsList[position].laptopType
        holder.textViewABRConfigurationVal.text = activeBorrowRequestsList[position].configuration
        holder.textViewABRLoanTypeVal.text = activeBorrowRequestsList[position].loanType
        holder.textViewABRLibraryVal.text = activeBorrowRequestsList[position].library
        holder.textViewABRStatusVal.text = activeBorrowRequestsList[position].status
        holder.textViewABRDateVal.text = activeBorrowRequestsList[position].dateOfRequest

        // Detect click on Cancel button
        holder.buttonCancelBorrowRequest.setOnClickListener() {

            // Create borrow request dialog instance
            val borrowRequestDialog = Dialog(context, R.style.dialog_popup)
            borrowRequestDialog.setContentView(R.layout.borrow_request_popup)

            // Detect the click on Yes button
            val buttonYes: Button = borrowRequestDialog.findViewById(R.id.buttonYes)
            buttonYes.setOnClickListener {

                // Get the active borrow requests from database
                database.collection("laptop_booking")
                    .whereEqualTo("user_id", "$user_id")
                    .whereEqualTo("status", "active")
                    .get().addOnSuccessListener { result ->
                        for (document in result) {

                            var bookingId: String = document.id
                            var bookingData = hashMapOf("status" to "Cancelled")
                            var laptopId: String = document.data.getValue("laptop_id").toString()

                            // Update the status of booking request
                            database.collection("laptop_booking").document("$bookingId")
                                .set(bookingData, SetOptions.merge())

                            // Get the users current device
                            database.collection("user_devices")
                                .whereEqualTo("user_id", "$user_id")
                                .whereEqualTo("status", "active")
                                .get().addOnSuccessListener { result ->

                                    for (document in result) {

                                        var userDevicesData = hashMapOf("status" to "inactive")
                                        Log.d(TAG, "Current Devices document id: ${document.id}")

                                        // Update the status of user device
                                        database.collection("user_devices")
                                            .document("${document.id}")
                                            .set(userDevicesData, SetOptions.merge())

                                        var laptopInfoData = hashMapOf("is_booked" to false)

                                        // Update the booking status in laptop information collection
                                        database.collection("laptop_information").document(laptopId)
                                            .set(laptopInfoData, SetOptions.merge())

                                    }

                                }.addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }

                            borrowRequestDialog.dismiss()

                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }
            }

            // Detect the click on No button
            val buttonNo: Button = borrowRequestDialog.findViewById(R.id.buttonNo)
            buttonNo.setOnClickListener {
                borrowRequestDialog.dismiss()
            }

            // Show the borrow request dialog
            borrowRequestDialog.show()
        }
    }

    // Returns the size of the data
    override fun getItemCount(): Int {
        return activeBorrowRequestsList.size
    }

}