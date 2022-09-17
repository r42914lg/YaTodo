package com.r42914lg.arkados.yatodo.graph


import androidx.appcompat.app.AppCompatActivity
import com.r42914lg.arkados.yatodo.ui.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component
interface ActivityComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : ActivityComponent
    }

    fun inject(mainActivity: MainActivity)
}