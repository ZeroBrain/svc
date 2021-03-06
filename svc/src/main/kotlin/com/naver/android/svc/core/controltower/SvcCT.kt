/*
 * Copyright 2018 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.naver.android.svc.core.controltower

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.naver.android.svc.SvcConfig
import com.naver.android.svc.core.screen.SvcScreen
import com.naver.android.svc.core.views.SvcViews


/**
 * this class is dealing with lifecycle of screen(Activity, Fragment, Dialog)
 * mainly the first initialize screen logic
 * and receives view events from usecase(User Interaction)
 *
 * @author bs.nam@navercorp.com 2017. 6. 8..
 */

abstract class SvcCT<out Screen : SvcScreen<V, *>, out V : SvcViews<*>>(val screen: Screen, val views: V) : LifecycleObserver {

    val CLASS_SIMPLE_NAME = javaClass.simpleName
    var TAG: String = CLASS_SIMPLE_NAME

    val activity: FragmentActivity? = screen.hostActivity


    //------LifeCycle START------
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun logOnCreate() {
        if (SvcConfig.debugMode) {
            Log.d(TAG, "onCreate")
        }
    }

    /**
     * is called after views inflated
     */
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
        if (SvcConfig.debugMode) {
            Log.d(TAG, "onDestroy")
        }
    }

    //------LifeCycle END------

    fun showToast(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun showDialog(dialogFragment: DialogFragment) {
        val activity = activity ?: return
        dialogFragment.show(activity.supportFragmentManager, dialogFragment.javaClass.simpleName)
    }

    fun showDialog(dialogFragment: DialogFragment, sharedElement: View, elementId: String) {
        val activity = activity ?: return
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.addSharedElement(sharedElement, elementId)
        dialogFragment.show(transaction, dialogFragment.javaClass.simpleName)
    }

    fun finishActivity() {
        activity?.finish()
    }

    open fun onBackPressed(): Boolean {
        return false
    }

}
