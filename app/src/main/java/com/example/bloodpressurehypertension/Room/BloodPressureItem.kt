package com.example.bloodpressurehypertension.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blood_pressure_db")
data class BloodPressureItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val date:String,
    val days:String,
    val month:String,
    val years:String,
    val hours:String,
    val minute:String,
    val pulse:Int,
    val upperValue:Int,
    val lowerValue:Int,
    val healthy:List<String>,
    val unhealthy:List<String>,
    val symptoms:List<String>,
    val care:List<String>,
    val other:List<String>
)