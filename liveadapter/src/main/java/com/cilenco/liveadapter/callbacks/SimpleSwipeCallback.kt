package com.cilenco.liveadapter.callbacks

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cilenco.liveadapter.adapters.LiveAdapter
import com.cilenco.liveadapter.utils.SwipeDirections

class SimpleSwipeCallback<T:Any>(private val adapter: LiveAdapter<T>): ItemTouchHelper.SimpleCallback(0, 0), SwipeAdapter<T> {
    override var swipeDirections = SwipeDirections.NONE
    override var dragDirections = SwipeDirections.NONE

    override var onSwipeCallback = { _:Int, _: T, _: Int -> }
    override var onDragCallback = { _:Int, _: Int, _: Boolean -> true }

    override var swipeColorLeft = Color.TRANSPARENT
    override var swipeColorRight = Color.TRANSPARENT

    override var swipeDrawableLeft: Drawable? = null
    override var swipeDrawableRight: Drawable? = null

    override var swipeMargin = 20 //Integer.MAX_VALUE

    private val bgPaint by lazy { Paint() }

    private var dragFrom = RecyclerView.NO_POSITION
    private var dragTo = RecyclerView.NO_POSITION

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        val pos = viewHolder.adapterPosition
        return swipeDirections(pos)
    }

    override fun getDragDirs(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        val pos = viewHolder.adapterPosition
        return dragDirections(pos)
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        viewHolder.itemView.translationX = 0f
        viewHolder.itemView.translationY = 0f

        val position = viewHolder.adapterPosition

        if (position != RecyclerView.NO_POSITION) {
            val item = adapter.getItem(position)
            onSwipeCallback(position, item, direction)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
        if (dragFrom == RecyclerView.NO_POSITION) {
            dragFrom = viewHolder.adapterPosition
        }

        dragTo = target.adapterPosition

        val oldPosition = viewHolder.adapterPosition
        val newPosition = viewHolder.adapterPosition

        return onDragCallback(oldPosition, newPosition, false)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (dragFrom != RecyclerView.NO_POSITION && dragTo != RecyclerView.NO_POSITION) {
            onDragCallback(dragFrom, dragTo, true)
        }

        dragFrom = RecyclerView.NO_POSITION // reset the dragFrom positions
        dragTo = RecyclerView.NO_POSITION // reset the dragTo positions
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (viewHolder.adapterPosition == RecyclerView.NO_POSITION) return

        val itemView = viewHolder.itemView

        if (Math.abs(dX) > Math.abs(dY)) {
            val isLeft = dX < 0

            bgPaint.color = if (isLeft) swipeColorLeft else swipeColorRight
            val drawable = if (isLeft) swipeDrawableLeft else swipeDrawableRight

            if (bgPaint.color != Color.TRANSPARENT) {
                val left = if (isLeft) itemView.right + dX.toInt() else itemView.left
                val right = if (isLeft) itemView.right else itemView.left + dX.toInt()

                c.drawRect(left.toFloat(), itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat(), bgPaint)
            }

            if (drawable != null) {
                val itemHeight = itemView.bottom - itemView.top

                val intrinsicWidth = drawable.intrinsicWidth
                val intrinsicHeight = drawable.intrinsicWidth

                val left: Int
                val right: Int

                if (isLeft) {
                    left = itemView.right - swipeMargin - intrinsicWidth
                    right = itemView.right - swipeMargin
                } else {
                    left = itemView.left + swipeMargin
                    right = itemView.left + swipeMargin + intrinsicWidth
                }

                val top = itemView.top + (itemHeight - intrinsicHeight) / 2
                val bottom = top + intrinsicHeight

                drawable.setBounds(left, top, right, bottom)
                drawable.draw(c)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}