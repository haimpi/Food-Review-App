package com.example.midassignment.data.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @ColumnInfo(name = "content_foodName")
    val foodName:String,

    @ColumnInfo(name = "content_restaurantName")
    val restaurantName:String,

    @ColumnInfo(name = "content_description")
    val description:String,

    @ColumnInfo(name = "food_rating")
    val ratingRate: Float,

    @ColumnInfo(name = "image")
    val photo: String?)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}