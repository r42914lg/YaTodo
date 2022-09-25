package com.r42914lg.arkados.yatodo

import android.app.Application
import com.r42914lg.arkados.yatodo.graph.AppComponent
import com.r42914lg.arkados.yatodo.graph.DaggerAppComponent

interface IMyApp {
    val baseUrl: String
    val appComponent: AppComponent
}

open class YaTodoApp : Application(), IMyApp {
    override lateinit var appComponent: AppComponent

    override val baseUrl
        get() = "https://d5dtbgsodb2erid89lun.apigw.yandexcloud.net/"

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

    companion object {
        const val REFRESH_INTERVAL = 60000L
    }
}