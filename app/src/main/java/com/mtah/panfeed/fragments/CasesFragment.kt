package com.mtah.panfeed.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.mtah.panfeed.R
import com.mtah.panfeed.api.CasesInterface
import com.mtah.panfeed.api.Covid19ApiClient
import com.mtah.panfeed.models.Country
import com.mtah.panfeed.models.TotalCases
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
    lateinit var confirmedNumber: TextView
    lateinit var recoveredNumber: TextView
    lateinit var deathsNumber: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cases, container, false)

        confirmedNumber = view.findViewById(R.id.confirmedNumber)
        recoveredNumber = view.findViewById(R.id.recoveredNumber)
        deathsNumber = view.findViewById(R.id.deathNumber)


        fetchAllCases()

        return view
    }

    private fun fetchAllCases(){
        var requests = Covid19ApiClient.getApi(CasesInterface::class.java)
        var call = requests.getTotalCases()

        call.enqueue(object : Callback<Country> {
            override fun onFailure(call: Call<Country>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
                throw t
            }

            override fun onResponse(call: Call<Country>, response: Response<Country>) {
                Log.i(TAG, "onResponse ${response.body().toString()}")
                if (response.isSuccessful){
                    var totalCases = response.body()!!
                    Log.i(TAG, "onResponse $totalCases")
                    confirmedNumber.text = totalCases.cases
                    recoveredNumber.text = totalCases.recovered
                    deathsNumber.text = totalCases.deaths
                }
            }
        })
    }

}
