package com.r42914lg.arkados.yatodo.ui.presenter

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.model.AuxVm
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.ui.IMainView
import com.r42914lg.arkados.yatodo.utils.FirebaseHelper
import com.r42914lg.arkados.yatodo.utils.Theme

class MainPresenter(
    private val appCompatActivity: AppCompatActivity,
    private val mainVm: MainVm,
    private val detailsVm: DetailsVm,
    private val auxVm: AuxVm,
) {

    private lateinit var _iMainView: IMainView
    private var wasOnlineBefore = false

    private var _accountPicker = appCompatActivity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK)
            onSinginFailure()

        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)?.addOnCompleteListener {
            if (it.isSuccessful && !it.result.token.isNullOrEmpty()) {
                user.displayName?.let { it1 -> FirebaseHelper.logUserLogin(it1) }
                mainVm.onSignSuccess(user, it.result.token!!)
            } else
                onSinginFailure()
        }
    }

    fun initView(iMainView: IMainView) {
        _iMainView = iMainView

        mainVm.toastUi.observe(appCompatActivity) {
            if (it != null) {
                iMainView.toast(it)
                mainVm.toastShown()
            }
        }

        mainVm.eventSigninRefreshRequest.observe(appCompatActivity) {
            if (!it)
                return@observe

            if (mainVm.currentUser == null) {
                mainVm.onSigninRefreshFailure()
                launchSignInFlow()
            }

            mainVm.currentUser?.getIdToken(false)?.addOnCompleteListener { it1 ->
                if (it1.isSuccessful && !it1.result.token.isNullOrEmpty())
                    mainVm.onSignRefreshSuccess(it1.result.token!!)
                else {
                    mainVm.onSigninRefreshFailure()
                    launchSignInFlow()
                }
            }
        }

        mainVm.eventLoginRequest.observe(appCompatActivity) {
            if (!it)
                return@observe

            launchSignInFlow()
        }

        mainVm.currentUserName.observe(appCompatActivity) {
            iMainView.setUserDetails(it == null, it)
        }

        mainVm.networkStatus.observe(appCompatActivity) {
            if (it && wasOnlineBefore) {
                iMainView.snackBarWithText(appCompatActivity.getString(R.string.back_online_msg))
                wasOnlineBefore = false
            }
            if (it)
                wasOnlineBefore = true

            iMainView.setNetworkStatus(
                if (it) appCompatActivity.getString(R.string.network_online)
                else appCompatActivity.getString(R.string.network_offline))
        }

        mainVm.evenLogoutRequest.observe(appCompatActivity) {
            if (!it)
                return@observe

            AuthUI.getInstance().signOut(appCompatActivity)
            mainVm.currentUserName.value?.let { it1 -> FirebaseHelper.logUserLogout(it1) }
            mainVm.onSingOut()
        }

        auxVm.uiTheme.observe(appCompatActivity) {
            when (it) {
                Theme.LIGHT  -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Theme.SYS_DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        mainVm.showFab.observe(appCompatActivity) {
            it?.let {
                iMainView.showFab(it)
            }
        }

        mainVm.animateFab.observe(appCompatActivity) {
            it?.let {
                iMainView.animateFab(it)
            }
        }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        _accountPicker.launch(intent)
    }

    fun onNewTodoItem() {
        if (mainVm.currentUserName.value == null) {
            _iMainView.toast(appCompatActivity.getString(R.string.force_signin_message))
            return
        }

        detailsVm.newItem()

        val navController = Navigation.findNavController(appCompatActivity, R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    private fun onSinginFailure() {
        mainVm.onSingOut()
        mainVm.onSignFailure()
    }

    fun onRefresh() {
        mainVm.handleSyncRequest(true)
    }
}