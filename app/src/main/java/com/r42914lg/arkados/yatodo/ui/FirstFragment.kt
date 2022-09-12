package com.r42914lg.arkados.yatodo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.r42914lg.arkados.yatodo.databinding.FragmentFirstBinding
import com.r42914lg.arkados.yatodo.getAppComponent
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.*
import com.r42914lg.arkados.yatodo.ui.controller.FirstFragmentController


class FirstFragment : Fragment(), ITodoListView {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkItemAdapter
    private val list = mutableListOf<TodoItem>()

    private val mainVm: MainVm by activityViewModels {
        VmFactory {
            getAppComponent().getMainFactory().create()
        }
    }

    private val detailsVm: DetailsVm by activityViewModels {
        VmFactory {
            getAppComponent().getTodoDetailsFactory().create()
        }
    }

    private val controller: FirstFragmentController by lazy {
        FirstFragmentController(this, mainVm, detailsVm)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.layoutManager = LinearLayoutManager(context)
        adapter = WorkItemAdapter(list, requireContext(), controller)
        binding.recycler.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeHandler(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recycler)

        controller.initView(binding)
        log("onViewCreated")
    }

    override fun onDestroyView() {
        log("onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun setTitle(text: String) {
        binding.toolbar.title = text
    }

    override fun setSubtitle(text: String) {
        binding.toolbar.subtitle = text
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setItems(newItems: List<TodoItem>) {
        list.clear()
        list.addAll(newItems)
        adapter.notifyDataSetChanged()
    }
}