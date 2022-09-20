package com.r42914lg.arkados.yatodo.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.databinding.FragmentSecondBinding
import com.r42914lg.arkados.yatodo.getAppComponent
import com.r42914lg.arkados.yatodo.getColorFromAttr
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.*
import com.r42914lg.arkados.yatodo.ui.presenter.SecondFragmentPresenter

class SecondFragment : Fragment(), ITodoDetailsView {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val detailsVm: DetailsVm by activityViewModels {
        VmFactory {
            getAppComponent().getTodoDetailsFactory().create()
        }
    }

    private val mainVm: MainVm by activityViewModels {
        VmFactory {
            getAppComponent().getMainFactory().create()
        }
    }

    private val controller: SecondFragmentPresenter by lazy {
        SecondFragmentPresenter(this, detailsVm, mainVm)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
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
                setImportance(DEFAULT)
                controller.onImportanceSelected(DEFAULT)
                true
            }
            R.id.importance_low -> {
                setImportance(LOW)
                controller.onImportanceSelected(LOW)
                true
            }
            R.id.importance_high -> {
                setImportance(HIGH)
                controller.onImportanceSelected(HIGH)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun setImportance(importance: Importance) {
        binding.importanceValue.text = getStringRep(importance)
        binding.importanceValue.setTextColor(
            if (importance == DEFAULT)
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

    private fun getStringRep(importance: Importance) =
        when (importance) {
            DEFAULT -> getString(R.string.importance_default_menu_text)
            HIGH -> getString(R.string.importance_high_menu_text)
            LOW -> getString(R.string.importance_low_menu_text)
        }
}