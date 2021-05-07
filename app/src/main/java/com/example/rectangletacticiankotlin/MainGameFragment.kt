package com.example.rectangletacticiankotlin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class MainGameFragment : Fragment(), View.OnClickListener {

    private lateinit var exceptionTV: TextView
    private var isException = false

    var playerCount = 0
    var fieldWidth = 0
    var fieldHeight = 0
    var playerNumber = 0

    //companion object {
        var rectWidth = 0
        var rectHeight = 0
    //}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_game, container, false)

        playerCount = arguments?.getInt("playerCount") ?: 2
        fieldWidth = arguments?.getInt("fieldWidth") ?: 25
        fieldHeight = arguments?.getInt("fieldHeight") ?: 35
        playerNumber = arguments?.getInt("playerNumber") ?: 1

        exceptionTV = view.findViewById(R.id.exceptionTV)

        Log.d("my", "playerCountMainGameFrag: $playerCount")
        Log.d("my", "fieldWidthMainGameFrag: $fieldWidth")
        Log.d("my", "fieldHeightMainGameFrag: $fieldHeight")
        Log.d("my", "playerNumberMainGameFrag: $playerNumber")
        Log.d("my", "rectWidthMainGameFrag: $rectWidth")
        Log.d("my", "rectHeightMainGameFrag: $rectHeight")

        exceptionTVNoRectangle()
        return view
    }

    fun exceptionTVNoException() {
        isException = true
        exceptionTV.setBackgroundColor(Color.GREEN)
        exceptionTV.text = getString(R.string.exceptionTV_good_text)
    }

    fun exceptionTVNoRectangle() {
        isException = false
        exceptionTV.setBackgroundColor(Color.BLUE)
        exceptionTV.text = getString(R.string.exceptionTV_bad_NoRectangle_text)
    }

    fun exceptionTVOutOfBounds() {
        isException = false
        exceptionTV.setBackgroundColor(Color.RED)
        exceptionTV.text = getString(R.string.exceptionTV_bad_OutOfBoundsException_text)
    }

    fun exceptionTVLocation() {
        isException = false
        exceptionTV.setBackgroundColor(Color.RED)
        exceptionTV.text = getString(R.string.exceptionTV_bad_LocationException_text)
    }

    override fun onClick(v: View?) {
//        when(view.id) {
//            R.id.rotationButton -> {
//                fieldWidth += fieldHeight
//                fieldHeight = fieldWidth - fieldHeight
//                fieldWidth -= fieldHeight
//                //check players' rectangle
//            }
//            R.id.endPlayerTurnButton -> {
//                //remember players' coordinates & next turn
//            }
//        }
    }

}