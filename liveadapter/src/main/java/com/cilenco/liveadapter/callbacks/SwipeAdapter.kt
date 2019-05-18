package com.cilenco.liveadapter.callbacks

import android.graphics.drawable.Drawable

interface SwipeAdapter<T> {

    var swipeDirections: (position: Int) -> Int
    var dragDirections: (position: Int) -> Int

    var onSwipeCallback: (position: Int, item: T, direction: Int) -> Unit
    var onDragCallback: (oldPosition: Int, newPosition: Int, dropped: Boolean) -> Boolean

    var swipeColorLeft: Int
    var swipeColorRight: Int

    var swipeDrawableLeft: Drawable?
    var swipeDrawableRight: Drawable?

    var swipeMargin: Int
}