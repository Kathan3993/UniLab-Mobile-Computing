package com.example.mobilecomputingproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Adapter class for Borrow Requests tabs
 */
class BorrowRequestsViewPagerAdapter(
    supportFragmentManager: FragmentManager,
    fragmentList: ArrayList<Fragment>,
    fragmentTitleList: ArrayList<String>
) : FragmentStatePagerAdapter(supportFragmentManager) {

    // Get the fragment list
    private val fragmentList = fragmentList

    // Get the fragment title list
    private val fragmentTitleList = fragmentTitleList

    // Returns the size of the data
    override fun getCount(): Int {
        return fragmentList.size
    }

    // Get the fragment when user clicks on tab
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    // Get the fragment title when user clicks on tab
    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }

}