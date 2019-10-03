package com.cleveroad.blur_tutorial.state.tutorial

/**
 * State of tutorial.
 * Describes UI item to explain.
 */
interface TutorialState {

    /**
     * ID of this state.
     */
    val id: Int

    /**
     * Layout of popup for this state.
     *
     * If not specified (null),
     * then will be used default layout of popup.
     */
    val popupLayout: Int?
}