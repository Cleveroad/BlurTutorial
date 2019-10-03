package com.cleveroad.blur_tutorial.sample.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cleveroad.blur_tutorial.sample.R
import com.cleveroad.blur_tutorial.sample.preferences.PreferencesProvider
import com.cleveroad.blur_tutorial.sample.ui.base.BaseLifecycleActivity
import com.cleveroad.blur_tutorial.sample.ui.home.HomeFragment
import com.cleveroad.blur_tutorial.sample.ui.plan.PlanFragment
import com.cleveroad.blur_tutorial.sample.ui.tracking.TrackingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseLifecycleActivity(),
        NavigationOwner{

    override val layoutId = R.layout.activity_main

    override val containerId = R.id.flContainer

    private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener listener@{
        if (it.itemId == bnvMain.selectedItemId) return@listener false

        when (it.itemId) {
            R.id.home -> showHome().let { true }
            R.id.tracking -> showTracking().let { true }
            R.id.plan -> showPlan().let { true }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bnvMain.setOnNavigationItemSelectedListener(navigationListener)
        replaceFragment(HomeFragment.newInstance())
    }

    override fun onDestroy() {
        resetTutorial()
        super.onDestroy()
    }

    override fun getNavigationBar(): BottomNavigationView = bnvMain

    // Just for sample.
    // Need so that when user open a sample app next time
    // to show the tutorial again..
    private fun resetTutorial() {
        PreferencesProvider.run {
            isHomeTutorialAlreadyShown = false
            isTrackingTutorialAlreadyShown = false
            isPlanTutorialAlreadyShown = false
        }
    }

    private fun showHome() {
        replace(HomeFragment.newInstance(), false)
    }

    private fun showTracking() {
        replace(TrackingFragment.newInstance(), bnvMain.selectedItemId == R.id.home)
    }

    private fun showPlan() {
        replace(PlanFragment.newInstance(), true)
    }

    private fun replace(fragment: Fragment, isToRightDirection: Boolean) {
        replaceFragment(fragment,
                inAnimRes = R.anim.slide_in_right.takeIf { isToRightDirection } ?: R.anim.slide_in_left,
                outAnimRes = R.anim.slide_out_left.takeIf { isToRightDirection } ?: R.anim.slide_out_right)
    }
}