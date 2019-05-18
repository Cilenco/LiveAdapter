package com.cilenco.liveadapter.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.cilenco.sample.R

class LiveRecyclerView : RecyclerView {
    private var placeholderView: View? = null
    private var observer = EmptyObserver()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LiveRecyclerView, 0, 0).apply {
            try {
                val layoutRes = getInteger(R.styleable.LiveRecyclerView_placeholder, -1)
                if(layoutRes != -1) setPlaceholder(layoutRes)
            } finally {
                recycle()
            }
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)

        adapter?.registerAdapterDataObserver(observer)
        observer.onChanged() // Trigger change manually
    }

    fun setPlaceholder(placeholderView: View) {
        val parentView = parent as ViewGroup
        parentView.addView(placeholderView)
    }

    fun setPlaceholder(@LayoutRes layoutRes: Int) {
        val parentView = parent as ViewGroup

        val inflater = LayoutInflater.from(context)
        placeholderView = inflater.inflate(layoutRes, parentView, false)

        parentView.addView(placeholderView)
    }

    private inner class EmptyObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            toggleVisibility()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            toggleVisibility()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            toggleVisibility()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            toggleVisibility()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            toggleVisibility()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            toggleVisibility()
        }

        private fun toggleVisibility() {
            val adapter: Adapter<*>? = adapter

            if(placeholderView == null || adapter == null) return

            if (adapter.itemCount == 0) {
                placeholderView!!.visibility = View.VISIBLE
                this@LiveRecyclerView.visibility = View.GONE
            } else {
                placeholderView!!.visibility = View.GONE
                this@LiveRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}