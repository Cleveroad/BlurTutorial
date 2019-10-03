package com.cleveroad.blur_tutorial.explanator

import com.cleveroad.blur_tutorial.state.tutorial.TutorialState

internal interface Explanator {

    /**
     * Highlight the UI element, described by [state].
     * Also, show the popup with some explanation.
     *
     * @param state [TutorialState] describes UI element to highlight
     */
    fun explain(state: TutorialState)

    /**
     * Cancel view highlighting.
     * Dismiss the explanation popup.
     */
    fun cancel()

    /**
     * Release resources.
     */
    fun onDestroy()
}