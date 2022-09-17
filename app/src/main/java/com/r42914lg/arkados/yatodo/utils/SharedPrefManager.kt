package com.r42914lg.arkados.yatodo.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.r42914lg.arkados.yatodo.R
import javax.inject.Inject
import javax.inject.Singleton


enum class Theme(code: Int) {
    SYS_DEFAULT(0), DARK(1), LIGHT(2)
}

@Singleton
class SharedPrefManager @Inject constructor(app: Application) {
    private val prefs: SharedPreferences =
        app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE)

    private var _uiTheme = MutableLiveData<Theme>(fetchTheme())
    val uiTheme: LiveData<Theme>
        get() = _uiTheme

    private var _userName = MutableLiveData<String?>(fetchUserName())
    val userName: LiveData<String?>
        get() = _userName

    private var _token: String? = fetchAuthToken()
    val token: String?
        get() = _token

    private var _user: FirebaseUser? = null
    val user: FirebaseUser?
        get() = _user

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"
        const val UI_THEME = "ui_theme"
    }

    fun saveUiTheme(theme: Theme) {
        _uiTheme.value = theme
        val editor = prefs.edit()
        editor.putInt(UI_THEME, theme.ordinal)
        editor.apply()
    }

    fun saveAuthToken(firebaseUser: FirebaseUser, token: String) {
        _user = firebaseUser
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_NAME, firebaseUser.displayName)
        editor.apply()

        _userName.value = firebaseUser.displayName
        _token = token
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()

        _user = null
        _userName.value =  null
        _token = null
    }

    private fun fetchTheme(): Theme {
         return when (prefs.getInt(UI_THEME, 0)) {
             Theme.DARK.ordinal -> Theme.DARK
             Theme.LIGHT.ordinal -> Theme.LIGHT
             else -> Theme.SYS_DEFAULT
         }
    }

    private fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    private fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }
}