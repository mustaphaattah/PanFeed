package com.mtah.panfeed.fragments.cases

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blongho.country_data.World
import com.mtah.panfeed.MainActivity
import com.mtah.panfeed.R
import com.mtah.panfeed.ReadActivity
import com.mtah.panfeed.adapters.CasesAdapter
import com.mtah.panfeed.api.CasesInterface
import com.mtah.panfeed.api.Covid19ApiClient
import com.mtah.panfeed.models.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CasesFragment : Fragment(), CasesAdapter.OnCaseClickListener {

    private val CASES_URL = "https://www.worldometers.info/coronavirus/country/"
    private val TAG = "CasesFragment"

    private lateinit var casesAdapter: CasesAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var counrtySearchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cases, container, false)

        recyclerView = view.findViewById(R.id.casesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        casesAdapter = CasesAdapter(mutableListOf(), context, this)
        recyclerView.adapter = casesAdapter

        swipeRefresh = view.findViewById(R.id.casesRefresh)
        swipeRefresh.setOnRefreshListener { fetchAllCases() }

        World.init(context)


        counrtySearchView = view.findViewById(R.id.countrySearch)
        counrtySearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                casesAdapter.filter.filter(newText)
                return false
            }

        })

        fetchAllCases()
        return view
    }

    private fun fetchAllCases(){
        Log.i(TAG, "Making get request...")
        swipeRefresh.isRefreshing = true
        val requests = Covid19ApiClient.getApi(CasesInterface::class.java)
        val call = requests.getAllCases()

        call.enqueue(object : Callback<MutableList<Country>> {
            override fun onFailure(call: Call<MutableList<Country>>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Toast.makeText(context, "Unable to get cases", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Unable to get cases")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<MutableList<Country>>, response: Response<MutableList<Country>>) {
                swipeRefresh.isRefreshing = false
                if (response.isSuccessful) {
                    val casesList = response.body()!!

                    Log.i(TAG, "onResponse: got ${casesList.size} cases")
                    //remove null last update case in response array
                    casesList.removeAt(casesList.size - 1)
                    casesList.sortBy { it.country }
                    casesAdapter.setCaseList(casesList)

                    recyclerView.adapter = casesAdapter
                } else {
                    response.raw().body?.close()
                }
            }
        })
    }

    override fun onItemClick(countryCase: Country) {
        val countryName = countryCase.country.trim().replace(" ", "-")
        val url = CASES_URL + countryName
        val readIntent = Intent(context, ReadActivity::class.java)
        readIntent.putExtra(MainActivity.EXTRA_TITLE, countryCase.country)
        readIntent.putExtra(MainActivity.EXTRA_URL, url)

        Log.i(TAG, "onItemClick: Opening $url")
        startActivity(readIntent)
    }


}
