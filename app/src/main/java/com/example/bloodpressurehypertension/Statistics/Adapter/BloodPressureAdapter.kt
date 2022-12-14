package com.example.bloodpressurehypertension.Statistics.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodpressurehypertension.R
import com.example.bloodpressurehypertension.Room.BloodPressureItem
import com.example.bloodpressurehypertension.databinding.BloodPressureItemBinding

class BloodPressureAdapter(
    val delete: (bloodPressureItem: BloodPressureItem) -> Unit
) : ListAdapter<BloodPressureItem, BloodPressureAdapter.BloodPressureViewHolder>(DiffCallback){
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BloodPressureItem>() {
            override fun areItemsTheSame(oldItem: BloodPressureItem, newItem: BloodPressureItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BloodPressureItem, newItem: BloodPressureItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodPressureViewHolder {
        return BloodPressureViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.blood_pressure_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BloodPressureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BloodPressureViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val bindingBloodPressureItem = BloodPressureItemBinding.bind(view)

        fun bind(item: BloodPressureItem) = with(bindingBloodPressureItem){
            cardDate.text = item.date
            cardPulse.text = item.pulse.toString()
            cardHealthy.text = item.healthy.toString().replace("[", "").replace("]", "")
            cardUnhealthy.text = item.unhealthy.toString().replace("[", "").replace("]", "")
            cardSymptoms.text = item.symptoms.toString().replace("[", "").replace("]", "")
            cardCare.text = item.care.toString().replace("[", "").replace("]", "")
            cardUpper.text = item.upperValue.toString()
            cardLower.text = item.lowerValue.toString()
            cardOther.text = item.other.toString().replace("[", "").replace("]", "")

            cardMore.setOnClickListener {
                    val popupMenu = PopupMenu(it.context, cardMore)
                    val pressure = it.context.getString(R.string.pressure)
                    val date = it.context.getString(R.string.date)
                    val healthy = it.context.getString(R.string.healthy)
                    val txtUnhealthy = it.context.getString(R.string.txtUnhealthy)
                    val txtSymptoms = it.context.getString(R.string.txtSymptoms)
                    val txtCare = it.context.getString(R.string.txtCare)
                    val txtOtherTags = it.context.getString(R.string.txtOtherTags)

                    popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->

                        when (menuItem.itemId) {
                            R.id.share -> {
                                val intent = Intent()
                                intent.action = Intent.ACTION_SEND
                                intent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "${pressure}: ${item.upperValue} / ${item.lowerValue} \n$date: ${item.date} \n$healthy: ${item.healthy} \n$txtUnhealthy: ${item.unhealthy} \n$txtSymptoms: ${item.symptoms} \n$txtCare: ${item.care} \n$txtOtherTags: ${item.other}"
                                )
                                intent.type = "text/plain"
                                it.context.startActivity(Intent.createChooser(intent, "Share To:"))
                            }
                            R.id.delete -> {
                                delete.invoke(item)
                            }
                        }
                        true
                    })
                    popupMenu.show()
                }
        }
    }


}
