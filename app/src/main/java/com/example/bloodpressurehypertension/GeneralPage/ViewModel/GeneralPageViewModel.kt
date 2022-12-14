package com.example.bloodpressurehypertension.GeneralPage.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloodpressure.fragment.GeneralPage
import com.example.bloodpressurehypertension.R
import com.example.bloodpressurehypertension.Room.BloodPressureDatabase
import com.example.bloodpressurehypertension.Room.BloodPressureItem
import kotlinx.coroutines.launch

class GeneralPageViewModel(application: Application) : AndroidViewModel(application) {

    private val bloodPressureDao = BloodPressureDatabase.getDatabase(application.applicationContext).bloodPressureDao()
    val bloodPressureItems = bloodPressureDao.fetchAllBloodPressure()

    fun saveBloodPressure(bloodPressureItem: BloodPressureItem){
        viewModelScope.launch {
            bloodPressureDao.insertBloodPressure(bloodPressureItem)
        }
    }

    fun getHealthyList(context: Context):List<String>{
        return  listOf(
            context.resources.getString(R.string.breathingExercises),
            context.resources.getString(R.string.cardioWorkout),
            context.resources.getString(R.string.dairy),
            context.resources.getString(R.string.diet),
            context.resources.getString(R.string.gardening),
            context.resources.getString(R.string.goodSleep),
            context.resources.getString(R.string.highFiberFood),
            context.resources.getString(R.string.housework),
            context.resources.getString(R.string.lowSaltDiet),
            context.resources.getString(R.string.seafood),
            context.resources.getString(R.string.sex),
            context.resources.getString(R.string.sport),
            context.resources.getString(R.string.stretching),
            context.resources.getString(R.string.vegetables),
            context.resources.getString(R.string.walking),
            context.resources.getString(R.string.yoga),
            context.resources.getString(R.string.otherPhysicalActivity)

        )
    }
    fun getUnhealthyList(context: Context):List<String>{
        return  listOf(
            context.resources.getString(R.string.alcohol),
            context.resources.getString(R.string.coffee),
            context.resources.getString(R.string.energeticDrinks),
            context.resources.getString(R.string.fastfood),
            context.resources.getString(R.string.fatFood),
            context.resources.getString(R.string.irregularEating),
            context.resources.getString(R.string.irregularSports),
            context.resources.getString(R.string.lateDinner),
            context.resources.getString(R.string.longSitting),
            context.resources.getString(R.string.noActivity),
            context.resources.getString(R.string.overeating),
            context.resources.getString(R.string.overpressure),
            context.resources.getString(R.string.pastry),
            context.resources.getString(R.string.processedFood),
            context.resources.getString(R.string.salt),
            context.resources.getString(R.string.smoking),
            context.resources.getString(R.string.soda),
            context.resources.getString(R.string.stress),
            context.resources.getString(R.string.sugar),
            context.resources.getString(R.string.sweets)

        )
    }
    fun getSymptomsList(context: Context):List<String>{
        return  listOf(
            context.resources.getString(R.string.confused),
            context.resources.getString(R.string.coordinationProblems),
            context.resources.getString(R.string.crankyOrImpatient),
            context.resources.getString(R.string.decreasedVision),
            context.resources.getString(R.string.dizzy),
            context.resources.getString(R.string.dryMouth),
            context.resources.getString(R.string.drySkin),
            context.resources.getString(R.string.dyspnea),
            context.resources.getString(R.string.energetic),
            context.resources.getString(R.string.fastHeartbeat),
            context.resources.getString(R.string.fatigue),
            context.resources.getString(R.string.feelWell),
            context.resources.getString(R.string.goodMood),
            context.resources.getString(R.string.happy),
            context.resources.getString(R.string.headache),
            context.resources.getString(R.string.healSlowly),
            context.resources.getString(R.string.hunger),
            context.resources.getString(R.string.itchySkin),
            context.resources.getString(R.string.loseWeightWithoutTrying),
            context.resources.getString(R.string.nausea),
            context.resources.getString(R.string.nervous),
            context.resources.getString(R.string.nightmares),
            context.resources.getString(R.string.numbOrTinglingHandsOrFeet),
            context.resources.getString(R.string.painInChest),
            context.resources.getString(R.string.paleSkin),
            context.resources.getString(R.string.shaky),
            context.resources.getString(R.string.sleepy),
            context.resources.getString(R.string.sweaty),
            context.resources.getString(R.string.thirsty),
            context.resources.getString(R.string.urinateALot),
            context.resources.getString(R.string.weak),
            context.resources.getString(R.string.otherSymptoms)
        )
    }
    fun getCareList(context: Context):List<String>{
        return  listOf(
            context.resources.getString(R.string.aceInhibitors),
            context.resources.getString(R.string.alphaBlockers),
            context.resources.getString(R.string.alpha2ReceptorAgonists),
            context.resources.getString(R.string.angiotensin2ReceptorBlockers),
            context.resources.getString(R.string.betaBlockers),
            context.resources.getString(R.string.calciumChannelBlockers),
            context.resources.getString(R.string.centralAgonists),
            context.resources.getString(R.string.combinedAlphaAndBetablockers),
            context.resources.getString(R.string.diuretics),
            context.resources.getString(R.string.peripheralAdrenergicInhibitors),
            context.resources.getString(R.string.vasodilators),
            context.resources.getString(R.string.otherDrugs)
        )
    }
}