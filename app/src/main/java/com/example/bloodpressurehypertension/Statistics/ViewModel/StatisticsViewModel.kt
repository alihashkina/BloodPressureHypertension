package com.example.bloodpressurehypertension.Statistics.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloodpressurehypertension.Room.BloodPressureDatabase
import com.example.bloodpressurehypertension.Room.BloodPressureItem
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val bloodPressureDao = BloodPressureDatabase.getDatabase(application.applicationContext).bloodPressureDao()

    val bloodPressureItems = bloodPressureDao.fetchAllBloodPressure()

    fun deleteBloodPressure(bloodPressureItem: BloodPressureItem){
        viewModelScope.launch {
            bloodPressureDao.deleteBloodPressure(bloodPressureItem)
        }
    }
}