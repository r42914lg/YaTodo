package com.r42914lg.arkados.yatodo.ui.controller

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.ui.IMainView
import kotlin.concurrent.timerTask

class MainController(
    private val appCompatActivity: AppCompatActivity,
    private val mainVm: MainVm,
    private val detailsVm: DetailsVm,
) {

    private lateinit var _iMainView: IMainView

    private var _accountPicker = appCompatActivity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                user!!.getIdToken(true).addOnCompleteListener {
                    if (it.isSuccessful
                            && !it.result.token.isNullOrEmpty()) {
                        mainVm.onSignSuccess(user.displayName!!, it.result.token!!)
                    } else {
                        mainVm.onSingOut()
                        mainVm.onSignFailure()
                    }
                }
            } else {
                mainVm.onSignFailure()
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

        mainVm.eventLoginRequest.observe(appCompatActivity) {
            if (!it)
                return@observe

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

        mainVm.currentUser.observe(appCompatActivity) {
            iMainView.setUserDetails(it == null, it)
        }

        mainVm.evenLogoutRequest.observe(appCompatActivity){
            if (!it)
                return@observe

            AuthUI.getInstance().signOut(appCompatActivity)
            mainVm.onSingOut()
        }
    }

    fun onNewTodoItem() {
        if (mainVm.currentUser.value == null) {
            _iMainView.toast(appCompatActivity.getString(R.string.force_signin_message))
            return
        }

        detailsVm.newItem()

        val navController = Navigation.findNavController(appCompatActivity, R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}