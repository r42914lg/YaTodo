package com.r42914lg.arkados.yatodo.utils

import android.app.Application
import android.content.*
import android.net.NetworkCapabilities
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkTracker @Inject constructor(app: Application) {
    private var _isOnline = MutableLiveData(false)
    val isOnline : LiveData<Boolean>
        get() = _isOnline

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback: NetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _isOnline.postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isOnline.postValue(false)
            }
        }
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}