package com.naver.android.svc.core.views

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.naver.android.svc.SvcConfig
import com.naver.android.svc.core.ActivityProvider

/**
 * @author bs.nam@navercorp.com 2017. 6. 8..
 */

abstract class SvcBaseViews<out Owner : ActivityProvider>(val owner: Owner) : LifecycleObserver {

    val CLASS_SIMPLE_NAME = javaClass.simpleName
    var TAG: String = CLASS_SIMPLE_NAME

    var rootView: ViewGroup? = null

    val context: Context?
        get() = owner.getActivity()

    @get:LayoutRes
    abstract val layoutResId: Int

    val isInitialized: Boolean
        get() = rootView != null


    //------LifeCycle START------
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun logOnCreate() {
        if (SvcConfig.debugMode) {
            Log.d(TAG, "onCreate")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    abstract fun onCreated()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStarted() {
        if (SvcConfig.debugMode) {
            Log.d(TAG, "onStarted")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResumed() {
        if (SvcConfig.debugMode) {
            Log.d(TAG, "onResumed")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        if (SvcConfig.debugMode) {
            Log.d(TAG, "onPause")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        if (SvcConfig.debugMode) {
            Log.d(TAG, "onStop")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        rootView = null
        if (SvcConfig.debugMode) {
            Log.d(TAG, "onDestroy")
        }
    }

    //------LifeCycle END------

    fun post(runnable: () -> Unit) {
        rootView?.post(runnable)
    }

    fun postDelayed(runnable: Runnable, delayMillis: Int) {
        rootView?.postDelayed(runnable, delayMillis.toLong())
    }

    fun removeCallbacks(runnable: Runnable) {
        rootView?.removeCallbacks(runnable)
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getColor(@ColorRes colorRes: Int): Int {
        var context = context
        if (context == null) {
            context = getMainApplicationContext()
        }
        context ?: return 0
        return ContextCompat.getColor(context, colorRes)
    }

    fun getDimen(@DimenRes dimenId: Int): Int {
        var context = context
        if (context == null) {
            context = getMainApplicationContext()
        }
        context ?: return 0
        return context.resources.getDimensionPixelSize(dimenId)
    }

    fun getString(@StringRes stringId: Int): String {
        val context = context
        context ?: return ""
        return context.resources.getString(stringId)
    }

    fun isDestroyed(): Boolean {
        return !isInitialized
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    fun <T : View> findViewById(id: Int): T? {
        return rootView?.findViewById(id)
    }

    /**
     * when use getDimen or getString in contructor
     * there is no context before inflating and setting rootView
     *
     * to use getDimen or getString in constructor
     * you can override this function on your BaseViews.
     */
    open fun getMainApplicationContext(): Context? {
        return null
    }
}
