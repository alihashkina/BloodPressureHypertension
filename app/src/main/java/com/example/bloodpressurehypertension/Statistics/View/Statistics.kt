package com.example.bloodpressurehypertension.Statistics.View

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodpressurehypertension.R
import com.example.bloodpressurehypertension.Statistics.Adapter.BloodPressureAdapter
import com.example.bloodpressurehypertension.Statistics.ViewModel.StatisticsViewModel
import com.example.bloodpressurehypertension.databinding.StatisticsFragmentBinding
import kotlinx.coroutines.launch

class Statistics : Fragment() {

    companion object {
        fun newInstance() = Statistics()
    }

    val viewModel: StatisticsViewModel by viewModels()
    lateinit var bindingStatistics: StatisticsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingStatistics = DataBindingUtil.inflate(inflater, R.layout.statistics_fragment, container, false)
        return bindingStatistics.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindingStatistics.apply {
            recyclerStatistics.layoutManager = LinearLayoutManager(requireContext())
            val adapter = BloodPressureAdapter(
                delete = {
                    viewModel.deleteBloodPressure(it)
                    Toast.makeText(requireContext(), requireContext().getString(R.string.toastDelete), Toast.LENGTH_SHORT).show()
                }
            )
            recyclerStatistics.adapter = adapter
            lifecycleScope.launch {
                viewModel.bloodPressureItems.collect {
                    adapter.submitList(it)

                }
            }
        }
    }
    }

