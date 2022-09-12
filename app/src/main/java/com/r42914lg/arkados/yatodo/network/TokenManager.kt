package com.r42914lg.arkados.yatodo.network

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.r42914lg.arkados.yatodo.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(app: Application) {
    private val prefs: SharedPreferences =
        app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE)

    private var _user = MutableLiveData<String?>(fetchUserName())
    val userName: LiveData<String?>
        get() = _user

    private var _token: String? = fetchAuthToken()
    val token: String?
        get() = _token

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"
    }

    fun saveAuthToken(theName: String, theToken: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, theToken)
        editor.putString(USER_NAME, theName)
        editor.apply()

        _user.value = theName
        _token = theToken
    }

    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()

        _user.value =  null
        _token = null
    }

    private fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    private fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }
}