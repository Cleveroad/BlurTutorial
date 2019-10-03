package com.cleveroad.blur_tutorial.sample.ui.tracking

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.cleveroad.blur_tutorial.BlurTutorial
import com.cleveroad.blur_tutorial.TutorialBuilder
import com.cleveroad.blur_tutorial.listener.SimpleTutorialListener
import com.cleveroad.blur_tutorial.sample.R
import com.cleveroad.blur_tutorial.sample.preferences.PreferencesProvider
import com.cleveroad.blur_tutorial.sample.ui.NavigationOwner
import com.cleveroad.blur_tutorial.sample.ui.base.BaseLifecycleFragment
import com.cleveroad.blur_tutorial.sample.utils.bindInterfaceOrThrow
import com.cleveroad.blur_tutorial.state.tutorial.MenuState
import com.cleveroad.blur_tutorial.state.tutorial.TutorialState
import com.cleveroad.blur_tutorial.state.tutorial.ViewState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_tracking.*
import org.jetbrains.anko.textResource
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class TrackingFragment : BaseLifecycleFragment<TrackingVM>() {

    companion object {

        private const val TIMER_VIEW_ID = 0
        private const val DISTANCE_VIEW_ID = 1
        private const val CALORIES_VIEW_ID = 2
        private const val PACE_VIEW_ID = 3
        private const val TRACKING_MENU_ID = 4

        private const val POPUP_RADIUS = 20F
        private const val BLUR_RADIUS = 25F

        private const val TIME_PATTERN = "HH:mm:ss"
        private const val DISTANCE_DELIMITER = 1000.0

        fun newInstance() = TrackingFragment()
    }

    override val viewModelClass = TrackingVM::class.java

    override val layoutId = R.layout.fragment_tracking

    private lateinit var bnvMain: BottomNavigationView

    private var tutorial: BlurTutorial? = null

    private val states by lazy {
        listOf(ViewState(TIMER_VIEW_ID, tvTime),
                ViewState(DISTANCE_VIEW_ID, llDistance),
                ViewState(CALORIES_VIEW_ID, llCalories),
                ViewState(PACE_VIEW_ID, llPace),
                MenuState(TRACKING_MENU_ID, bnvMain, R.id.tracking))
    }

    private val tutorialListener = object : SimpleTutorialListener() {

        override fun onPopupViewInflated(state: TutorialState, popupView: View) {
            popupView.run {
                val tvTitle = findViewById<TextView>(R.id.tvTitle)
                val tvDesc = findViewById<TextView>(R.id.tvDesc)
                val bGotIt = findViewById<Button>(R.id.bGotIt)

                bGotIt.setOnClickListener { tutorial?.next() }

                when (state.id) {
                    TIMER_VIEW_ID -> {
                        tvTitle.textResource = R.string.timer_title
                        tvDesc.textResource = R.string.timer_desc
                    }
                    DISTANCE_VIEW_ID -> {
                        tvTitle.textResource = R.string.distance_title
                        tvDesc.textResource = R.string.distance_desc
                    }
                    CALORIES_VIEW_ID -> {
                        tvTitle.textResource = R.string.calories_title
                        tvDesc.textResource = R.string.calories_desc
                    }
                    PACE_VIEW_ID -> {
                        tvTitle.textResource = R.string.pace_title
                        tvDesc.textResource = R.string.pace_desc
                    }
                    TRACKING_MENU_ID -> {
                        tvTitle.textResource = R.string.tracking_title
                        tvDesc.textResource = R.string.tracking_desc
                        bGotIt.setOnClickListener {
                            // Finish tutorial process for current screen
                            // and open next screen
                            tutorial?.clearStates()
                            bnvMain.selectedItemId = R.id.plan
                        }
                    }
                }
            }
        }
    }

    private val timerObserver = Observer<Long> { tick ->
        tvTime.text = DateTime(tick, DateTimeZone.UTC).toString(TIME_PATTERN)
    }

    private val distanceObserver = Observer<Long> {
        tvDistanceValue.text = (it.toDouble() / DISTANCE_DELIMITER).toString()
    }

    private val caloriesObserver = Observer<Long> {
        tvCaloriesValue.text = it.toString()
    }

    private val paceObserver = Observer<Int> {
        tvPaceValue.text = it.toString()
    }

    override fun observeLiveData() = viewModel.run {
        timerLD.observe(this@TrackingFragment, timerObserver)
        distanceLD.observe(this@TrackingFragment, distanceObserver)
        caloriesLD.observe(this@TrackingFragment, caloriesObserver)
        paceLD.observe(this@TrackingFragment, paceObserver)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bnvMain = bindInterfaceOrThrow<NavigationOwner>(context).getNavigationBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.run {
            // These methods are needed just for
            // simulate tracking of human activity..
            startTimer()
            trackDistance()
            trackCalories()
            trackPace()
        }
        initTutorial()
    }

    // This method is called, when fragment enter animation is finished.
    // For more info see com.cleveroad.blur_tutorial.sample.ui.base.BaseLifecycleFragment realisation
    override fun onEnterAnimationEnd() {
        tutorial?.start()
    }

    private fun initTutorial() {
        // First, check if the tutorial has been already shown to the user.
        // If not, then show tutorial.
        if (PreferencesProvider.isTrackingTutorialAlreadyShown) return
        tutorial = TutorialBuilder()
                .withParent(llParent)
                .withListener(tutorialListener)
                .withPopupLayout(R.layout.popup_window)
                .withPopupAppearAnimation(R.anim.fade_in)
                .withPopupDisappearAnimation(R.anim.fade_out)
                .withPopupCornerRadius(POPUP_RADIUS)
                .withBlurRadius(BLUR_RADIUS)
                .build().apply { addAllStates(states) }
        PreferencesProvider.isTrackingTutorialAlreadyShown = true
    }
}