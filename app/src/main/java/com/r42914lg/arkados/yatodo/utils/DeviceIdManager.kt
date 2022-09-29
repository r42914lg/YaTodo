package com.r42914lg.arkados.yatodo.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.installations.FirebaseInstallations
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.log

@Singleton
class DeviceIdManager @Inject constructor(app: Application) {
    private val prefs: SharedPreferences =
        app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE)

    private var _deviceId: String = "LG_DEFAULT_DEV_ID"
    val deviceId
        get() = _deviceId

    init {
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            _deviceId =
                if (task.isSuccessful) {
                    val editor = prefs.edit()
                    editor.putString(DEVICE_ID, task.result)
                    editor.apply()

                    task.result
                } else {
                    fetchDeviceId() ?: ""
                }
            log("DeviceIdManager initialized FID is --> $_deviceId")
        }
    }

    private fun fetchDeviceId() =
        prefs.getString(DEVICE_ID, "")

    companion object {
        const val DEVICE_ID = "device_id"
    }
}