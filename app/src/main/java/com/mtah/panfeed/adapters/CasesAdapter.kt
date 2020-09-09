package com.mtah.panfeed.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.mtah.panfeed.R
import com.mtah.panfeed.models.Country

class CasesAdapter(var cases: MutableList<Country>, val context: Context?) : RecyclerView.Adapter<CasesAdapter.CaseViewHolder>(), Filterable {
    val TAG = "CasesAdapter"
    var fullCasesList = cases

    init {
        World.init(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_case, parent, false)
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
            val countryName = countryCase.country
            caseName.text = countryName

            countryFlag.setImageResource(World.getFlagOf(countryName))
            if (countryName == "World") countryFlag.setImageResource(R.drawable.world_img)

            confirmedNumber.text = if (countryCase.cases.isNotBlank()) countryCase.cases else "N/A"
            recoveredNumber.text = if (countryCase.recovered.isNotBlank()) countryCase.recovered else "N/A"
            deathsNumber.text = if (countryCase.deaths.isNotBlank()) countryCase.deaths else "N/A"
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    fun setCaseList(casesList: MutableList<Country>) {
        fullCasesList = casesList
        notifyDataSetChanged()
    }

    private var filter  = object : Filter() {
        val filteredList = mutableListOf<Country>()

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            Log.i(TAG, "Query: $constraint")
            filteredList.clear()
            if (constraint.isNullOrBlank()) {
                filteredList.addAll(fullCasesList)
            }else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                filteredList.addAll(fullCasesList.filter {
                    it.country.toLowerCase().contains(filterPattern)
                })
            }

            val results = FilterResults()
            results.values =  filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            cases = if(results?.values == null) {
                arrayListOf()
            } else results.values as MutableList<Country>
            notifyDataSetChanged()
//            Toast.makeText(context, "No resutls", Toast.LENGTH_SHORT).show()
        }

    }

}