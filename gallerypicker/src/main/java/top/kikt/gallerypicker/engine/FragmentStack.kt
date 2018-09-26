package top.kikt.gallerypicker.engine

import android.support.v4.app.Fragment

interface FragmentStack {

    fun pushFragment(fragment: Fragment)

    fun popFragment()

}