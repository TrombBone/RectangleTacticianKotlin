package com.example.rectangletacticiankotlin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class MainGameFragment : Fragment(), View.OnClickListener {

    private lateinit var exceptionTV: TextView
    private var isException = false

    companion object {
        var playerCount = 0
        var playerNumber = 0

        var fieldWidth = 0
        var fieldHeight = 0

        var rectWidth = 0
        var rectHeight = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_game, container, false)
        Log.d("my", "arguments_MainGameFragment: $arguments")

        playerCount = arguments?.getInt("playerCount") ?: 2
        fieldWidth = arguments?.getInt("fieldWidth") ?: 25
        fieldHeight = arguments?.getInt("fieldHeight") ?: 35
        playerNumber = arguments?.getInt("playerNumber") ?: 0
        rectWidth = arguments?.getInt("rectWidth") ?: 0
        rectHeight = arguments?.getInt("rectHeight") ?: 0

        exceptionTV = view.findViewById(R.id.exceptionTV)

        view.findViewById<Button>(R.id.rotationButton).setOnClickListener(this)
        view.findViewById<Button>(R.id.nextTurnButton).setOnClickListener(this)

        Log.d("my", "playerCountMainGameFragment: $playerCount")
        Log.d("my", "fieldWidthMainGameFragment: $fieldWidth")
        Log.d("my", "fieldHeightMainGameFragment: $fieldHeight")
        Log.d("my", "playerNumberMainGameFragment: $playerNumber")
        Log.d("my", "rectWidthMainGameFragment: $rectWidth")
        Log.d("my", "rectHeightMainGameFragment: $rectHeight")

        exceptionTVNoRectangle()
        return view
    }

    fun exceptionTVNoException() {
        isException = true
        exceptionTV.setBackgroundColor(Color.argb(127, 0, 255, 0))
        exceptionTV.text = getString(R.string.exceptionTV_good_text)
    }

    fun exceptionTVNoRectangle() {
        isException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 0, 0, 255))
        exceptionTV.text = getString(R.string.exceptionTV_bad_NoRectangle_text)
    }

    fun exceptionTVOutOfBounds() {
        isException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 255, 0, 0))
        exceptionTV.text = getString(R.string.exceptionTV_bad_OutOfBoundsException_text)
    }

    fun exceptionTVLocation() {
        isException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 255, 0, 0))
        exceptionTV.text = getString(R.string.exceptionTV_bad_LocationException_text)
    }

    override fun onClick(view: View) {
        val listener = activity as OnFragmentListener?
        when(view.id) {
            R.id.rotationButton -> {
                rectWidth += rectHeight
                rectHeight = rectWidth - rectHeight
                rectWidth -= rectHeight
                //check player's rectangle again
            }
            R.id.nextTurnButton -> {
                //remember players' coordinates & next turn
                listener?.onButtonSelected(R.id.nextTurnButton)// next turn
            }
        }
    }
}