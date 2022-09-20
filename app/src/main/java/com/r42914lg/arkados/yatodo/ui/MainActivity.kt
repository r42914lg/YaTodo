package com.r42914lg.arkados.yatodo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.animation.PulseAnimator
import com.r42914lg.arkados.yatodo.databinding.ActivityMainBinding
import com.r42914lg.arkados.yatodo.getAppComponent
import com.r42914lg.arkados.yatodo.graph.DaggerActivityComponent
import com.r42914lg.arkados.yatodo.model.AuxVm
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.model.VmFactory
import com.r42914lg.arkados.yatodo.ui.presenter.MainPresenter
import com.r42914lg.arkados.yatodo.utils.PermissionsHelper
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IMainView {

    @Inject
    lateinit var permissionsHelper: PermissionsHelper

    private lateinit var binding: ActivityMainBinding

    private val pulseAnimator: PulseAnimator by lazy {
        PulseAnimator(binding.fab)
    }

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

    private val auxVm: AuxVm by viewModels {
        VmFactory {
            getAppComponent().getAuxFactory().create()
        }
    }

    private val controller: MainPresenter by lazy {
        MainPresenter(this, mainVm, detailsVm, auxVm)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerActivityComponent.factory().create(this)
            .inject(this)

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

    override fun setNetworkStatus(text: String) {
        binding.network.text = text
    }

    override fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun snackBarWithText(text: String) {
        Snackbar.make(binding.network, text, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.snack_refresh)) {
                controller.onRefresh()
            }
            .show()
    }

    override fun showFab(showFlag: Boolean) {
        binding.fab.visibility = if (showFlag) View.VISIBLE else View.INVISIBLE
    }

    override fun animateFab(animateFlag: Boolean) {
        if (animateFlag)
            pulseAnimator.start()
        else
            pulseAnimator.stop()
    }
}