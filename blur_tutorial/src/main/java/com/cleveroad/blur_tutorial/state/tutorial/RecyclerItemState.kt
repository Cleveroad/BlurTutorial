package com.cleveroad.blur_tutorial.state.tutorial

import androidx.recyclerview.widget.RecyclerView

/**
 * Realisation of [TutorialState] for [RecyclerView] items.
 */
data class RecyclerItemState(override val id: Int,
                             val recyclerView: RecyclerView,
                             val itemPosition: Int,
                             override val popupLayout: Int? = null): TutorialState