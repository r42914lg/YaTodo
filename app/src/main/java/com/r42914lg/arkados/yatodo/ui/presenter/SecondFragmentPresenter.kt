package com.r42914lg.arkados.yatodo.ui.presenter

import android.content.res.ColorStateList
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.datepicker.MaterialDatePicker
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.databinding.FragmentSecondBinding
import com.r42914lg.arkados.yatodo.getColorFromAttr
import com.r42914lg.arkados.yatodo.model.DEFAULT
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.Importance
import com.r42914lg.arkados.yatodo.ui.ITodoDetailsView
import com.r42914lg.arkados.yatodo.ui.SecondFragment
import java.util.*


class SecondFragmentPresenter(
    private val iTodoDetailsView: ITodoDetailsView,
    private val vm: DetailsVm,
) {

    private val owner = (iTodoDetailsView as SecondFragment).viewLifecycleOwner
    private val ctx = (iTodoDetailsView as Fragment).requireContext()
    private lateinit var _binding: FragmentSecondBinding

    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.DatePicker)
            .build()
    }

    fun initView(binding: FragmentSecondBinding) {
        _binding = binding

        vm.todoItem.observe(owner) {
            iTodoDetailsView.setImportance(it.importance.stringRep())
            iTodoDetailsView.setTodoText(it.text)
            it.deadline?.let {
                    it1 -> iTodoDetailsView.setDeadline(it1)
            }
            iTodoDetailsView.setDateSwitchChecked(!it.deadline.isNullOrEmpty())
        }

       addButtonListeners()
    }

    private fun addButtonListeners() {
        _binding.buttonSave.setOnClickListener {
            vm.onSaveItem(
                _binding.todoInputText.text.toString(),
                Importance.Factory().parseFromStr(_binding.importanceValue.text.toString()),
                _binding.todoDate.text.toString()
            )
            navigateToFirst()
        }

        _binding.buttonClose.setOnClickListener {
            navigateToFirst()
        }

        _binding.buttonDelete.setOnClickListener {
            vm.onDelete()
            navigateToFirst()
        }

        datePicker.addOnPositiveButtonClickListener {
            iTodoDetailsView.setDeadline(datePicker.headerText)
        }

        datePicker.addOnNegativeButtonClickListener {
            iTodoDetailsView.setDateSwitchChecked(false)
        }

        datePicker.addOnCancelListener {
            iTodoDetailsView.setDateSwitchChecked(false)
        }

        _binding.dateSwitch.setOnCheckedChangeListener { _, isChecked ->
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

    fun enableSaveDelete(b: Boolean) {
        _binding.buttonSave.isEnabled = b
        _binding.buttonDelete.isEnabled = b
    }
}