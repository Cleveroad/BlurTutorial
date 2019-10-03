package com.cleveroad.blur_tutorial.sample.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.AnimatorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE

abstract class BaseLifecycleActivity : AppCompatActivity() {

    protected abstract val containerId: Int

    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    protected fun replaceFragment(fragment: Fragment,
                                  needToAddToBackStack: Boolean = false,
                                  @AnimatorRes inAnimRes: Int = 0,
                                  @AnimatorRes outAnimRes: Int = 0) {
        val name = fragment.javaClass.simpleName
        supportFragmentManager.beginTransaction().apply {
            if (inAnimRes != 0 || outAnimRes != 0) setCustomAnimations(inAnimRes, outAnimRes)
            replace(containerId, fragment, name)
            if (needToAddToBackStack) addToBackStack(name)
        }.commit()
    }

    protected fun removeFragmentByTag(vararg tags: String) {
        with(supportFragmentManager) {
            for (fragmentTag in tags) {
                findFragmentByTag(fragmentTag)?.let {
                    beginTransaction().remove(it).commit()
                    popBackStackImmediate()
                }
            }
        }
    }

    protected fun clearFragmentBackStack() {
        supportFragmentManager.fragments.forEach {
            clearBackStackRecursive(it)
        }
    }

    private fun clearBackStackRecursive(fragment: Fragment) {
        fragment.childFragmentManager.run {
            fragments.forEach {
                clearBackStackRecursive(it)
            }
            popBackStack(null, POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(containerId)?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        supportFragmentManager.findFragmentById(containerId)
                ?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}