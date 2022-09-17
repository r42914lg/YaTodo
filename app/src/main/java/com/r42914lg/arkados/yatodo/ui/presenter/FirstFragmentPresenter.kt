package com.r42914lg.arkados.yatodo.ui.presenter

import android.graphics.drawable.Drawable
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.animation.ShakeAnimator
import com.r42914lg.arkados.yatodo.databinding.FragmentFirstBinding
import com.r42914lg.arkados.yatodo.getColorFromAttr
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.TodoItem
import com.r42914lg.arkados.yatodo.ui.FirstFragment
import com.r42914lg.arkados.yatodo.ui.ITodoListView

class FirstFragmentPresenter(
    private val iTodoListView: ITodoListView,
    private val mainVm: MainVm,
    private val detailsVm: DetailsVm,
) {

    private val ctx = (iTodoListView as FirstFragment).requireContext()
    private val owner = (iTodoListView as FirstFragment).viewLifecycleOwner
    private lateinit var _binding: FragmentFirstBinding
    private lateinit var loginMenuItem: MenuItem

    fun initView(binding: FragmentFirstBinding) {
        _binding = binding
        binding.toolbar.inflateMenu(R.menu.menu_fragment)
        binding.toolbar.setBackgroundColor(ctx.getColorFromAttr(R.attr.themeBackground))

        configureMenuItems()

        mainVm.todoItems.observe(owner) {
            iTodoListView.setItems(it.toMutableList())
        }

        mainVm.prepareList()

        iTodoListView.setTitle(ctx.getString(R.string.toolbar_title))

        mainVm.countCompleted.observe(owner) {
            iTodoListView.setSubtitle("${ctx.getString(R.string.toolbar_subtitle)} $it")
        }

        mainVm.currentUserName.observe(owner) {
            if (it == null) {
                loginMenuItem.title = ctx.getString(R.string.login)
            } else {
                loginMenuItem.title = ctx.getString(R.string.logout)
            }
        }
    }

    private fun configureMenuItems() {
        loginMenuItem = _binding.toolbar.menu.findItem(R.id.login)
        loginMenuItem.setOnMenuItemClickListener {
            mainVm.handleLoginOrLogoutClick()
            it.isChecked
        }

        val syncMenuItem = _binding.toolbar.menu.findItem(R.id.synchronize)
        syncMenuItem.setOnMenuItemClickListener {
            mainVm.handleSyncRequest()
            it.isChecked
        }

        val showCompletedMenuItem = _binding.toolbar.menu.findItem(R.id.show_completed)
        mainVm.showCompleted.value?.let {
            showCompletedMenuItem.isChecked = it
            showCompletedMenuItem.icon = getIconForShowCompleted(it)
        }
        showCompletedMenuItem.setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            it.icon = getIconForShowCompleted(it.isChecked)
            mainVm.setShowCompleted(it.isChecked)

            it.isChecked
        }

        val autoSyncMenuItem = _binding.toolbar.menu.findItem(R.id.autosync)
        autoSyncMenuItem.setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            log("AUTO SYNC is ${it.isChecked}")
            mainVm.setAutoRefresh(it.isChecked)
            if (it.isChecked)
                autoSyncMenuItem.setIcon(R.drawable.ic_checked)
            else
                autoSyncMenuItem.icon = null
            it.isChecked
        }

        val themeMenuItem = _binding.toolbar.menu.findItem(R.id.theme)
        themeMenuItem.setOnMenuItemClickListener {
            navigateToSettings()
            it.isChecked
        }
    }

    fun processDelete(todoItem: TodoItem) {
        mainVm.onDeleteOrRestore(todoItem, false)
    }

    fun processRestore(deletedItem: TodoItem) {
        mainVm.onDeleteOrRestore(deletedItem, true)
    }

    fun processCompleteChanged(todoItem: TodoItem) {
        mainVm.onCompleteChanged(todoItem)
    }

    fun processOpenDetails(todoItem: TodoItem) {
        detailsVm.onOpenItem(todoItem)
        navigateToSecond()
    }

    private fun navigateToSecond() {
        val navController = Navigation.findNavController(
            (iTodoListView as FirstFragment).requireActivity(),
            R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    private fun navigateToSettings() {
        val navController = Navigation.findNavController(
            (iTodoListView as FirstFragment).requireActivity(),
            R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.action_FirstFragment_to_SettingsFragment)
    }

    private fun getIconForShowCompleted(needToShowFlag: Boolean) : Drawable? =
        if (needToShowFlag)
            ContextCompat.getDrawable(ctx, R.drawable.ic_visibility_off)
        else
            ContextCompat.getDrawable(ctx, R.drawable.ic_visibility)
}