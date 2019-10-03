package com.cleveroad.blur_tutorial.explanator.highlight.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import androidx.annotation.ColorInt
import com.cleveroad.blur_tutorial.explanator.highlight.BackgroundProcessor
import com.cleveroad.blur_tutorial.explanator.highlight.HighlightCallback
import com.cleveroad.blur_tutorial.ext.getBitmap
import com.cleveroad.blur_tutorial.ext.leftLocationInWindow
import com.cleveroad.blur_tutorial.ext.safeLet
import com.cleveroad.blur_tutorial.ext.topLocationInWindow
import com.cleveroad.blur_tutorial.utils.doOnBackground

/**
 * Processor, that highlight the view by blurring background.
 */
internal class BlurProcessor(ctx: Context,
                             private val blurRadius: Float,
                             @ColorInt private val overlayColor: Int) : BackgroundProcessor() {

    private val renderScript = RenderScript.create(ctx)

    private var blurTask: AsyncTask<Unit, Unit, BitmapDrawable>? = null

    override fun applyHighlight(view: View, except: View, callback: HighlightCallback) {
        safeLet(view.getBitmap(), except.getBitmap(Bitmap.Config.ALPHA_8)) { overlay, exceptBmp ->
            blurTask = doOnBackground({
                val exceptLeft = except.leftLocationInWindow.toFloat()
                val exceptTop = except.topLocationInWindow.toFloat()
                val overlayAlloc = Allocation.createFromBitmap(renderScript, overlay)
                ScriptIntrinsicBlur.create(renderScript, overlayAlloc.element).run {
                    setInput(overlayAlloc)
                    setRadius(blurRadius)
                    forEach(overlayAlloc)
                }
                overlayAlloc.copyTo(overlay)
                Canvas(overlay).run {
                    overlayColor.takeUnless { it == Color.TRANSPARENT }?.let { drawColor(it) }
                    drawBitmap(exceptBmp, exceptLeft, exceptTop, exceptPaint)
                }
                BitmapDrawable(view.resources, overlay)
            }, { drawable -> callback.onHighlight(drawable) })
        }
    }

    override fun applyHighlight(view: View, except: Path, callback: HighlightCallback) {
        view.getBitmap()?.let { overlay ->
            blurTask = doOnBackground({
                val overlayAlloc = Allocation.createFromBitmap(renderScript, overlay)
                ScriptIntrinsicBlur.create(renderScript, overlayAlloc.element).run {
                    setInput(overlayAlloc)
                    setRadius(blurRadius)
                    forEach(overlayAlloc)
                }
                overlayAlloc.copyTo(overlay)
                Canvas(overlay).run {
                    overlayColor.takeUnless { it == Color.TRANSPARENT }?.let { drawColor(it) }
                    drawPath(except, exceptPaint)
                }
                BitmapDrawable(view.resources, overlay)
            }, { drawable -> callback.onHighlight(drawable) })
        }
    }

    override fun release() {
        blurTask?.cancel(true)
        renderScript.destroy()
    }
}