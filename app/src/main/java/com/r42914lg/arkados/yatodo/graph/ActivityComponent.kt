package com.r42914lg.arkados.yatodo.graph

import androidx.appcompat.app.AppCompatActivity
import com.r42914lg.arkados.yatodo.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Scope
annotation class ActivityScope

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface ActivityComponent {

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent,
                   @BindsInstance activity: AppCompatActivity) : ActivityComponent
    }

    fun inject(mainActivity: MainActivity)
}