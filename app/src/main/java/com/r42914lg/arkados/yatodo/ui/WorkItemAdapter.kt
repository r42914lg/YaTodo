package com.r42914lg.arkados.yatodo.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.*
import com.r42914lg.arkados.yatodo.ui.controller.FirstFragmentController

class WorkItemAdapter(
    private val list: MutableList<TodoItem>,
    private val _context: Context,
    private val controller: FirstFragmentController
) : RecyclerView.Adapter<WorkItemAdapter.WorkItemViewHolder>(),
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    val context: Context
        get() = _context

    class WorkItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doneCheckBox: CheckBox = itemView.findViewById(R.id.todo_done)
        val importanceTextView: TextView = itemView.findViewById(R.id.todo_importance)
        val todoText: TextView = itemView.findViewById(R.id.todo_text)
        val todoDate: TextView = itemView.findViewById(R.id.todo_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkItemViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_row, parent, false)
        return WorkItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: WorkItemViewHolder, position: Int) {
        holder.doneCheckBox.tag = position
        holder.doneCheckBox.setOnCheckedChangeListener(null)
        holder.doneCheckBox.isChecked = list[position].done
        holder.doneCheckBox.setOnCheckedChangeListener(this)

        holder.todoText.text = list[position].text
        holder.todoDate.text = list[position].deadline
        holder.importanceTextView.text = getImportancePrefixText(list[position].importance)

        if (list[position].done)
            holder.todoText.paintFlags = holder.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            holder.todoText.paintFlags = Paint.ANTI_ALIAS_FLAG

        val color = getImportancePrefixColor(list[position].importance)
        holder.importanceTextView.setTextColor(color)
        holder.doneCheckBox.buttonTintList = ColorStateList.valueOf(color)

        holder.itemView.tag = list[position]
        holder.itemView.setOnClickListener(this)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getImportancePrefixText(importance: Importance) : String {
        return when (importance) {
            LOW -> context.getString(R.string.importance_prefix_low)
            DEFAULT -> context.getString(R.string.importance_prefix_default)
            HIGH -> context.getString(R.string.importance_prefix_high)
        }
    }

    private fun getImportancePrefixColor(importance: Importance) : Int {
        return when (importance) {
            LOW -> context.getColor(R.color.black)
            DEFAULT -> context.getColor(R.color.black)
            HIGH -> context.getColor(R.color.red)
        }
    }

    fun onSwipeToDelete(position: Int) {
        controller.processDelete(list[position])
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun onSwipeToComplete(position: Int, flag: Boolean) {
        processCompleteChanged(position, flag)
    }

    override fun onClick(v: View?) {
        controller.processOpenDetails(v?.tag as TodoItem)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val position = buttonView?.tag as Int
        processCompleteChanged(position, isChecked)
    }

    private fun processCompleteChanged(position: Int, isChecked: Boolean) {
        list[position].done = isChecked
        notifyItemChanged(position)
        controller.processCompleteChanged(list[position])
    }
}