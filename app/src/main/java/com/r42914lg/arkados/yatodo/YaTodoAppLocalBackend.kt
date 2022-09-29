package com.r42914lg.arkados.yatodo

import com.r42914lg.arkados.yatodo.graph.DaggerAppComponentLocalBackend

class YaTodoAppLocalBackend : YaTodoApp() {
    override val baseUrl
        get() = "https://10.0.2.2:8443/"

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponentLocalBackend.factory().create(this)
    }
}