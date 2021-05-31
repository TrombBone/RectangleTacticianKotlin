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

class DiceFragment(private val listener: OnFragmentListener, private val allData: MyAppData) : Fragment(), View.OnClickListener {

    lateinit var sideWidthTV: TextView
    lateinit var sideHeightTV: TextView
    private lateinit var generateButton: Button
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dice, container, false)

        sideWidthTV = view.findViewById(R.id.widthSideTV)
        sideHeightTV = view.findViewById(R.id.heightSideTV)
        generateButton = view.findViewById(R.id.generateSidesButton)

        generateButton.setOnClickListener(this)

        mediaPlayer = MediaPlayer.create(activity, R.raw.dice)

        return view
    }

    override fun onClick(view: View) {
        mediaPlayer.start()
        generateButton.isEnabled = false

        for (i in 1..5) {
            var del = 0
            if (i == 5) del = 300
            Timer().schedule((230 * i + del).toLong()) {
                activity?.runOnUiThread {
                    allData.apply {
                        setRectDrawNowSizes()
                        val rectDrawNowSizes = getRectDrawNowSizes()
                        sideWidthTV.text = rectDrawNowSizes.first.toString()
                        sideHeightTV.text = rectDrawNowSizes.second.toString()
                    }
                }
            }
        }

        Timer().schedule(2500) {
            mediaPlayer.stop()
            listener.onButtonSelected(R.id.generateSidesButton)
        }
    }
}