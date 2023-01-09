package com.example.mobilecomputingproject.adapters

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.LaptopInformationItem
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Adapter class for data binding of Available Laptops
 */
class LaptopsAdapter(private var laptopsList: List<LaptopInformationItem>, var context: Context) :
    RecyclerView.Adapter<LaptopsAdapter.LaptopsViewHolder>() {

    var library: String = ""

    // Database instance creation
    val database = FirebaseFirestore.getInstance()

    /**
     * Provides reference to the type of view variables used.
     */
    class LaptopsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewBorrowLaptopTypeVal: TextView =
            itemView.findViewById(R.id.textViewBorrowLaptopTypeVal)
        var textViewBorrowConfigurationVal: TextView =
            itemView.findViewById(R.id.textViewBorrowConfigurationVal)
        var textViewBorrowLoanTypeVal: TextView =
            itemView.findViewById(R.id.textViewBorrowLoanTypeVal)
        var textViewBorrowLibraryVal: TextView =
            itemView.findViewById(R.id.textViewBorrowLibraryVal)
        var buttonAvailable: Button = itemView.findViewById(R.id.buttonAvailable)
        var buttonUnavailable: Button = itemView.findViewById(R.id.buttonUnavailable)
    }

    // Creates a new view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaptopsViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.laptops_card_design, parent, false)

        return LaptopsViewHolder(view)
    }

    // Replaces the content of the view with data entered by the user
    override fun onBindViewHolder(holder: LaptopsViewHolder, position: Int) {
        holder.textViewBorrowLaptopTypeVal.text = laptopsList[position].laptop_type
        holder.textViewBorrowConfigurationVal.text =
            laptopsList[position].laptop_model + ' ' + laptopsList[position].ram + ' ' + laptopsList[position].storage
        holder.textViewBorrowLoanTypeVal.text = laptopsList[position].lending_type

        // Get the library name from database
        if (laptopsList[position].library.isNotEmpty()) {
            holder.textViewBorrowLibraryVal.text = laptopsList[position].library
            library = laptopsList[position].library
        } else {
            database.collection("library").document(laptopsList[position].library_id)
                .get()
                .addOnSuccessListener { result ->
                    Log.d(ContentValues.TAG, result.data!!.getValue("name").toString())

                    holder.textViewBorrowLibraryVal.text = result.data!!.getValue("name").toString()
                    library = result.data!!.getValue("name").toString()

                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }

        // Change the status of laptops
        if (laptopsList[position].is_booked) {
            holder.buttonAvailable.setVisibility(View.GONE)
        } else {
            holder.buttonUnavailable.setVisibility(View.GONE)
        }

    }

    // Returns the size of the data
    override fun getItemCount(): Int {
        return laptopsList.size
    }

}