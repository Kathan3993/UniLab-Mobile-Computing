package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.viewpager.widget.ViewPager
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.adapters.DevicesViewPagerAdapter
import com.example.mobilecomputingproject.databinding.FragmentDevicesBinding
import com.google.android.material.tabs.TabLayout

/**
 * Fragment for Devices main fragment
 */
class DevicesFragment : Fragment() {

    // Declaring the variables for tabs
    lateinit var barDevices: Toolbar
    lateinit var pagerDevices: ViewPager
    lateinit var tabDevices: TabLayout

    // Binding for view binding
    private var _binding: FragmentDevicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDevicesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set the Current Devices Fragment as home fragment
        parentFragmentManager.commit {
            replace(R.id.fragmentDevices, CurrentDevicesFragment())
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "On view created: $savedInstanceState")

        pagerDevices = binding.viewPagerDevices
        tabDevices = binding.tabsDevices
        barDevices = binding.toolbarDevices

        val fragmentList = ArrayList<Fragment>()
        val fragmentTitleList = ArrayList<String>()

        // Add the fragments
        fragmentList.add(CurrentDevicesFragment())
        fragmentList.add(PastDevicesFragment())

        // Add the fragments title
        fragmentTitleList.add("Current Devices")
        fragmentTitleList.add("Past Devices")

        // Set the adapter for tabs
        val adapter = DevicesViewPagerAdapter(parentFragmentManager,fragmentList, fragmentTitleList)

        pagerDevices.adapter = adapter

        tabDevices.setupWithViewPager(pagerDevices)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}