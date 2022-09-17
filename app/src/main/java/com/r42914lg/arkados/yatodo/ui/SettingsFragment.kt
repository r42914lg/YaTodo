package com.r42914lg.arkados.yatodo.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.databinding.FragmentSettingsBinding
import com.r42914lg.arkados.yatodo.getAppComponent
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.model.VmFactory
import com.r42914lg.arkados.yatodo.utils.Theme

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val mainVm: MainVm by activityViewModels {
        VmFactory {
            getAppComponent().getMainFactory().create()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)

        init()
        log("onViewCreated")
    }

    private fun init() {
        when (mainVm.uiTheme.value) {
            Theme.LIGHT -> binding.light.isChecked = true
            Theme.DARK -> binding.dark.isChecked = true
            Theme.SYS_DEFAULT -> binding.systemDefault.isChecked = true
        }

        binding.dark.setOnClickListener {
            mainVm.storeUiTheme(Theme.DARK)
        }
        binding.light.setOnClickListener {
            mainVm.storeUiTheme(Theme.LIGHT)
        }
        binding.systemDefault.setOnClickListener {
            mainVm.storeUiTheme(Theme.SYS_DEFAULT)
        }

        binding.toolbar.title = getString(R.string.settings_title)
    }

    override fun onDestroyView() {
        log("onDestroyView")
        super.onDestroyView()
        _binding = null
    }
}