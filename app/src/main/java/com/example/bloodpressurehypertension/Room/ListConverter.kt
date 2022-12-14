package com.example.bloodpressurehypertension.Room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {

    @TypeConverter
    fun fromList(list:List<String>) : String{
        val gson = Gson()
        val json = gson.toJson(list)
        return json
    }

    @TypeConverter
    fun toList(value:String) : List<String>{
        val listType = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
}