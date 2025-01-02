package com.example.midassignment.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.midassignment.data.model.Item

@Dao
interface ItemDao {

    @Insert()
    fun addItem(item: Item)

    @Delete
    fun deleteItem(vararg items: Item)

    @Update
    fun updateItem(item: Item)

    @Query("SELECT * FROM items ORDER BY content_foodName ASC")
    fun getItems() : LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE id LIKE:id")
    fun getItem(id: Int) : Item

    @Query("DELETE FROM items")
    fun deleteAll()
}