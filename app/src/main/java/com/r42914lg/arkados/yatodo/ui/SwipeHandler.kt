package com.r42914lg.arkados.yatodo.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.getColorFromAttr

class SwipeHandler(private val adapter: WorkItemAdapter) :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val iconDelete = ContextCompat.getDrawable(adapter.context, R.drawable.ic_delete)
    private val iconComplete = ContextCompat.getDrawable(adapter.context, R.drawable.ic_complete)
    private val backgroundRight = ColorDrawable(adapter.context.getColorFromAttr(R.attr.swipeDeleteColor))
    private val backgroundLeft = ColorDrawable(adapter.context.getColorFromAttr(R.attr.markCompleteColor))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT)
            adapter.onSwipeToDelete(position)
        else if (direction == ItemTouchHelper.RIGHT)
            adapter.onSwipeToComplete(position, true)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if (iconComplete == null ||  iconDelete == null)
            return

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20
        val iconMargin = (itemView.height - iconComplete.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - iconComplete.intrinsicHeight) / 2
        val iconBottom = iconTop + iconComplete.intrinsicHeight

        if (dX > 0) { // Swiping to the right
            val iconRight = itemView.left + iconMargin + iconComplete.intrinsicWidth
            val iconLeft = itemView.left + iconMargin
            iconComplete.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            backgroundLeft.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - iconDelete.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            backgroundRight.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            backgroundLeft.setBounds(0, 0, 0, 0)
            backgroundRight.setBounds(0,0,0,0)
            iconComplete.setBounds(0,0,0,0)
            iconDelete.setBounds(0,0,0,0)
        }

        backgroundLeft.draw(c)
        backgroundRight.draw(c)
        iconComplete.draw(c)
        iconDelete.draw(c)
    }
}