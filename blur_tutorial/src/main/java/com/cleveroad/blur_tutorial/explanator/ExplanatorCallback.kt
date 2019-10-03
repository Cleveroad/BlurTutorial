package com.cleveroad.blur_tutorial.explanator

import android.view.View

internal interface ExplanatorCallback {

    fun onPopupInflated(popupView: View)

    fun onError(errMsg: String)
}