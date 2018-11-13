package io.github.landerlyoung.kotlin.mpp

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.concurrent.ConcurrentHashMap

/**
 * <pre>
 * Author: landerlyoung@gmail.com
 * Date:   2018-11-10
 * Time:   16:23
 * Life with Passion, Code with Creativity.
 * </pre>
 */

private val _coroutineScopes = ConcurrentHashMap<LifecycleOwner, CoroutineScope>()

val LifecycleOwner.coroutineScope: CoroutineScope
    get() {
        return _coroutineScopes.getOrPut(this) {
            val job = Job()
            val cs = CoroutineScope(job + Dispatchers.Main)

            lifecycle.addObserver(GenericLifecycleObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    job.cancel()
                }
            })
            cs
        }.also {
            if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
                // remove from map, to prevent from leak
                _coroutineScopes.remove(this)
            }
        }
    }