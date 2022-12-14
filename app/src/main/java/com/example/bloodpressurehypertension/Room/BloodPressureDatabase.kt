package com.example.bloodpressurehypertension.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [BloodPressureItem::class],
    version = 1
)
@TypeConverters(ListConverter::class)
abstract class BloodPressureDatabase : RoomDatabase(){

    companion object {
        @Volatile
        private var INSTANCE: BloodPressureDatabase? = null

        fun getDatabase(context: Context): BloodPressureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    BloodPressureDatabase::class.java,
                    "app_database")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
    abstract fun bloodPressureDao(): BloodPressureDao
}