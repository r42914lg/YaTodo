package com.r42914lg.arkados.yatodo.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.databinding.FragmentSecondBinding
import com.r42914lg.arkados.yatodo.getAppComponent
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.VmFactory
import com.r42914lg.arkados.yatodo.ui.controller.FirstFragmentController
import com.r42914lg.arkados.yatodo.ui.controller.SecondFragmentController

class SecondFragment : Fragment(), ITodoDetailsView {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val detailsVm: DetailsVm by activityViewModels {
        VmFactory {
            getAppComponent().getTodoDetailsFactory().create()
        }
    }

    private val controller: SecondFragmentController by lazy {
        SecondFragmentController(this, detailsVm)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerForContextMenu(binding.importanceClickable)

        controller.initView(binding)
        log("onViewCreated")
    }

    override fun onDestroyView() {
        log("onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.menu_importance, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.importance_default -> {
                binding.importanceValue.text = getString(R.string.importance_default_menu_text)
                true
            }
            R.id.importance_low -> {
                binding.importanceValue.text = getString(R.string.importance_low_menu_text)
                true
            }
            R.id.importance_high -> {
                binding.importanceValue.text = getString(R.string.importance_high_menu_text)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun setImportance(text: String) {
        binding.importanceValue.text = text
    }

    override fun setDeadline(text: String) {
        binding.todoBy.text = text
    }

    override fun setTodoText(text: String) {
        binding.todoInputText.setText(text)
    }

    override fun setDateSwitchChecked(boolean: Boolean) {
        binding.dateSwitch.isChecked = boolean
    }
}