package com.cleveroad.blur_tutorial.sample.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.cleveroad.blur_tutorial.sample.SampleApp
import com.cleveroad.blur_tutorial.sample.utils.SimpleAnimationListener

abstract class BaseLifecycleFragment<T : BaseVM> : Fragment() {

    abstract val viewModelClass: Class<T>

    protected abstract val layoutId: Int

    protected val viewModel: T by lazy {
        ViewModelProviders.of(this).get(viewModelClass)
    }


    protected var toolbar: Toolbar? = null

    protected val ctx: Context
        get() = context ?: SampleApp.instance

    abstract fun observeLiveData()

    /**
     * Callback, that called, when fragment enter animation is finished.
     */
    protected open fun onEnterAnimationEnd() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutId, container, false)

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? =
            when (nextAnim) {
                0 -> super.onCreateAnimation(transit, enter, nextAnim)
                else -> AnimationUtils.loadAnimation(ctx, nextAnim)
                        .takeIf { enter }
                        ?.apply {
                            setAnimationListener(object : SimpleAnimationListener() {

                                override fun onAnimationEnd(animation: Animation?) {
                                    onEnterAnimationEnd()
                                }
                            })
                        }
            }
}