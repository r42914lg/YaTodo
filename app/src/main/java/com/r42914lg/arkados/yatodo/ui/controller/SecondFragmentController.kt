package com.r42914lg.arkados.yatodo.ui.controller

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.datepicker.MaterialDatePicker
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.databinding.FragmentSecondBinding
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.Importance
import com.r42914lg.arkados.yatodo.ui.ITodoDetailsView
import com.r42914lg.arkados.yatodo.ui.SecondFragment

class SecondFragmentController(
    private val iTodoDetailsView: ITodoDetailsView,
    private val vm: DetailsVm,
) {

    private val owner = (iTodoDetailsView as SecondFragment).viewLifecycleOwner

    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker().build()
    }

    fun initView(binding: FragmentSecondBinding) {

        vm.todoItem.observe(owner) {
            iTodoDetailsView.setImportance(it.importance.stringRep())
            iTodoDetailsView.setTodoText(it.text)
            it.deadline?.let {
                    it1 -> iTodoDetailsView.setDeadline(it1)
            }
            iTodoDetailsView.setDateSwitchChecked(!it.deadline.isNullOrEmpty())
        }

        binding.buttonSave.setOnClickListener {
            vm.onSaveItem(
                binding.todoInputText.text.toString(),
                Importance.Factory().parseFromStr(binding.importanceValue.text.toString()),
                binding.todoDate.text.toString()
            )
            navigateToFirst()
        }

        binding.buttonClose.setOnClickListener {
            navigateToFirst()
        }

        binding.buttonDelete.setOnClickListener {
            vm.onDelete()
            navigateToFirst()
        }

        datePicker.addOnPositiveButtonClickListener {
            binding.todoDate.text = datePicker.headerText
        }

        datePicker.addOnNegativeButtonClickListener {
            iTodoDetailsView.setDateSwitchChecked(false)
        }

        datePicker.addOnCancelListener {
            iTodoDetailsView.setDateSwitchChecked(false)
        }

        binding.dateSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                datePicker.show((iTodoDetailsView as Fragment).parentFragmentManager, "TAG");
            }  else {
                iTodoDetailsView.setDeadline("")
            }
        }
    }

    private fun navigateToFirst() {
        val navController = Navigation.findNavController(
            (iTodoDetailsView as SecondFragment).requireActivity(),
            R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.action_SecondFragment_to_FirstFragment)
    }
}