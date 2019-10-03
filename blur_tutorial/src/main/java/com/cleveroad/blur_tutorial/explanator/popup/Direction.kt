package com.cleveroad.blur_tutorial.explanator.popup

internal enum class Direction {
    /**
     * There is a place below view to show popup.
     */
    TOP,

    /**
     * There is a place above view to show popup.
     */
    BOTTOM,

    /**
     * There is no place to show popup. (View occupies all screen).
     */
    NO_MATTER
}