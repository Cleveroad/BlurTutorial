package com.cleveroad.blur_tutorial.sample.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.blur_tutorial.BlurTutorial
import com.cleveroad.blur_tutorial.TutorialBuilder
import com.cleveroad.blur_tutorial.listener.SimpleTutorialListener
import com.cleveroad.blur_tutorial.sample.R
import com.cleveroad.blur_tutorial.sample.models.ActivityModel
import com.cleveroad.blur_tutorial.sample.preferences.PreferencesProvider
import com.cleveroad.blur_tutorial.sample.ui.NavigationOwner
import com.cleveroad.blur_tutorial.sample.ui.base.BaseLifecycleFragment
import com.cleveroad.blur_tutorial.sample.utils.bindInterfaceOrThrow
import com.cleveroad.blur_tutorial.state.tutorial.MenuState
import com.cleveroad.blur_tutorial.state.tutorial.TutorialState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.textResource

class HomeFragment : BaseLifecycleFragment<HomeVM>() {

    companion object {

        private const val HOME_MENU_ID = 0
        private const val SYNC_MENU_ID = 1

        private const val POPUP_RADIUS = 20F
        private const val BLUR_RADIUS = 25F


        fun newInstance() = HomeFragment()
    }

    override val viewModelClass = HomeVM::class.java

    override val layoutId = R.layout.fragment_home

    private val activitiesAdapter by lazy { ActivitiesAdapter(ctx) }

    private val activitiesObserver = Observer<List<ActivityModel>> { activities ->
        activitiesAdapter.addAllNotify(activities)

        // scroll to top item (RecyclerView is reversed)
        rvActivity.scrollToPosition(activities.lastIndex)
    }

    private val states by lazy {
        listOf(MenuState(SYNC_MENU_ID, tbHome, R.id.sync),
                MenuState(HOME_MENU_ID, bnvMain, R.id.home))
    }

    private val tutorialListener = object : SimpleTutorialListener() {

        override fun onPopupViewInflated(state: TutorialState, popupView: View) {
            popupView.run {
                val tvTitle = findViewById<TextView>(R.id.tvTitle)
                val tvDesc = findViewById<TextView>(R.id.tvDesc)
                val bGotIt = findViewById<Button>(R.id.bGotIt)

                bGotIt.setOnClickListener { tutorial?.next() }

                when (state.id) {
                    SYNC_MENU_ID -> {
                        tvTitle.textResource = R.string.sync_title
                        tvDesc.textResource = R.string.sync_desc
                    }
                    HOME_MENU_ID -> {
                        tvTitle.textResource = R.string.home_title
                        tvDesc.textResource = R.string.home_desc
                        bGotIt.setOnClickListener {
                            // Finish tutorial process for current screen
                            // and open next screen (tracking)
                            tutorial?.clearStates()
                            bnvMain.selectedItemId = R.id.tracking
                        }
                    }
                }
            }
        }
    }

    private lateinit var bnvMain: BottomNavigationView

    private var tutorial: BlurTutorial? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bnvMain = bindInterfaceOrThrow<NavigationOwner>(context).getNavigationBar()
    }

    override fun observeLiveData() {
        viewModel.activitiesLD.observe(this@HomeFragment, activitiesObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        viewModel.getActivities()   // fill RecyclerView
        initTutorial()
    }

    override fun onResume() {
        super.onResume()
        tutorial?.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tutorial?.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        tutorial?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        tutorial?.onDestroy()
        super.onDestroy()
    }

    private fun setupUi() {
        with(rvActivity) {
            adapter = activitiesAdapter
            addItemDecoration(ActivityItemDecorator(ctx))
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
                reverseLayout = true   // need to set it to create round effect for RecyclerView items.
            }
        }
    }

    private fun initTutorial() {
        // First, check if the tutorial has been already shown to the user.
        // If not, then show tutorial.
        if (PreferencesProvider.isHomeTutorialAlreadyShown) return
        tutorial = TutorialBuilder()
                .withParent(clParent)
                .withListener(tutorialListener)
                .withPopupLayout(R.layout.popup_window)
                .withPopupAppearAnimation(R.anim.fade_in)
                .withPopupDisappearAnimation(R.anim.fade_out)
                .withPopupCornerRadius(POPUP_RADIUS)
                .withBlurRadius(BLUR_RADIUS)
                .build().apply { addAllStates(states) }
        PreferencesProvider.isHomeTutorialAlreadyShown = true
    }
}