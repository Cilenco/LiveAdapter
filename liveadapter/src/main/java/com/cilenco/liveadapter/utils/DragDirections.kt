package com.cilenco.liveadapter.utils

import androidx.recyclerview.widget.ItemTouchHelper

object DragDirections {
    val ALL = { _: Int -> ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT }
    val LEFT_RIGHT = { _: Int -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT }
    val UP_DOWN = {_: Int -> ItemTouchHelper.UP or ItemTouchHelper.DOWN }

    val NONE = { _: Int -> 0 }
}