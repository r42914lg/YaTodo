package com.r42914lg.arkados.yatodo

import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.activity.ComponentActivity
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import com.r42914lg.arkados.yatodo.graph.AppComponent

inline fun <reified T> T.log(message: String) =
    Log.d("LG>" + T::class.java.simpleName, message)

fun ComponentActivity.getAppComponent(): AppComponent =
    (application as YaTodoApp).appComponent

fun Fragment.getAppComponent(): AppComponent =
    (requireContext().applicationContext as YaTodoApp).appComponent

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}
