package com.example.midassignment.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.midassignment.data.model.Item
import com.example.midassignment.data.repository.ItemRepository

class ItemsViewModel(application: Application) : AndroidViewModel(application){

    private val repository = ItemRepository(application)

    val items : LiveData<List<Item>>? = repository.getItems()

    private val _chosenItem = MutableLiveData<Item>()

    val chosenItem : LiveData<Item> get() = _chosenItem

    fun setItem(item : Item){
        _chosenItem.value = item
    }

    fun updateItem(item:Item){
        repository.updateItem(item)
    }

    fun addItem(item: Item){
        repository.addItem(item)
    }

    fun deleteItem(item: Item){
        repository.deleteItem(item)
    }

    fun deleteAll(){
        repository.deleteAll()
    }
}