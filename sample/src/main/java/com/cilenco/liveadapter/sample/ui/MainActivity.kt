package com.cilenco.liveadapter.sample.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat

import com.cilenco.liveadapter.sample.BR
import com.cilenco.liveadapter.sample.R

import com.cilenco.liveadapter.adapters.LiveAdapter
import com.cilenco.liveadapter.utils.SwipeDirections
import com.cilenco.liveadapter.sample.models.Book
import com.cilenco.liveadapter.utils.SortOrder

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private val adapter by lazy { LiveAdapter(viewModel.books) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.setup(R.layout.item_two_line, BR.item)

        adapter.swipeDirections = SwipeDirections.ALL
        //adapter.dragDirections = DragDirections.NONE

        adapter.swipeColorLeft = Color.GREEN
        adapter.swipeColorRight = Color.RED

        adapter.swipeDrawableLeft = ContextCompat.getDrawable(this, R.drawable.star)
        adapter.swipeDrawableRight = ContextCompat.getDrawable(this, R.drawable.delete)

        adapter.sortedBy { it.title }

        adapter.onClick(::onItemClicked)
        adapter.onLongClick(::onItemLongClicked)
        adapter.onSwipeCallback = ::onItemSwiped

        list.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu!!)

        val orderItem = menu.findItem(R.id.action_order)
        orderItem.setOnMenuItemClickListener(::onOrderClicked)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                adapter.filter { it.title.toLowerCase().startsWith(query.toLowerCase()) }
                return false
            }
        })

        return true
    }

    private fun onOrderClicked(item: MenuItem): Boolean {
        val newOrder = if (adapter.sortOrder == SortOrder.ASC) SortOrder.DSC else SortOrder.ASC

        adapter.sortOrder = newOrder
        return true
    }

    private fun onItemClicked(book: Book, position: Int) {
        Toast.makeText(this, "Clicked: ${book.title}", Toast.LENGTH_LONG).show()
    }

    private fun onItemLongClicked(book: Book, position: Int): Boolean {
        Toast.makeText(this, "Long clicked: ${book.title}", Toast.LENGTH_LONG).show()
        return true
    }

    private fun onItemSwiped(position: Int, item: Book, direction: Int) {
        viewModel.books.remove(item)
    }
}
