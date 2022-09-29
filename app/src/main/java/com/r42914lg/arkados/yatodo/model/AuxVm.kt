package com.r42914lg.arkados.yatodo.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.utils.PersistAttrManager
import com.r42914lg.arkados.yatodo.utils.Theme
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AuxVm @AssistedInject constructor(
    app: Application,
    private val persistAttrManager: PersistAttrManager,
) : AndroidViewModel(app) {

    @AssistedFactory
    interface Factory {
        fun create(): AuxVm
    }

    private var _uiTheme = persistAttrManager.uiTheme
    val uiTheme: LiveData<Theme>
        get() = _uiTheme

    fun storeUiTheme(theme: Theme) {
        log("storeUiTheme -> new them selected: $theme")
        persistAttrManager.saveUiTheme(theme)
    }

}