package com.cleveroad.blur_tutorial

enum class HighlightType {

    /**
     * Highlight the view by blurring background.
     */
    BLUR,

    /**
     * Highlight the view by dimming background.
     */
    DIM;

    companion object {
        fun byValue(value: Int?) = values().firstOrNull { value == it() } ?: BLUR
    }

    operator fun invoke() = ordinal
}