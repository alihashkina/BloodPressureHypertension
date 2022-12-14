package com.example.bloodpressurehypertension.MainTab

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.bloodpressure.fragment.GeneralPage
import com.example.bloodpressurehypertension.R
import com.example.bloodpressurehypertension.Statistics.View.Statistics

class PagerAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {

    val tabs: List<Pair<String, Fragment>> = listOf(
        context.resources.getString(R.string.record) to GeneralPage.newInstance(),
        context.resources.getString(R.string.statistics) to Statistics.newInstance()
    )

    override fun getItem(position: Int): Fragment {
        return tabs[position].second
    }

    override fun getCount(): Int {
        return tabs.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position].first
    }

}