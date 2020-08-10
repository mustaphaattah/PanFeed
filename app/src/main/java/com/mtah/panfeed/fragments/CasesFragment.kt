package com.mtah.panfeed.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mtah.panfeed.R
import com.mtah.panfeed.adapters.CasesAdapter
import com.mtah.panfeed.api.CasesInterface
import com.mtah.panfeed.api.Covid19ApiClient
import com.mtah.panfeed.models.Country
import com.mtah.panfeed.models.CountryList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [CasesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CasesFragment : Fragment() {

    private val TAG = "CasesFragment"
    lateinit var countryName: TextView
    lateinit var confirmedNumber: TextView
    lateinit var recoveredNumber: TextView
    lateinit var deathsNumber: TextView

    val casesList = mutableListOf<Country>()
    private lateinit var casesAdapter: CasesAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var swipeRefresh: SwipeRefreshLayout




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cases, container, false)

        recyclerView = view.findViewById(R.id.casesRecyclerView)
        layoutManager = LinearLayoutManager(context)

        swipeRefresh = view.findViewById(R.id.casesRefresh)
        swipeRefresh.setOnRefreshListener { fetchAllCases() }

        fetchAllCases()
//        fetchTotal()

        return view
    }

    private fun fetchAllCases(){
        Log.i(TAG, "Making get request...")
        swipeRefresh.isRefreshing = true
        val requests = Covid19ApiClient.getApi(CasesInterface::class.java)
        val call = requests.getAllCases()

        call.enqueue(object : Callback<Country> {
            override fun onFailure(call: Call<Country>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "get request FAILED")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Country>, response: Response<Country>) {
                swipeRefresh.isRefreshing = false
                Log.i(TAG, "onResponse ${response.body()}")
                if (response.isSuccessful){
                    val case = response.body()!!
                    casesList.add(case)

                    Log.i(TAG, "onResponse: got $case cases")
                    casesAdapter = CasesAdapter(casesList)

                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = casesAdapter
                } else {
                    //close response body
                    response.raw().body?.close()
                }
            }
        })
    }

    private fun fetchTotal(){
        val req = Covid19ApiClient.getApi(CasesInterface::class.java)
        val call = req.getCases()

        call.enqueue(object : Callback<List<Country>> {
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.i(TAG, "${ t.message }")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful){
                    val cases = response.body()
                    Log.i(TAG, "Got ${cases?.size} cases")
                    cases?.stream()?.forEach { Log.i(TAG, it.toString()) }
                }
            }

        })
    }

}
