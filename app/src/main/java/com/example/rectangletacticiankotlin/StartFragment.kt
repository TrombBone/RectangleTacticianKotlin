package com.example.rectangletacticiankotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class StartFragment : Fragment(), View.OnClickListener {

    private lateinit var startGameButton: Button
    private lateinit var regulationsButton: Button
    private lateinit var settingsButton: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        startGameButton = view.findViewById(R.id.startButton)
        regulationsButton = view.findViewById(R.id.regulationsButton)
        settingsButton = view.findViewById(R.id.settingsButton)

        startGameButton.setOnClickListener(this)
        regulationsButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)

        return view
    }

    override fun onClick(view: View) {
        //val listener = activity as OnFragmentListener?
        when(view.id) {
            R.id.startButton -> {
                val myDialogFragment = MyDialogFragment()
                val myManager = activity!!.supportFragmentManager
                myDialogFragment.show(myManager, "myDialog")
            }
            R.id.regulationsButton -> activity!!.supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                replace(R.id.frame_container_start, RegulationsFragment())
                commit()
            }
            R.id.settingsButton -> activity!!.supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                replace(R.id.frame_container_start, SettingsFragment())
                commit()
            }
        }
    }
}