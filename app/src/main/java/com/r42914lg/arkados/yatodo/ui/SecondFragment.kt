package com.r42914lg.arkados.yatodo.ui

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.databinding.FragmentSecondBinding
import com.r42914lg.arkados.yatodo.getAppComponent
import com.r42914lg.arkados.yatodo.getColorFromAttr
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.DEFAULT
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.model.VmFactory
import com.r42914lg.arkados.yatodo.ui.presenter.SecondFragmentPresenter

class SecondFragment : Fragment(), ITodoDetailsView {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val detailsVm: DetailsVm by activityViewModels {
        VmFactory {
            getAppComponent().getTodoDetailsFactory().create()
        }
    }

    private val controller: SecondFragmentPresenter by lazy {
        SecondFragmentPresenter(this, detailsVm)
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

        binding.todoInputText.addTextChangedListener {
            controller.enableSaveDelete(!it.isNullOrEmpty())
        }

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
                setImportance(getString(R.string.importance_default_menu_text))
                true
            }
            R.id.importance_low -> {
                setImportance(getString(R.string.importance_low_menu_text))
                true
            }
            R.id.importance_high -> {
                setImportance(getString(R.string.importance_high_menu_text))
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun setImportance(text: String) {
        binding.importanceValue.text = text
        binding.importanceValue.setTextColor(
            if (text == DEFAULT.stringRep())
                requireContext().getColorFromAttr(R.attr.disableColor)
            else
                requireContext().getColorFromAttr(com.google.android.material.R.attr.colorOnPrimary)
        )
    }

    override fun setDeadline(text: String) {
        binding.todoDate.text = text
    }

    override fun setTodoText(text: String) {
        binding.todoInputText.setText(text)
    }

    override fun setDateSwitchChecked(boolean: Boolean) {
        binding.dateSwitch.isChecked = boolean
    }
}