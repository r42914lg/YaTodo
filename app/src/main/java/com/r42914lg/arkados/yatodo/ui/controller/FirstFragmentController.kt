package com.r42914lg.arkados.yatodo.ui.controller

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.Navigation
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.databinding.FragmentFirstBinding
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.TodoItem
import com.r42914lg.arkados.yatodo.ui.FirstFragment
import com.r42914lg.arkados.yatodo.ui.ITodoListView

class FirstFragmentController(
    private val iTodoListView: ITodoListView,
    private val mainVm: MainVm,
    private val detailsVm: DetailsVm,
) {

    private val ctx = (iTodoListView as FirstFragment).requireContext()
    private val owner = (iTodoListView as FirstFragment).viewLifecycleOwner

    fun initView(binding: FragmentFirstBinding) {
        binding.toolbar.inflateMenu(R.menu.menu_fragment)
        val showCompleted = binding.toolbar.menu[0]

        mainVm.showCompleted.value?.let {
            showCompleted.isChecked = it
            showCompleted.icon = getIconForShowCompleted(it)
        }

        showCompleted.setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            it.icon = getIconForShowCompleted(it.isChecked)
            mainVm.setShowCompleted(it.isChecked)

            it.isChecked
        }

        mainVm.todoItems.observe(owner) {
            iTodoListView.setItems(it)
        }

        mainVm.prepareList()

        iTodoListView.setTitle(ctx.getString(R.string.toolbar_title))

        mainVm.countCompleted.observe(owner) {
            iTodoListView.setSubtitle("${ctx.getString(R.string.toolbar_subtitle)} $it")
        }
    }

    fun processDelete(todoItem: TodoItem) {
        mainVm.onDelete(todoItem)
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

    private fun getIconForShowCompleted(needToShowFlag: Boolean) : Drawable? =
        if (needToShowFlag)
            ContextCompat.getDrawable(ctx, R.drawable.ic_visibility_off)
        else
            ContextCompat.getDrawable(ctx, R.drawable.ic_visibility)
}