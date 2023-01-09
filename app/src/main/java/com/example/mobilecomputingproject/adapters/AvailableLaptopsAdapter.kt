package com.example.mobilecomputingproject.adapters

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.LaptopInformationItem
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Adapter class for data binding of Available Laptops
 */
class AvailableLaptopsAdapter(
    private var availableLaptopsList: List<LaptopInformationItem>,
    var context: Context
) :
    RecyclerView.Adapter<AvailableLaptopsAdapter.AvailableLaptopsViewHolder>() {

    var library: String = ""

    // Database instance creation
    val database = FirebaseFirestore.getInstance()

    /**
     * Provides reference to the type of view variables used.
     */
    class AvailableLaptopsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewBorrowLaptopTypeVal: TextView =
            itemView.findViewById(R.id.textViewBorrowLaptopTypeVal)
        var textViewBorrowConfigurationVal: TextView =
            itemView.findViewById(R.id.textViewBorrowConfigurationVal)
        var textViewBorrowLoanTypeVal: TextView =
            itemView.findViewById(R.id.textViewBorrowLoanTypeVal)
        var textViewBorrowLibraryVal: TextView =
            itemView.findViewById(R.id.textViewBorrowLibraryVal)
        var buttonBorrowAvailableLaptop: Button =
            itemView.findViewById(R.id.buttonBorrowAvailableLaptop)
    }

    // Creates a new view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableLaptopsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.laptops_borrow_card_design, parent, false)

        return AvailableLaptopsViewHolder(view)
    }

    // Replaces the content of the view with data entered by the user
    override fun onBindViewHolder(holder: AvailableLaptopsViewHolder, position: Int) {
        holder.textViewBorrowLaptopTypeVal.text = availableLaptopsList[position].laptop_type
        holder.textViewBorrowConfigurationVal.text =
            availableLaptopsList[position].laptop_model + ' ' + availableLaptopsList[position].ram + ' ' + availableLaptopsList[position].storage
        holder.textViewBorrowLoanTypeVal.text = availableLaptopsList[position].lending_type

        // Get the library name from database
        if (availableLaptopsList[position].library.isNotEmpty()) {
            holder.textViewBorrowLibraryVal.text = availableLaptopsList[position].library
            library = availableLaptopsList[position].library
        } else {
            database.collection("library").document(availableLaptopsList[position].library_id)
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

        // Detect click on borrow button
        holder.buttonBorrowAvailableLaptop.setOnClickListener() {

            // Send the data to borrow details fragment
            val bundle = bundleOf(
                "laptopId" to availableLaptopsList[position].laptop_id,
                "laptopType" to availableLaptopsList[position].laptop_type,
                "configuration" to availableLaptopsList[position].laptop_model + ' ' + availableLaptopsList[position].ram + ' ' + availableLaptopsList[position].storage,
                "loanType" to availableLaptopsList[position].lending_type,
                "library" to library
            )

            // Navigate to Borrow Details screen
            val activity = it.context as AppCompatActivity
            val navHostFragment =
                activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main_navigation) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(
                R.id.action_availableLaptopsFragment_to_borrowDetailsFragment,
                bundle
            )
        }

    }

    // Returns the size of the data
    override fun getItemCount(): Int {
        return availableLaptopsList.size
    }

}