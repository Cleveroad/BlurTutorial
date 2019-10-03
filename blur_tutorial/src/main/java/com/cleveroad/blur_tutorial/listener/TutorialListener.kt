package com.cleveroad.blur_tutorial.listener

import android.view.View
import com.cleveroad.blur_tutorial.state.tutorial.StateException
import com.cleveroad.blur_tutorial.state.tutorial.TutorialState

/**
 * Listener of tutorial process.
 */
interface TutorialListener {

    /**
     * Called before state is entered.
     * Use it to prepare UI for view highlighting.
     * E.g. smoothly scroll to this view.
     *
     * @param state [TutorialState] describes UI item, that will be highlighted
     */
    fun onStateEnter(state: TutorialState)

    /**
     * Called after state exit.
     *
     * @param state [TutorialState] describes UI item, that was highlighted
     */
    fun onStateExit(state: TutorialState)

    /**
     * Called on state error.
     *
     * @param state [TutorialState] describes UI item to highlight
     * @param error [StateException] describes error. E.g. view is not visible, etc.
     */
    fun onStateError(state: TutorialState, error: StateException)

    /**
     * Called, when popup view was inflated.
     * Use it to populate popup view layout.
     *
     * @param state [TutorialState] Describes UI element, that was highlighted
     * @param popupView [View] View of popup window
     */
    fun onPopupViewInflated(state: TutorialState, popupView: View)

    /**
     * Will be called, when tutorial process is finished.
     */
    fun onFinish()
}