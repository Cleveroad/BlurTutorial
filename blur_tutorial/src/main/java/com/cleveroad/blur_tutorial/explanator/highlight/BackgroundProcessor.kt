package com.cleveroad.blur_tutorial.explanator.highlight

import android.graphics.*
import android.view.View

internal abstract class BackgroundProcessor {

    protected val exceptPaint = Paint().apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    }

    /**
     * Apply highlight for view.
     *
     * @param view [View] Background view.
     * @param except [View] View to highlight.
     * @param callback [HighlightCallback] Will be called, when background will be generated.
     */
    abstract fun applyHighlight(view: View,
                                except: View,
                                callback: HighlightCallback)

    /**
     * Apply highlight for custom path.
     *
     * @param view [View] Background view.
     * @param except [Path] to highlight.
     * @param callback [HighlightCallback] Will be called, when background will be generated.
     */
    abstract fun applyHighlight(view: View,
                                except: Path,
                                callback: HighlightCallback)

    /**
     * Release resources.
     */
    abstract fun release()
}