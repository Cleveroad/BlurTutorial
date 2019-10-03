package com.cleveroad.blur_tutorial

import android.os.Bundle
import android.util.Log
import android.view.View
import com.cleveroad.blur_tutorial.explanator.Explanator
import com.cleveroad.blur_tutorial.explanator.ExplanatorCallback
import com.cleveroad.blur_tutorial.explanator.ExplanatorImpl
import com.cleveroad.blur_tutorial.ext.applyIf
import com.cleveroad.blur_tutorial.listener.TutorialListener
import com.cleveroad.blur_tutorial.state.TutorialConfig
import com.cleveroad.blur_tutorial.state.tutorial.StateException
import com.cleveroad.blur_tutorial.state.tutorial.TutorialState
import com.cleveroad.blur_tutorial.utils.Common.Companion.getExtra
import com.cleveroad.blur_tutorial.utils.TAG
import java.lang.ref.WeakReference

internal class BlurTutorialImpl(var config: TutorialConfig,
                                listener: TutorialListener?) : BlurTutorial {

    companion object {
        private const val INITIAL_INDEX = -1
        private const val INDEX_NOT_CONTAINS = -1

        private val CURRENT_STATE_INDEX_EXTRA =
                getExtra("CURRENT_STATE_INDEX", BlurTutorialImpl::class.java)
    }

    private val errorCallback = object : ExplanatorCallback {

        override fun onPopupInflated(popupView: View) {
            currentStateIndex.takeIf { it in states.indices }?.let { currentIndex ->
                dispatchPopupInflatedEvent(states[currentIndex], popupView)
            }
        }

        override fun onError(errMsg: String) {
            currentStateIndex.takeIf { it in states.indices }?.let { currentIndex ->
                dispatchStateErrorEvent(states[currentIndex], StateException(errMsg))
            }
        }
    }

    private val explanator: Explanator = ExplanatorImpl(config, errorCallback)

    private val states = mutableListOf<TutorialState>()

    private var currentStateIndex = INITIAL_INDEX

    private var isInterrupted = false

    private var listenerRef = WeakReference(listener)

    var listener: TutorialListener?
        get() = listenerRef.get()
        set(value) {
            listenerRef = WeakReference(value)
        }

    override fun addAllStates(states: List<TutorialState>) {
        this.states.addAll(states)
    }

    override fun addState(state: TutorialState) {
        states.add(state)
    }

    override fun setState(state: TutorialState, index: Int) {
        checkStateIndex(index)
        states[index] = state
    }

    override fun removeState(state: TutorialState) {
        states.indexOf(state).takeIf {
            it != INDEX_NOT_CONTAINS && it <= currentStateIndex
        }?.let { --currentStateIndex }
        states.remove(state)
    }

    override fun clearStates() {
        states.clear()
        interrupt()
        currentStateIndex = INITIAL_INDEX
    }

    override fun getCurrentState(): TutorialState? =
            currentStateIndex.takeIf {
                it != INITIAL_INDEX && it < states.size
            }?.let { states[it] }

    override fun start() {
        isInterrupted = false
        when {
            checkStates() -> return                  // Can't start because states are empty.
            currentStateIndex != INITIAL_INDEX -> {  // If tutorial already started,
                currentStateIndex--                  // process current state again.
                startNextState()
            }
            else -> startNextState()                 // start tutorial
        }
    }

    override fun next() {
        isInterrupted = false
        finishPreviousState()
        startNextState()
    }

    override fun interrupt() {
        explanator.cancel()
        isInterrupted = true
    }

    override fun configure(): TutorialBuilder = TutorialBuilder().apply {
        config = this@BlurTutorialImpl.config
        blurTutorial = this@BlurTutorialImpl
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_STATE_INDEX_EXTRA, currentStateIndex)
    }

    override fun onRestoreInstanceState(savedState: Bundle?) {
        savedState?.run {
            currentStateIndex = getInt(CURRENT_STATE_INDEX_EXTRA, currentStateIndex)
        }
    }

    override fun onDestroy() {
        explanator.onDestroy()
    }

    private fun finishPreviousState() {
        if (currentStateIndex == INITIAL_INDEX || currentStateIndex >= states.size) return
        explanator.cancel()
        dispatchStateExitEvent(states[currentStateIndex])
    }

    private fun startNextState() {
        if (++currentStateIndex >= states.size) {
            dispatchFinishEvent()
            return
        }
        states[currentStateIndex].let { nextState ->
            dispatchStateEnterEvent(nextState)
            explainView(nextState)
        }
    }

    private fun explainView(state: TutorialState) {
        if (!isInterrupted) explanator.explain(state)
    }

    private fun checkStateIndex(index: Int) {
        if (index <= currentStateIndex) {
            Log.w(TAG, "Inserting state to this position will have no effect..")
        }
    }

    private fun checkStates() = states.isEmpty().applyIf ({ it }) {
        Log.w(TAG, "Can't start tutorial. Add states.")
    }

    private fun dispatchStateEnterEvent(state: TutorialState) {
        listenerRef.get()?.onStateEnter(state)
    }

    private fun dispatchStateExitEvent(state: TutorialState) {
        listenerRef.get()?.onStateExit(state)
    }

    private fun dispatchStateErrorEvent(state: TutorialState, error: StateException) {
        listenerRef.get()?.onStateError(state, error)
    }

    private fun dispatchPopupInflatedEvent(state: TutorialState, popupView: View) {
        listenerRef.get()?.onPopupViewInflated(state, popupView)
    }

    private fun dispatchFinishEvent() {
        listenerRef.get()?.onFinish()
    }
}