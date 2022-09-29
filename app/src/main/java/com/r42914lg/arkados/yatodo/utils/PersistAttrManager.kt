package com.r42914lg.arkados.yatodo.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.r42914lg.arkados.yatodo.R
import javax.inject.Inject
import javax.inject.Singleton

enum class Theme(code: Int) {
    SYS_DEFAULT(0), DARK(1), LIGHT(2)
}

@Singleton
class PersistAttrManager @Inject constructor(app: Application) {
    private val prefs: SharedPreferences =
        app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE)

    private var _uiTheme = MutableLiveData<Theme>(fetchTheme())
    val uiTheme: LiveData<Theme>
        get() = _uiTheme

    companion object {
        const val UI_THEME = "ui_theme"
    }

    fun saveUiTheme(theme: Theme) {
        _uiTheme.value = theme
        val editor = prefs.edit()
        editor.putInt(UI_THEME, theme.ordinal)
        editor.apply()
    }

    private fun fetchTheme(): Theme {
         return when (prefs.getInt(UI_THEME, 0)) {
             Theme.DARK.ordinal -> Theme.DARK
             Theme.LIGHT.ordinal -> Theme.LIGHT
             else -> Theme.SYS_DEFAULT
         }
    }
}