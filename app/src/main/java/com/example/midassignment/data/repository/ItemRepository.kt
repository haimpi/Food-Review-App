package com.example.midassignment.data.repository

import android.app.Application
import com.example.midassignment.data.local_db.ItemDao
import com.example.midassignment.data.local_db.ItemDataBase
import com.example.midassignment.data.model.Item

class ItemRepository(application: Application){

    private var itemDao:ItemDao?

    init {
        val db = ItemDataBase.getDatabase(application.applicationContext)
        itemDao = db?.itemsDao()
    }

    fun updateItem(item:Item){
        itemDao?.updateItem(item)
    }

    fun getItems() = itemDao?.getItems()

    fun addItem(item: Item){
        itemDao?.addItem(item)
    }

    fun deleteItem(item: Item){
        itemDao?.deleteItem(item)
    }

    fun deleteAll(){
        itemDao?.deleteAll()
    }

    fun getItem(id:Int) = itemDao?.getItem(id)
}