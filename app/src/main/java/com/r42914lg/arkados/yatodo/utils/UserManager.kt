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

interface IUserManager {
    val userName: LiveData<String?>
    val token: String?
    val user: FirebaseUser?

    fun saveAuthToken(firebaseUser: FirebaseUser, token: String) {}
    fun saveAuthToken(token: String) {}
    fun clearAuthToken() {}
}

@Singleton
class UserManager @Inject constructor(app: Application) : IUserManager {
    private val prefs: SharedPreferences =
        app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE)

    private var _userName = MutableLiveData<String?>(fetchUserName())
    override val userName: LiveData<String?>
        get() = _userName

    private var _token: String? = fetchAuthToken()
    override val token: String?
        get() = _token

    private var _user: FirebaseUser? = null
    override val user: FirebaseUser?
        get() = _user

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"
    }

    override fun saveAuthToken(firebaseUser: FirebaseUser, token: String) {
        _user = firebaseUser
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_NAME, firebaseUser.displayName)
        editor.apply()

        _userName.value = firebaseUser.displayName
        _token = token
    }

    override fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    override fun clearAuthToken() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()

        _user = null
        _userName.value =  null
        _token = null
    }

    private fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    private fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }
}

@Singleton
class UserManagerTest @Inject constructor(): IUserManager {
    override val userName: LiveData<String?>
        get() = MutableLiveData<String>("Test User")

    override val token: String?
        get() = "Test_token"

    override val user: FirebaseUser?
        get() = null
}