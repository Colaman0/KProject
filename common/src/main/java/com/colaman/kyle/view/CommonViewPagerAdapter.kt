package com.colaman.kyle.view

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


/**
 * Author   : kyle
 * Date     : 2020/4/30
 * Function : 通用的viewpageradapter
 */
class CommonViewPagerAdapter(fm: FragmentManager,
                             behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                             val createFun: (Int) -> Fragment) : FragmentStatePagerAdapter(fm, behavior) {
    var registeredFragments = SparseArray<Fragment>()

    override fun getItem(position: Int): Fragment {
        return createFun(position)
    }

    override fun getCount() = registeredFragments.size()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container!!, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }

}