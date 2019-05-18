package com.cilenco.liveadapter.sample.ui

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel

import com.cilenco.liveadapter.sample.models.Book

class MainViewModel: ViewModel() {
    val books = ObservableArrayList<Book>()

    init {
        books.addAll(Book.SAMPLE_DATA)
    }
}