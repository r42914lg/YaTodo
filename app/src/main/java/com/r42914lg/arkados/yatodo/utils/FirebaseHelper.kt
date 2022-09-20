package com.r42914lg.arkados.yatodo.utils

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object FirebaseHelper {

    private const val SERVER_ERROR_EVENT = "backend_error"
    private const val USER_LOGIN_EVENT = "user_login"
    private const val USER_LOGOUT_EVENT = "user_logout"
    private const val ERROR_MSG = "error"
    private const val USER_NAME = "user"

    fun logServerError(s: String) {
        val bundle = Bundle().apply { putString(ERROR_MSG, s) }
        Firebase.analytics.logEvent(SERVER_ERROR_EVENT, bundle)
    }

    fun logUserLogin(s: String) {
        val bundle = Bundle().apply { putString(USER_NAME, s) }
        Firebase.analytics.logEvent(USER_LOGIN_EVENT, bundle)
    }

    fun logUserLogout(s: String) {
        val bundle = Bundle().apply { putString(USER_NAME, s) }
        Firebase.analytics.logEvent(USER_LOGOUT_EVENT, bundle)
    }


}