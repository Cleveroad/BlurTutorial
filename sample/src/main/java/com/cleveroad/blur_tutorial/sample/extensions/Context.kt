package com.cleveroad.blur_tutorial.sample.extensions

import android.app.Activity
import android.content.Context

fun Context.availableContext(callback: (context: Context) -> Unit) =
        isAvailable().takeIf { it }?.let { callback(this) }


/**
 * Return true if this [Context] is available.
 * Availability is defined as the following:
 * + [Context] is not null
 * + [Context] is not destroyed
 */
fun Context?.isAvailable(): Boolean = when {
    this == null -> false
    this is Activity -> !this.isDestroyed
    else -> true
}