package com.cleveroad.blur_tutorial.sample.ui.plan

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.cleveroad.blur_tutorial.BlurTutorial
import com.cleveroad.blur_tutorial.TutorialBuilder
import com.cleveroad.blur_tutorial.listener.SimpleTutorialListener
import com.cleveroad.blur_tutorial.sample.R
import com.cleveroad.blur_tutorial.sample.models.PlanModel
import com.cleveroad.blur_tutorial.sample.preferences.PreferencesProvider
import com.cleveroad.blur_tutorial.sample.ui.NavigationOwner
import com.cleveroad.blur_tutorial.sample.ui.base.BaseLifecycleFragment
import com.cleveroad.blur_tutorial.sample.utils.bindInterfaceOrThrow
import com.cleveroad.blur_tutorial.state.tutorial.MenuState
import com.cleveroad.blur_tutorial.state.tutorial.RecyclerItemState
import com.cleveroad.blur_tutorial.state.tutorial.TutorialState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_plan.*
import org.jetbrains.anko.textResource

class PlanFragment : BaseLifecycleFragment<PlanVM>() {

    companion object {

        private const val PLAN_FIRST_ITEM_ID = 0
        private const val PLAN_SECOND_ITEM_ID = 1
        private const val PLAN_MENU_ID = 2

        private const val PLAN_COLUMNS_NUMBER = 2

        private const val FIRST_ITEM_INDEX = 0
        private const val SECOND_ITEM_INDEX = 1

        private const val POPUP_RADIUS = 20F
        private const val BLUR_RADIUS = 25F

        fun newInstance() = PlanFragment()
    }

    override val viewModelClass = PlanVM::class.java

    override val layoutId = R.layout.fragment_plan

    private lateinit var bnvMain: BottomNavigationView

    private var adapter: PlansAdapter? = null

    private var tutorial: BlurTutorial? = null

    private val states by lazy {
        listOf(RecyclerItemState(PLAN_FIRST_ITEM_ID, rvPlan, FIRST_ITEM_INDEX),
                RecyclerItemState(PLAN_SECOND_ITEM_ID, rvPlan, SECOND_ITEM_INDEX),
                MenuState(PLAN_MENU_ID, bnvMain, R.id.plan)) }

    private val tutorialListener = object : SimpleTutorialListener() {

        override fun onPopupViewInflated(state: TutorialState, popupView: View) {
            popupView.run {
                val tvTitle = findViewById<TextView>(R.id.tvTitle)
                val tvDesc = findViewById<TextView>(R.id.tvDesc)
                val bGotIt = findViewById<Button>(R.id.bGotIt)

                bGotIt.setOnClickListener { tutorial?.next() }

                when (state.id) {
                    PLAN_FIRST_ITEM_ID, PLAN_SECOND_ITEM_ID -> {
                        tvTitle.textResource = R.string.plan_item_title
                        tvDesc.textResource = R.string.plan_item_desc
                    }
                    PLAN_MENU_ID -> {
                        tvTitle.textResource = R.string.plan_title
                        tvDesc.textResource = R.string.plan_desc
                    }
                }
            }
        }
    }

    private val plansObserver = Observer<List<PlanModel>> {
        adapter?.addAllNotify(it)
    }

    override fun observeLiveData() = viewModel.run {
        plansLD.observe(this@PlanFragment, plansObserver)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bnvMain = bindInterfaceOrThrow<NavigationOwner>(context).getNavigationBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUi()
        initTutorial()
        viewModel.getPlans()
    }

    // This method is called, when fragment enter animation is finished.
    // For more info see com.cleveroad.blur_tutorial.sample.ui.base.BaseLifecycleFragment realisation
    override fun onEnterAnimationEnd() {
        tutorial?.start()
    }

    private fun setupUi() {
        rvPlan.run {
            adapter = PlansAdapter(ctx).also { this@PlanFragment.adapter = it }
            layoutManager = GridLayoutManager(ctx, PLAN_COLUMNS_NUMBER)
        }
    }

    private fun initTutorial() {
        // First, check if the tutorial has been already shown to the user.
        // If not, then show tutorial.
        if (PreferencesProvider.isPlanTutorialAlreadyShown) return
        tutorial = TutorialBuilder()
                .withParent(clParent)
                .withListener(tutorialListener)
                .withOverlayColor(ContextCompat.getColor(ctx, R.color.purple_blue))
                .withPopupLayout(R.layout.popup_window)
                .withPopupAppearAnimation(R.anim.fade_in)
                .withPopupDisappearAnimation(R.anim.fade_out)
                .withPopupCornerRadius(POPUP_RADIUS)
                .withBlurRadius(BLUR_RADIUS)
                .build().apply { addAllStates(states) }
        PreferencesProvider.isPlanTutorialAlreadyShown = true
    }
}