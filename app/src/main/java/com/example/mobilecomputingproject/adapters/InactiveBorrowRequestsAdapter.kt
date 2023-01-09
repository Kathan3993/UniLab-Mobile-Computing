package com.example.mobilecomputingproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.BorrowRequest

/**
 * Adapter class for data binding of Inactive Borrow Requests
 */
class InactiveBorrowRequestsAdapter(
    private var inactiveBorrowRequestsList: List<BorrowRequest>,
    var context: Context
) :
    RecyclerView.Adapter<InactiveBorrowRequestsAdapter.InactiveBorrowRequestsViewHolder>() {

    /**
     * Provides reference to the type of view variables used.
     */
    class InactiveBorrowRequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewIABRLaptopTypeVal: TextView =
            itemView.findViewById(R.id.textViewIABRLaptopTypeVal)
        var textViewIABRConfigurationVal: TextView =
            itemView.findViewById(R.id.textViewIABRConfigurationVal)
        var textViewIABRLoanTypeVal: TextView = itemView.findViewById(R.id.textViewIABRLoanTypeVal)
        var textViewIABRLibraryVal: TextView = itemView.findViewById(R.id.textViewIABRLibraryVal)
        var textViewIABRStatusVal: TextView = itemView.findViewById(R.id.textViewIABRStatusVal)
        var textViewIABRDateVal: TextView = itemView.findViewById(R.id.textViewIABRDateVal)
    }

    // Creates a new view
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InactiveBorrowRequestsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.inactive_borrow_requests_card_design, parent, false)
        return InactiveBorrowRequestsViewHolder(view)
    }

    // Replaces the content of the view with data entered by the user
    override fun onBindViewHolder(holder: InactiveBorrowRequestsViewHolder, position: Int) {
        holder.textViewIABRLaptopTypeVal.text = inactiveBorrowRequestsList[position].laptopType
        holder.textViewIABRConfigurationVal.text =
            inactiveBorrowRequestsList[position].configuration
        holder.textViewIABRLoanTypeVal.text = inactiveBorrowRequestsList[position].loanType
        holder.textViewIABRLibraryVal.text = inactiveBorrowRequestsList[position].library
        holder.textViewIABRStatusVal.text = inactiveBorrowRequestsList[position].status
        holder.textViewIABRDateVal.text = inactiveBorrowRequestsList[position].dateOfRequest
    }

    // Returns the size of the data
    override fun getItemCount(): Int {
        return inactiveBorrowRequestsList.size
    }
}