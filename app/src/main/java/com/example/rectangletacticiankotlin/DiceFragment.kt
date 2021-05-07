package com.example.rectangletacticiankotlin

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class DiceFragment : Fragment(), View.OnClickListener {

    lateinit var sideWidthTV: TextView
    lateinit var sideHeightTV: TextView
    lateinit var generateButton: Button
    lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dice, container, false)

        sideWidthTV = view.findViewById(R.id.widthSideTV)
        sideHeightTV = view.findViewById(R.id.heightSideTV)
        generateButton = view.findViewById(R.id.generateSidesButton)

        generateButton.setOnClickListener(this)
        mediaPlayer = MediaPlayer.create(activity, R.raw.dice)

        return view
    }

    override fun onClick(v: View?) {
        mediaPlayer.start()
        generateButton.isEnabled = false

        var rectWidth = ""
        var rectHeight = ""
        for (i in 1..5) {
            var del = 0
            if (i == 5) del = 300
            Timer().schedule((230 * i + del).toLong()) {
                activity?.runOnUiThread {
                    rectWidth = Random.nextInt(1, 7).toString()
                    rectHeight = Random.nextInt(1, 7).toString()
                    sideWidthTV.text = rectWidth
                    sideHeightTV.text = rectHeight
                }
            }
        }

        Timer().schedule(2500) {
            activity!!.supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.frame_container_main, MainGameFragment())
                commit()
            }
            val listener = activity as OnFragmentListener?
            listener?.apply {
                //onButtonSelected(R.id.generateSidesButton)
                onParamsSelected(mapOf("rectWidth" to rectWidth, "rectHeight" to rectHeight))
            }
        }
    }
}