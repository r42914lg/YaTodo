package com.r42914lg.arkados.yatodo.utils

import androidx.appcompat.app.AppCompatActivity
import android.content.*
import android.net.NetworkCapabilities
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.r42914lg.arkados.yatodo.model.MainVm

class NetworkTracker constructor(appCompatActivity: AppCompatActivity, vm: MainVm) {
    private var isOnline = false

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback: NetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (!isOnline) {
                    vm.setNetworkStatus(true)
                }
                isOnline = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                if (isOnline) {
                    vm.setNetworkStatus(false)
                }
                isOnline = false
            }
        }
        val connectivityManager =
            appCompatActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}