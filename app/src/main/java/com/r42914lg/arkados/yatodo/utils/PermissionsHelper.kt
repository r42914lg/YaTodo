package com.r42914lg.arkados.yatodo.utils

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.ui.IMainView
import javax.inject.Inject

class PermissionsHelper @Inject constructor(
    private val appCompatActivity: AppCompatActivity
) {
    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.CHANGE_NETWORK_STATE,
        //Manifest.permission.WRITE_SETTINGS
    )
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        appCompatActivity.registerForActivityResult(RequestMultiplePermissions()) { result ->
            if (result.containsValue(false)) {
                (appCompatActivity as IMainView)
                    .toast(appCompatActivity.getString(R.string.missing_permissions))
                appCompatActivity.finish()
            }
        }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.CHANGE_NETWORK_STATE
            ) == PackageManager.PERMISSION_GRANTED /*&& ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.WRITE_SETTINGS
            ) == PackageManager.PERMISSION_GRANTED*/
        ) {
            // happy path - proceed with app logic
        } else { // request missing permission
            requestPermissionLauncher.launch(permissions)
        }
    }

    init {
        checkPermissions()
    }
}