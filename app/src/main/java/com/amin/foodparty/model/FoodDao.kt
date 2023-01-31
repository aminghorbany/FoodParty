package com.amin.foodparty.model

import androidx.room.*
import com.amin.foodparty.model.FoodDC

@Dao
interface FoodDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertOrUpdate(foodDC: FoodDC)

    @Insert
    fun insertFood(foodDC: FoodDC)

    @Insert
    fun insertAllFoods(list: List<FoodDC>)

    @Update
    fun updateFood(foodDC: FoodDC)

    @Delete
    fun deleteFood(foodDC: FoodDC)

    @Query("select * from tableFood order by id desc")
    fun getAllFoods():List<FoodDC>

    @Query("delete from tableFood")
    fun deleteAllFoods()

    @Query("select * from tableFood where title like '%' || :searchChar || '%' ")
    fun searchFood(searchChar:String):List<FoodDC>
}