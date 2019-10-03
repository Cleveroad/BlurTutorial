package com.cleveroad.blur_tutorial.sample.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseVM(application: Application) : AndroidViewModel(application) {

    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    private fun add(subscription: Disposable) {
        compositeDisposable.add(subscription)
    }

    protected fun Disposable.addSubscription() = add(this)
}