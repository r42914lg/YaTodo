package com.r42914lg.arkados.yatodo

import com.r42914lg.arkados.yatodo.graph.DaggerAppComponentTest

class YaTodoAppTest : YaTodoApp() {
    override val baseUrl
        get() = "https://localhost:8443/"

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponentTest.factory().create(this)
    }
}