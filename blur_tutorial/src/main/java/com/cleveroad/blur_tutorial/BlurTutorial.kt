package com.cleveroad.blur_tutorial

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cleveroad.blur_tutorial.state.tutorial.TutorialState

interface BlurTutorial {

    /**
     * Add [states] to tutorial.
     * Will be highlighted by order in list.
     *
     * @param states [List] Describes UI items to explain.
     */
    fun addAllStates(states: List<TutorialState>)

    /**
     * Add [state] to tutorial.
     *
     * @param state [TutorialState] Describes UI item to explain.
     */
    fun addState(state: TutorialState)

    /**
     * Add [state] to tutorial by [index].
     *
     * @param state [TutorialState] Describes UI item to explain.
     * @param index [Int] index of state.
     */
    fun setState(state: TutorialState, index: Int)

    /**
     * Remove [state] from tutorial.
     *
     * @param state [TutorialState] state to remove.
     */
    fun removeState(state: TutorialState)

    /**
     * Remove all states from tutorial.
     * Cancel tutorial process.
     */
    fun clearStates()

    /**
     * @return [TutorialState] current state of tutorial.
     */
    fun getCurrentState(): TutorialState?

    /**
     * Start tutorial process.
     *
     * Should be called in [Activity.onResume] or [Fragment.onResume] or later.
     * If you also highlight action bar menu items,
     * call it in [Activity.onPrepareOptionsMenu] or [Fragment.onPrepareOptionsMenu] or later.
     */
    fun start()

    /**
     * Go to next state of tutorial.
     */
    fun next()

    /**
     * Interrupt tutorial process.
     * It can be renewed by calling [next] or [start].
     */
    fun interrupt()

    /**
     * Change configuration of current [BlurTutorial] instance.
     *
     * @return [TutorialBuilder] builder of [BlurTutorial]
     */
    fun configure(): TutorialBuilder

    /**
     * Call it in [onSaveInstanceState] of your fragment or activity.
     *
     * @param outState state to save
     */
    fun onSaveInstanceState(outState: Bundle)

    /**
     * Call it in [onRestoreInstanceState] of your fragment or activity.
     *
     * @param savedState [Bundle] state to restore
     */
    fun onRestoreInstanceState(savedState: Bundle?)

    /**
     * Release resources.
     * Call it in [Activity.onDestroy] ([Fragment.onDestroy]).
     */
    fun onDestroy()
}