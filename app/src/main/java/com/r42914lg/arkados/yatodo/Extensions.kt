package com.r42914lg.arkados.yatodo

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.r42914lg.arkados.yatodo.graph.AppComponent
import com.r42914lg.arkados.yatodo.model.TodoItem
import java.util.stream.Collectors

inline fun <reified T> T.log(message: String) =
    Log.d("LG>" + T::class.java.simpleName, message)

fun ComponentActivity.getAppComponent(): AppComponent =
    (application as YaTodoApp).appComponent

fun Fragment.getAppComponent(): AppComponent =
    (requireContext().applicationContext as YaTodoApp).appComponent
