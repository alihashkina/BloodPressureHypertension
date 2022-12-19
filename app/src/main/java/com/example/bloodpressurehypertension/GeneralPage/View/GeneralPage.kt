package com.example.bloodpressure.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bloodpressurehypertension.GeneralPage.ViewModel.GeneralPageViewModel
import com.example.bloodpressurehypertension.R
import com.example.bloodpressurehypertension.Room.BloodPressureItem
import com.example.bloodpressurehypertension.TinyDB.TinyDB
import com.example.bloodpressurehypertension.databinding.GeneralPageFragmentBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartSymbolType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GeneralPage : Fragment() {

    companion object{
        fun newInstance() = GeneralPage()
    }

    lateinit var tinyDB: TinyDB
    lateinit var bindingGeneralPage: GeneralPageFragmentBinding
    val viewModel: GeneralPageViewModel by viewModels()
    lateinit var timePicker: TimePickerDialog
    lateinit var datePicker: DatePickerDialog
    var calendar: Calendar = Calendar.getInstance()
    var chipsOtherCheck = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingGeneralPage = DataBindingUtil.inflate(inflater, R.layout.general_page_fragment,container,false)
        return bindingGeneralPage.root
    }

    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindingGeneralPage.apply {

            tinyDB = TinyDB(requireContext())

            scrollGraph.post {
                scrollGraph.fullScroll(View.FOCUS_RIGHT)
            }

            //наблюдатель обновление графика
            lifecycleScope.launch {
                viewModel.bloodPressureItems.collect {
                    graph(it, scrollGraph, txtOnbord)
                }
            }

            createChipGroup(
                type = TypeChips.Healthy(),
                chipGroup = chipGroupHealthy,
                list = viewModel.getHealthyList(requireContext())
            )
            createChipGroup(
                type = TypeChips.Unhealthy(),
                chipGroup = chipGroupUnhealthy,
                list = viewModel.getUnhealthyList(requireContext())
            )
            createChipGroup(
                type = TypeChips.Symptoms(),
                chipGroup = chipGroupSymptoms,
                list = viewModel.getSymptomsList(requireContext())
            )
            createChipGroup(
                type = TypeChips.Care(),
                chipGroup = chipGroupCare,
                list = viewModel.getCareList(requireContext())
            )
            createChipGroup(
                type = TypeChips.Other(),
                chipGroup = chipGroupOtherTags,
                list = tinyDB.getListString("OtherChips")
            )

            txtRecord.setOnClickListener {
                datePicker.show()
            }

            //получаем сохраненный пульс
            if(tinyDB.getInt("nPickerPulse") == 0) {
                nPickerPulse.value = 60
            }else{
                nPickerPulse.value = tinyDB.getInt("nPickerPulse")
            }

            nPickerPulse.setOnValueChangedListener { picker, oldVal, newVal ->
                tinyDB.putInt("nPickerPulse", nPickerPulse.value)
            }

            //получаем сохраненное давление
            if(tinyDB.getInt("nPickUpper") == 0) {
                nPickUpper.value = 120
            }else{
                nPickUpper.value = tinyDB.getInt("nPickUpper")
            }

            nPickUpper.setOnValueChangedListener { picker, oldVal, newVal ->
                tinyDB.putInt("nPickUpper", nPickUpper.value)
            }

            if(tinyDB.getInt("nPickLower") == 0){
                nPickLower.value = 80
            }else{
                nPickLower.value = tinyDB.getInt("nPickLower")
            }

            nPickLower.setOnValueChangedListener { picker, oldVal, newVal ->
                tinyDB.putInt("nPickLower", nPickLower.value)
            }

            btnSave.setOnClickListener {

                //добавляем сохраненные данные
                chipsOtherCheck = tinyDB.getListString("OtherChips")

                val healthySelectedItems: List<String> = getSelectedItems(chipGroupHealthy)
                val unhealthySelectedItems: List<String> = getSelectedItems(chipGroupUnhealthy)
                val symptomsSelectedItems: List<String> = getSelectedItems(chipGroupSymptoms)
                val careSelectedItems: List<String> = getSelectedItems(chipGroupCare)
                val otherSelectedItems: List<String> = getSelectedItems(chipGroupOtherTags)

                val bloodPressureItem = BloodPressureItem(
                    id = 0,
                    date = getCurrentDateAndTime(),
                    days = getCurrentDateAndTime().split(".")?.get(0),
                    month = getCurrentDateAndTime().split(".")?.get(1),
                    years = getCurrentDateAndTime().split(".")?.get(2).dropLast(5).replace(" ", ""),
                    hours = getCurrentDateAndTime().split(" ")?.get(1).dropLast(2).replace(":", ""),
                    minute = getCurrentDateAndTime().split(":")?.get(1),
                    pulse = nPickerPulse.value,
                    upperValue = nPickUpper.value,
                    lowerValue = nPickLower.value,
                    healthy = healthySelectedItems,
                    unhealthy = unhealthySelectedItems,
                    symptoms = symptomsSelectedItems,
                    care = careSelectedItems,
                    other = otherSelectedItems
                )
                viewModel.saveBloodPressure(bloodPressureItem)

                chipGroupHealthy.clearCheck()
                chipGroupUnhealthy.clearCheck()
                chipGroupSymptoms.clearCheck()
                chipGroupCare.clearCheck()

                Toast.makeText(requireContext(), requireContext().getString(R.string.toastSave), Toast.LENGTH_SHORT).show()
                scrollGeneral.post {
                    scrollGeneral.fullScroll(View.FOCUS_UP)
                }
            }

            //фокус при пустой строке/скрытие клавы
            btnOtherTags.setOnClickListener {
                val inputMethodManager =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (editTxtOtherTags.text.isNotEmpty()) {
                    val chip = layoutInflater.inflate(
                        R.layout.chip,
                        chipGroupOtherTags,
                        false
                    ) as Chip
                    chip.isCheckable = true
                    chip.chipBackgroundColor = resources.getColorStateList(R.drawable.chip_state_other)
                    chip.text = editTxtOtherTags.text.toString()
                    chip.isCloseIconVisible = true
                    chip.setOnCloseIconClickListener{
                        chipGroupOtherTags.removeView(chip)
                        chipsOtherCheck.remove(chip?.text)
                        tinyDB.putListString("OtherChips", chipsOtherCheck)
                    }
                    chipsOtherCheck.add("${chip?.text}")
                    chip.isChecked = true
                    chipGroupOtherTags.addView(chip)
                    tinyDB.putListString("OtherChips", chipsOtherCheck)
                    editTxtOtherTags.setText("")
                    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                } else {
                    editTxtOtherTags.requestFocus()
                }
            }

            //кнопка на клавиатуре = добавить
            editTxtOtherTags.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE && editTxtOtherTags.text.toString() != "") {
                    btnOtherTags.callOnClick()
                    handled = true
                }
                handled
            })
            pickDate()
        }
    }

    //дейт/тайм пикеры
    private fun pickDate(){
        val timeListener =
            TimePickerDialog.OnTimeSetListener { view: TimePicker?, hour: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                updateDateAndTime()
            }
        val dateListener =
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, day: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                timePicker.show()
            }
        timePicker = TimePickerDialog(
            requireContext(),
            timeListener,
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            true
        )
        datePicker = DatePickerDialog(
            requireContext(),
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        bindingGeneralPage.txtRecord.text = "${requireContext().getString(R.string.record)} ${getCurrentDateAndTime()}"
    }

    fun updateDateAndTime() {
        bindingGeneralPage.txtRecord.text = "${requireContext().getString(R.string.record)} ${getCurrentDateAndTime()}"
    }

    fun getCurrentDateAndTime(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val currentDateAndTime = sdf.format(calendar.time)
        return currentDateAndTime
    }

    fun graph(list:List<BloodPressureItem>, scrollGraph: HorizontalScrollView, txtOnbord: LinearLayout){
        if (list.isNotEmpty()) {
            txtOnbord.visibility = View.GONE
            scrollGraph.visibility = View.VISIBLE

            val aaChartModel : AAChartModel = AAChartModel()
                .chartType(AAChartType.Spline)
                .backgroundColor("#FFFFFF")
                .dataLabelsEnabled(true)
                .legendEnabled(true)
                .animationDuration(10)
                .yAxisVisible(true)
                .yAxisLabelsEnabled(false)
                .yAxisTitle("")
                .markerSymbol(AAChartSymbolType.Circle)
                .colorsTheme(arrayOf("#29B6FC", "#29B6FC", "#EF5350"))
                .categories(list.map {
                    it.date
                }.toTypedArray())
                .series(arrayOf(
                    AASeriesElement()
                        .name(this.getString(R.string.upper))
                        .data(list.map {
                            it.upperValue
                        }.toTypedArray()),
                    AASeriesElement()
                        .name(this.getString(R.string.lower))
                        .data(list.map {
                            it.lowerValue
                        }.toTypedArray()),
                    AASeriesElement()
                        .name(this.getString(R.string.pulse))
                        .data(list.map {
                            it.pulse
                        }.toTypedArray())
                )
                )
            bindingGeneralPage.aaChartView.aa_drawChartWithChartModel(aaChartModel)

            if(list.map {
                    it.date
                }.size < 3){
                bindingGeneralPage.aaChartView.layoutParams.width = 1000
            } else{
                bindingGeneralPage.aaChartView.layoutParams.width = bindingGeneralPage.linearGraph.layoutParams.width
            }

            //фокус графика в конец
            scrollGraph.post {
                scrollGraph.fullScroll(View.FOCUS_RIGHT)
            }
        }else{
            txtOnbord.visibility = View.VISIBLE
            scrollGraph.visibility = View.GONE
        }
    }

    @SuppressLint("ResourceType")
    fun createChipGroup(type: TypeChips, chipGroup: ChipGroup, list: List<String>) {
        val chipBg = when (type) {
            is TypeChips.Care -> resources.getColorStateList(R.drawable.chip_state_care)
            is TypeChips.Healthy -> resources.getColorStateList(R.drawable.chip_state_healthy)
            is TypeChips.Symptoms -> resources.getColorStateList(R.drawable.chip_state_symptoms)
            is TypeChips.Unhealthy -> resources.getColorStateList(R.drawable.chip_state_unhealthy)
            is TypeChips.Other -> resources.getColorStateList(R.drawable.chip_state_other)
        }
        list.forEach {
            val chip = layoutInflater.inflate(
                R.layout.chip,
                chipGroup,
                false
            ) as Chip
            chip.chipBackgroundColor = chipBg
            chip.isCheckable = true
            chip.text = it
            chipGroup.addView(chip)
            if (chipBg == resources.getColorStateList(R.drawable.chip_state_other)){
                chip.isCloseIconVisible = true
            }
        }
    }

    fun getSelectedItems(chipGroup: ChipGroup): List<String> {
        return chipGroup.children.filter {
            (it as Chip).isChecked
        }.map {
            return@map (it as Chip).text.toString()
        }.toList()
    }
}

sealed class TypeChips() {
    class Healthy() : TypeChips()
    class Unhealthy() : TypeChips()
    class Symptoms() : TypeChips()
    class Care() : TypeChips()
    class Other() : TypeChips()
}