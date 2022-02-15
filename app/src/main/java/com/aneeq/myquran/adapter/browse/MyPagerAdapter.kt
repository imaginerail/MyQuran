package com.aneeq.myquran.adapter.browse

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aneeq.myquran.fragment.JuzFragment
import com.aneeq.myquran.fragment.SurahsFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> JuzFragment()
            else -> {
                SurahsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "JUZ"
            else -> {
                "SURAHS"
            }
        }
    }


}