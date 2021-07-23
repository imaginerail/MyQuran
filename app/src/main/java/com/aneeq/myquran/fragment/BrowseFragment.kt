package com.aneeq.myquran.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.aneeq.myquran.R
import com.aneeq.myquran.adapter.MyPagerAdapter
import com.google.android.material.tabs.TabLayout


class BrowseFragment : Fragment() {

    lateinit var viewpager: ViewPager
    lateinit var tablayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_browse, container, false)

        viewpager = view.findViewById(R.id.viewpager)
        tablayout = view.findViewById(R.id.tablayout)
        setUpAdapter()
        setUpTabLayout()
        return view
    }

    private fun setUpAdapter() {
        val fragAdapter = MyPagerAdapter(childFragmentManager)
        viewpager.adapter = fragAdapter
    }

    private fun setUpTabLayout() {
        tablayout.setupWithViewPager(viewpager)
    }
}
