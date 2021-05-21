package com.example.rectangletacticiankotlin

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlayerStartFragment(val listener: OnFragmentListener?) : Fragment(), View.OnTouchListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_player_start, container, false)

        val playerNumber = arguments?.getInt("playerNumber") ?: 1

        view.findViewById<TextView>(R.id.playerNotificationTV).apply {
            text = "${getString(R.string.playerNotificationTV_text)} $playerNumber"

            setTextColor(when(playerNumber) {
                1 -> resources.getColor(R.color.player1, resources.newTheme())
                2 -> resources.getColor(R.color.player2, resources.newTheme())
                3 -> resources.getColor(R.color.player3, resources.newTheme())
                4 -> resources.getColor(R.color.player4, resources.newTheme())
                else -> Color.BLACK
            })
        }

        view.setOnTouchListener(this)

        return view
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        listener?.onButtonSelected(R.id.playerNotificationTV)
        return true
    }
}