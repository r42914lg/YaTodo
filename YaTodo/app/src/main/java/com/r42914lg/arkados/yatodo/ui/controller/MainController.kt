package com.r42914lg.arkados.yatodo.ui.controller

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.model.DetailsVm

class MainController(
    private val appCompatActivity: AppCompatActivity,
    private val vm: DetailsVm,
) {

    fun onNewTodoItem() {
        vm.newItem()

        val navController = Navigation.findNavController(appCompatActivity, R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}