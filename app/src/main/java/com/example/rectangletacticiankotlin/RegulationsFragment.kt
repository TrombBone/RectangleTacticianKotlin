package com.example.rectangletacticiankotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RegulationsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_regulations, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.regulationsActivity_name)

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

class MyRecyclerAdapter(private val rules: List<String>) : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView? = null

        init {
            tv = itemView.findViewById(R.id.recyclerviewTV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv?.text = rules[position]
    }

    override fun getItemCount(): Int = rules.size
}