package com.r42914lg.arkados.yatodo

import android.app.Application
import com.r42914lg.arkados.yatodo.graph.AppComponent
import com.r42914lg.arkados.yatodo.graph.DaggerAppComponent

class YaTodoApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}