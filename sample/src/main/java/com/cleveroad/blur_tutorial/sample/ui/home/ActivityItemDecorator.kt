package com.cleveroad.blur_tutorial.sample.ui.home

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.blur_tutorial.sample.R

class ActivityItemDecorator(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0, -context.resources.getDimensionPixelSize(R.dimen.activity_item_offset), 0, 0)
    }
}