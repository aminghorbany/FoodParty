package com.amin.foodparty.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FoodDC::class] , exportSchema = false , version = 1)
abstract class MyDatabase:RoomDatabase() {
    abstract val foodDao : FoodDao
    companion object{
        private var database: MyDatabase? = null
        fun getDatabase(context: Context): MyDatabase {
            if (database == null){
                database = Room.databaseBuilder(
                    context.applicationContext ,
                    MyDatabase::class.java ,
                    "myDatabase.db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return database!!
        }
    }
}