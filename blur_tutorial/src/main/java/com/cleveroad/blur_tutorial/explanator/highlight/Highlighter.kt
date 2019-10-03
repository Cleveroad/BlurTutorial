package com.cleveroad.blur_tutorial.explanator.highlight

import android.graphics.Path
import android.view.View
import androidx.annotation.ColorInt
import com.cleveroad.blur_tutorial.HighlightType
import com.cleveroad.blur_tutorial.explanator.highlight.blur.BlurProcessor
import com.cleveroad.blur_tutorial.explanator.highlight.dim.DimProcessor
import com.cleveroad.blur_tutorial.ext.safeLet
import java.lang.ref.WeakReference

internal class Highlighter(callback: HighlightCallback) {

    private val callbackRef = WeakReference(callback)

    private var processor: BackgroundProcessor? = null

    fun highlight(view: View?,
                  parent: View?,
                  type: HighlightType,
                  @ColorInt overlay: Int,
                  radius: Float) {
        when (type) {
            HighlightType.BLUR -> applyBlurExceptView(view, parent, radius, overlay)
            HighlightType.DIM -> applyDimExceptView(view, parent, overlay)
        }
    }

    fun highlight(path: Path,
                  parent: View?,
                  type: HighlightType,
                  @ColorInt overlay: Int,
                  radius: Float) {
        when (type) {
            HighlightType.BLUR -> applyBlurExceptPath(path, parent, radius, overlay)
            HighlightType.DIM -> applyDimExceptPath(path, parent, overlay)
        }
    }

    fun isHighlighted() = processor != null

    fun release() {
        processor?.release()
        processor = null
    }

    private fun applyBlurExceptView(except: View?, parent: View?, radius: Float, @ColorInt overlay: Int) {
        safeLet (parent, except) { parentView, exceptView ->
            parentView.post {
                highlightView(exceptView, parentView, BlurProcessor(exceptView.context, radius, overlay))
            }
        }
    }

    private fun applyBlurExceptPath(except: Path?, parent: View?, radius: Float, @ColorInt overlay: Int) {
        safeLet (parent, except) { parentView, exceptRect ->
            parentView.post {
                highlightPath(exceptRect, parentView, BlurProcessor(parentView.context, radius, overlay))
            }
        }
    }

    private fun applyDimExceptView(except: View?, parent: View?, @ColorInt overlay: Int) {
        safeLet (parent, except) { parentView, exceptView ->
            parentView.post {
                highlightView(exceptView, parentView, DimProcessor(overlay))
            }
        }
    }

    private fun applyDimExceptPath(except: Path?, parent: View?, @ColorInt overlay: Int) {
        safeLet (parent, except) { parentView, exceptRect ->
            parentView.post {
                highlightPath(exceptRect, parentView, DimProcessor(overlay))
            }
        }
    }

    private fun highlightView(view: View, background: View, processor: BackgroundProcessor) {
        this.processor = processor

        callbackRef.get()?.let { callback ->
            processor.applyHighlight(background, view, callback)
        }
    }

    private fun highlightPath(path: Path, background: View, processor: BackgroundProcessor) {
        this.processor = processor

        callbackRef.get()?.let { callback ->
            processor.applyHighlight(background, path, callback)
        }
    }
}