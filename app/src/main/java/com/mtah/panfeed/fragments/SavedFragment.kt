package com.mtah.panfeed.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.mtah.panfeed.R
import com.mtah.panfeed.SavedViewModel
import com.mtah.panfeed.adapters.SavedNewsAdapter


class SavedFragment : Fragment() {
//    private lateinit var savedNewsRecyclerView: RecyclerView
    private lateinit var savedViewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)

        val savedNewsRecyclerView: RecyclerView = view.findViewById(R.id.savedNewsRecyclerView)
        savedNewsRecyclerView.layoutManager = LinearLayoutManager(context)
        val savedNewsAdapter = SavedNewsAdapter()
        savedNewsRecyclerView.adapter = savedNewsAdapter

        savedViewModel = ViewModelProvider(this).get(SavedViewModel::class.java)
        savedViewModel.getAll().observe(viewLifecycleOwner, Observer { news ->
            savedNewsAdapter.setNewsList(news)
        })

        return view
    }
}
