package com.cleveroad.blur_tutorial.state.tutorial

import android.graphics.Path
import android.view.View

/**
 * Realisation of [TutorialState] for [Path] to highlight.
 *
 * Use it to highlight a part of [parent] [View].
 * If [parent] is null, then will be highlighted [Path] with absolute coordinates.
 */
data class PathState(override val id: Int,
                     val path: Path,
                     override val popupLayout: Int? = null,
                     val parent: View? = null) : TutorialState