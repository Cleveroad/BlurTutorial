package com.cleveroad.blur_tutorial.utils

import android.os.AsyncTask

internal fun<T> doOnBackground(work: () -> T, onResult: (T) -> Unit) =
    object : AsyncTask<Unit, Unit, T>() {
        override fun doInBackground(vararg params: Unit?) = work()

        override fun onPostExecute(result: T) {
            onResult(result)
        }
    }.apply { execute() }