package com.cilenco.liveadapter.utils

import androidx.recyclerview.widget.ItemTouchHelper

object SwipeDirections {
    val ALL = { _: Int -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT }
    val RIGHT = { _: Int -> ItemTouchHelper.RIGHT }
    val LEFT = { _: Int -> ItemTouchHelper.LEFT }

    val NONE = { _: Int -> 0 }
}