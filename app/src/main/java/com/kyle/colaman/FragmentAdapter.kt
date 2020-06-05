package com.kyle.colaman

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 * Author   : kyle
 * Date     : 2020/5/13
 * Function : viewpager fragment adapter
 */
class FragmentAdapter(@NonNull fragmentManager: FragmentActivity) : FragmentStateAdapter(fragmentManager) {
     val arrayList: MutableList<IActionFragment> = mutableListOf()

    @NonNull
    fun getItem(position: Int): Fragment {
        return arrayList[position].findFragment()
    }

    fun addFragment(fragment: IActionFragment) {
        arrayList.add(fragment)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun createFragment(position: Int): Fragment {
        return arrayList[position].findFragment()
    }
}