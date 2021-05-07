package com.example.rectangletacticiankotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RegulationsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_regulations, container, false)

        //(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.regulationsActivity_name)

        val recyclerView = view.findViewById<RecyclerView>(R.id.regulationsRecyclerView)
        val regulationsList = listOf(
            resources.getString(R.string.regulationsMain_text),
            resources.getString(R.string.regulations1_text),
            resources.getString(R.string.regulations2_text),
            resources.getString(R.string.regulations3_text),
            resources.getString(R.string.regulations4_text),
            resources.getString(R.string.regulations5_text),
            resources.getString(R.string.regulations6_text),
            resources.getString(R.string.regulations7_text),
            resources.getString(R.string.regulationsPS_text),
        )
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MyRecyclerAdapter(regulationsList)

        return view
    }
}