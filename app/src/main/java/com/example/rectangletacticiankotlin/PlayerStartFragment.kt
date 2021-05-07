package com.example.rectangletacticiankotlin

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlayerStartFragment : Fragment(), View.OnTouchListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_player_start, container, false)
        val playerNumber = arguments?.getInt("playerNumber") ?: 2
        val tv = view.findViewById<TextView>(R.id.playerNotificationTV).apply {
            text = "${getString(R.string.playerNotificationTV_text)} $playerNumber"

            setTextColor(when(playerNumber) {
                1 -> Color.BLUE
                2 -> Color.RED
                3 -> Color.GREEN
                4 -> Color.YELLOW
                else -> Color.BLACK
            })
        }

        view.setOnTouchListener(this)

        return view
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        activity!!.supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(R.id.frame_container_main, DiceFragment())
            commit()
        }
//        val listener = activity as OnFragmentListener?
//        listener?.onButtonSelected(R.id.playerNotificationTV)
        return true
    }
}