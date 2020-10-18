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
import com.mtah.panfeed.models.Case
import com.mtah.panfeed.models.Stats
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CasesFragment : Fragment(), CasesAdapter.OnCaseClickListener {

    private val caseUrl = "https://www.worldometers.info/coronavirus/country/"
    private val flagUrl = "https://www.countries-ofthe-world.com/flags-normal/flag-of-"
    private val imageType = ".png"
    private val TAG = "CasesFragment"

    private lateinit var casesAdapter: CasesAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var countrySearchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cases, container, false)

        recyclerView = view.findViewById(R.id.casesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        casesAdapter = CasesAdapter(context, this)
        recyclerView.adapter = casesAdapter

        swipeRefresh = view.findViewById(R.id.casesRefresh)
        swipeRefresh.setOnRefreshListener { fetchAllCases() }

        World.init(context)

        //filter search for countries
        countrySearchView = view.findViewById(R.id.countrySearch)
        countrySearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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

    //fetch all cases from api
    private fun fetchAllCases(){
        Log.i(TAG, "Making get request...")
        swipeRefresh.isRefreshing = true
        val requests = Covid19ApiClient.getApi(CasesInterface::class.java)
        val call = requests.getAllCases()

        call.enqueue(object : Callback<Stats> {
            override fun onResponse(call: Call<Stats>, response: Response<Stats>) {

                swipeRefresh.isRefreshing = false
                if (response.isSuccessful) {
                    if (response.body()?.results == null) {
                        val caseList = response.body()?.results
                        caseList?.forEach { Log.i(TAG, "Case: $it") }
                        Toast.makeText(requireContext(), "Null response body", Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "Null response body")
                    } else {

                        val caseList = response.body()?.results as MutableList<Case>
                        caseList.sortBy { it.country }
                        casesAdapter.setCaseList(caseList)
                    }
                } else {
                    Toast.makeText(requireContext(), "Could not get cases.", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "onResponse: not successful")
                }
            }

            override fun onFailure(call: Call<Stats>, t: Throwable) {
                Toast.makeText(requireContext(), "Unable to get cases", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    override fun onItemClick(countryCase: Case) {
        val countryName = countryCase.country.trim().replace(" ", "-")
        val url = caseUrl + countryName
        val readIntent = Intent(context, ReadActivity::class.java)
        readIntent.putExtra(MainActivity.EXTRA_TITLE, countryCase.country)
        readIntent.putExtra(MainActivity.EXTRA_URL, url)
        val imageUrl = flagUrl + countryCase.country.trim() + imageType
        readIntent.putExtra(MainActivity.EXTRA_IMAGE_URL, imageUrl)

        Log.i(TAG, "onItemClick: Opening $url")
        startActivity(readIntent)
    }


}
