package com.r42914lg.arkados.yatodo.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.getColorFromAttr
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.*
import com.r42914lg.arkados.yatodo.ui.presenter.FirstFragmentPresenter


class WorkItemAdapter(
    private var list: MutableList<TodoItem>,
    private val _context: Context,
    private val controller: FirstFragmentPresenter,
    private val recyclerView: RecyclerView,
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
        val iconImageView: ImageView = itemView.findViewById(R.id.infoIcon)
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
        holder.importanceTextView.text = getImportancePrefixText(
            list[position].importance,
            holder.doneCheckBox.isChecked)

        if (list[position].done)
            holder.todoText.paintFlags = holder.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            holder.todoText.paintFlags = Paint.ANTI_ALIAS_FLAG

        val color = getImportancePrefixColor(list[position].importance)
        holder.importanceTextView.setTextColor(color)
        holder.doneCheckBox.buttonTintList =
            if (holder.doneCheckBox.isChecked)
                ColorStateList.valueOf(context.getColorFromAttr(R.attr.markCompleteColor))
            else
                ColorStateList.valueOf(context.getColorFromAttr(androidx.appcompat.R.attr.colorControlNormal))

        holder.itemView.tag = list[position]
        holder.itemView.setOnClickListener(this)

        holder.iconImageView.imageTintList = ColorStateList.valueOf(
            context.getColorFromAttr(androidx.appcompat.R.attr.colorControlNormal))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getImportancePrefixText(importance: Importance, markedComplete: Boolean) : String {
        return when (importance) {
            LOW -> context.getString(R.string.importance_prefix_low)
            DEFAULT -> context.getString(R.string.importance_prefix_default)
            HIGH -> if (markedComplete) "" else context.getString(R.string.importance_prefix_high)
        }
    }

    private fun getImportancePrefixColor(importance: Importance) : Int {
        return when (importance) {
            LOW -> context.getColorFromAttr(com.firebase.ui.auth.R.attr.colorOnPrimary)
            DEFAULT -> context.getColorFromAttr(com.firebase.ui.auth.R.attr.colorOnPrimary)
            HIGH -> context.getColorFromAttr(R.attr.swipeDeleteColor)
        }
    }

    fun onSwipeToDelete(position: Int) {
        val deletedItem = list[position]
        controller.processDelete(deletedItem)
        list.removeAt(position)
        notifyItemRemoved(position)

        Snackbar.make(recyclerView, context.getString(R.string.undo_delete), Snackbar.LENGTH_LONG)
            .setAction(context.getString(R.string.cancel_deletion)) {
                controller.processRestore(deletedItem)
                list.add(position, deletedItem)
                recyclerView.scrollToPosition(position)
                notifyItemInserted(position)
            }
            .show()
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

    fun getData(): List<TodoItem> {
        return list
    }

    fun setData(newItems: MutableList<TodoItem>) {
        list = newItems
    }
}