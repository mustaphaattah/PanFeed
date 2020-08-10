package com.mtah.panfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mtah.panfeed.R
import com.mtah.panfeed.models.Country
import kotlinx.android.synthetic.main.case_card.view.*
import java.util.*

class CasesAdapter(var cases: List<Country>) : RecyclerView.Adapter<CasesAdapter.CaseViewHolder>(), Filterable {
    var fullCasesList = listOf(cases)
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


        fun bind(countryCase: Country) {

            caseName.text = countryCase.country
            confirmedNumber.text = countryCase.cases
            recoveredNumber.text = countryCase.recovered
            deathsNumber.text = countryCase.deaths
        }

    }


    override fun getFilter(): Filter {
        TODO("Not yet implemented")
        return filter
    }

}