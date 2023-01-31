package com.amin.foodparty.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tableFood")
data class FoodDC(

    @PrimaryKey(autoGenerate = true)
    val id:Int?=null ,

    val title : String ,
    val price : String ,
    val distance : String ,
    val place : String ,

    @ColumnInfo(name = "URL")
    val imageUrl : String ,
    val numOfRating : Int ,
    val rating : Float
)
