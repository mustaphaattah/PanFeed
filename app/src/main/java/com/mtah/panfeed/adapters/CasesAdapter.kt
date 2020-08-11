package com.mtah.panfeed.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.mtah.panfeed.R
import com.mtah.panfeed.models.Country
import kotlinx.android.synthetic.main.case_card.view.*
import java.util.*

class CasesAdapter(var cases: List<Country>, val context: Context?) : RecyclerView.Adapter<CasesAdapter.CaseViewHolder>() {
    //var fullCasesList = listOf(cases)
    init {
        World.init(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.case_card, parent, false)
        return CaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cases.size
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.bind(cases[position])
    }

    class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val caseName: TextView = itemView.findViewById(R.id.countryName)
        private val confirmedNumber: TextView = itemView.findViewById(R.id.confirmedNumber)
        private val recoveredNumber: TextView = itemView.findViewById(R.id.recoveredNumber)
        private val deathsNumber: TextView = itemView.findViewById(R.id.deathNumber)
        private val countryFlag: ImageView = itemView.findViewById(R.id.flagImageView)

        fun bind(countryCase: Country) {
            if (!countryCase.country.isNullOrBlank()) {
                val countryName = countryCase.country
                caseName.text = countryName

                countryFlag.setImageResource(World.getFlagOf(countryName))
                if (countryName == "World") countryFlag.setImageResource(R.drawable.world_img)

                confirmedNumber.text = if (countryCase.cases.isNotBlank()) countryCase.cases else "N/A"
                recoveredNumber.text = if (countryCase.recovered.isNotBlank()) countryCase.recovered else "N/A"
                deathsNumber.text = if (countryCase.deaths.isNotBlank()) countryCase.deaths else "N/A"
            }
        }
    }




}