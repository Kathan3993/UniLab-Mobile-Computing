package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.viewpager.widget.ViewPager
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.adapters.BorrowRequestsViewPagerAdapter
import com.example.mobilecomputingproject.databinding.FragmentBorrowRequestsBinding
import com.google.android.material.tabs.TabLayout

/**
 * Fragment for Borrow Requests
 */
class BorrowRequestsFragment : Fragment() {

    lateinit var pagerBorrowRequests: ViewPager
    lateinit var tabBorrowRequests: TabLayout
    lateinit var barBorrowRequests: Toolbar

    private var _binding: FragmentBorrowRequestsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBorrowRequestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        parentFragmentManager.commit {
            replace(R.id.fragmentBorrowRequests, ActiveBorrowRequestsFragment())
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagerBorrowRequests = binding.viewPagerBorrowRequests
        tabBorrowRequests = binding.tabsBorrowRequests
        barBorrowRequests = binding.toolbarBorrowRequests

        val fragmentList = ArrayList<Fragment>()
        val fragmentTitleList = ArrayList<String>()

        fragmentList.add(ActiveBorrowRequestsFragment())
        fragmentList.add(InactiveBorrowRequestsFragment())

        fragmentTitleList.add("Active Borrow Requests")
        fragmentTitleList.add("Inactive Borrow Requests")

        val adapter = BorrowRequestsViewPagerAdapter(parentFragmentManager,fragmentList, fragmentTitleList)

        pagerBorrowRequests.adapter = adapter

        tabBorrowRequests.setupWithViewPager(pagerBorrowRequests)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}