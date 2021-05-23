package com.example.rectangletacticiankotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class StartFragment(private val listener: OnFragmentListener) : Fragment(), View.OnClickListener {

    private lateinit var startGameButton: Button
    private lateinit var regulationsButton: Button
    private lateinit var settingsButton: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)
        //listener = activity as OnFragmentListener? ?: throw IllegalStateException("OnFragmentListener is cannot be null")

        startGameButton = view.findViewById(R.id.startButton)
        regulationsButton = view.findViewById(R.id.regulationsButton)
        settingsButton = view.findViewById(R.id.settingsButton)

        startGameButton.setOnClickListener(this)
        regulationsButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)

        return view
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.startButton -> listener.onButtonSelected(R.id.startButton)
            R.id.regulationsButton -> listener.onButtonSelected(R.id.regulationsButton)
            R.id.settingsButton -> listener.onButtonSelected(R.id.settingsButton)
        }
    }
}