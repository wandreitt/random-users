package com.andrei.wegroszta.randomusers.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.doOnLifecycleStartedState(action: suspend CoroutineScope.() -> Unit) {
    doOnLifecycleState(Lifecycle.State.STARTED, action)
}

fun Fragment.doOnLifecycleState(
    lifecycleState: Lifecycle.State,
    action: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
            action()
        }
    }
}

fun Fragment.setSupportTitle(title: String) {
    (activity as? AppCompatActivity)?.let { ac ->
        ac.supportActionBar?.title = title
    }
}
