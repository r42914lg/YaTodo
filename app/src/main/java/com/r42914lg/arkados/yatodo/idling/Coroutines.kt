package com.r42914lg.arkados.yatodo.idling

import com.r42914lg.arkados.yatodo.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchIdling(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {

    if (BuildConfig.DEBUG)
        EspressoIdlingResource.increment()

    val job = this.launch(context, start, block)

    if (BuildConfig.DEBUG)
        job.invokeOnCompletion { EspressoIdlingResource.decrement() }

    return job
}