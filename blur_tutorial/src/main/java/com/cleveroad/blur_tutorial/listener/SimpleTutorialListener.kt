package com.cleveroad.blur_tutorial.listener

import android.view.View
import com.cleveroad.blur_tutorial.state.tutorial.StateException
import com.cleveroad.blur_tutorial.state.tutorial.TutorialState

open class SimpleTutorialListener : TutorialListener {

    override fun onStateEnter(state: TutorialState) = Unit

    override fun onStateExit(state: TutorialState) = Unit

    override fun onStateError(state: TutorialState, error: StateException) = Unit

    override fun onPopupViewInflated(state: TutorialState, popupView: View) = Unit

    override fun onFinish() = Unit
}