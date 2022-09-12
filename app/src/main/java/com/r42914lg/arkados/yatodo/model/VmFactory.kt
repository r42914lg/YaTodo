package com.r42914lg.arkados.yatodo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VmFactory<T: ViewModel>(private val create: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return create.invoke() as T
    }
}