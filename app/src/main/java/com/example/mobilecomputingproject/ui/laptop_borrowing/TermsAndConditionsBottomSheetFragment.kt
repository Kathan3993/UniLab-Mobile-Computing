package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Fragment for Terms and Conditions bottom sheet
 */
class TermsAndConditionsBottomSheetFragment : BottomSheetDialogFragment() {

    // variable for storing the bundle arguments
    private var laptopId = ""
    private var laptopType = ""
    private var configuration = ""
    private var loanType = ""
    private var library = ""
    private var firstName = ""
    private var lastName = ""
    private var street = ""
    private var city = ""
    private var state = ""
    private var postal = ""
    private var phone = ""
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false)

        // Get the bundle arguments
        laptopId = arguments?.getString("laptopId")!!
        laptopType = arguments?.getString("laptopType")!!
        configuration = arguments?.getString("configuration")!!
        loanType = arguments?.getString("loanType")!!
        library = arguments?.getString("library")!!
        firstName = arguments?.getString("firstName")!!
        lastName = arguments?.getString("lastName")!!
        street = arguments?.getString("street")!!
        city = arguments?.getString("city")!!
        state = arguments?.getString("state")!!
        postal = arguments?.getString("postal")!!
        phone = arguments?.getString("phone")!!
        email = arguments?.getString("email")!!

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Detect the click on Agree button
        val buttonAgree = view.findViewById<Button>(R.id.buttonAgree)
        buttonAgree.setOnClickListener(View.OnClickListener {

            val bundle = bundleOf(
                "laptopId" to laptopId,
                "laptopType" to laptopType,
                "configuration" to configuration,
                "loanType" to loanType,
                "library" to library,
                "firstName" to firstName,
                "lastName" to lastName,
                "street" to street,
                "city" to city,
                "state" to state,
                "postal" to postal,
                "phone" to phone,
                "email" to email
            )

            findNavController().navigate(
                R.id.action_borrowDetailsFragment_to_borrowPreviewFragment,
                bundle
            )
            dismiss()

        })

        // Detect click on Cancel button
        val buttonCancelTerms = view.findViewById<Button>(R.id.buttonCancelTerms)
        buttonCancelTerms.setOnClickListener(View.OnClickListener {
            dismiss()
        })
    }
}