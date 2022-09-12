package com.r42914lg.arkados.yatodo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.databinding.ActivityMainBinding
import com.r42914lg.arkados.yatodo.getAppComponent
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.model.VmFactory
import com.r42914lg.arkados.yatodo.ui.controller.MainController

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var binding: ActivityMainBinding

    private val mainVm: MainVm by viewModels {
        VmFactory {
            getAppComponent().getMainFactory().create()
        }
    }

    private val detailsVm: DetailsVm by viewModels {
        VmFactory {
            getAppComponent().getTodoDetailsFactory().create()
        }
    }

    private val controller: MainController by lazy {
        MainController(this, mainVm, detailsVm)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            controller.onNewTodoItem()
        }

        controller.initView(this)
    }

    override fun setUserDetails(hideFlag: Boolean, text: String?) {
        if (hideFlag)
            binding.user.visibility = View.INVISIBLE
        else {
            binding.user.visibility = View.VISIBLE
            binding.user.text = text
        }
    }

    override fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun snackBarWithText(text: String) {
        TODO("Not yet implemented")
    }
}