package com.example.rectangletacticiankotlin

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

const val TV_GOOD_NO_EXCEPTION = "noException"
const val TV_BAD_NO_RECT = "noRect"
const val TV_BAD_OUT_OF_BOUNDS = "outOfBounds"
const val TV_BAD_LOCATION = "location"
const val TV_BAD_LOCATION_FIRST_RECT = "locationFirstRect"
const val TV_NEUTRAL_NO_FREE_SPACE = "noFreeSpace"

class MainGameFragment(private val listener: OnFragmentListener, private val allData: MyAppData) :
    Fragment(), View.OnClickListener, OnExceptionHintListener {

    private lateinit var linearLayout: LinearLayout
    private lateinit var exceptionTV: TextView

    private var isNotException = false

    private lateinit var rotationButton: Button

    private lateinit var mySurfaceView: MySurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_game, container, false)
//        Log.d("my", "arguments_MainGameFragment: $arguments")

        linearLayout = view.findViewById(R.id.fragment_mainGame_LL)
        exceptionTV = view.findViewById(R.id.exceptionTV)

        rotationButton = view.findViewById(R.id.rotationButton)
        rotationButton.setOnClickListener(this)

        view.findViewById<Button>(R.id.nextTurnButton).setOnClickListener(this)

        mySurfaceView = view.findViewById(R.id.mySurfaceView)
        mySurfaceView.mainGameFragment = this
        mySurfaceView.allData = allData

        allData.listener = this
/*
        allData.apply {
            Log.d("my", "playerCountMainGameFragment: $playerCount")
            Log.d("my", "fieldWidthMainGameFragment: $fieldWidth")
            Log.d("my", "fieldHeightMainGameFragment: $fieldHeight")
            Log.d("my", "playerNumberMainGameFragment: $playerNumber")
            Log.d("my", "rectWidthMainGameFragment: $rectWidth")
            Log.d("my", "rectHeightMainGameFragment: $rectHeight")
        }
 */
        exceptionTVNoRect()
        return view
    }

    fun exceptionTVNoException() {
        isNotException = true
        exceptionTV.setBackgroundColor(Color.argb(127, 0, 255, 0))
        exceptionTV.text = getString(R.string.exceptionTV_good_text)
    }

    fun exceptionTVNoRect() {
        isNotException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 0, 0, 255))
        exceptionTV.text = getString(R.string.exceptionTV_bad_NoRectangle_text)
    }

    fun exceptionTVOutOfBounds() {
        isNotException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 255, 0, 0))
        exceptionTV.text = getString(R.string.exceptionTV_bad_OutOfBoundsException_text)
    }

    fun exceptionTVLocation() {
        isNotException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 255, 0, 0))
        exceptionTV.text = getString(R.string.exceptionTV_bad_LocationException_text)
    }

    fun exceptionTVLocationFirstRect() {
        isNotException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 255, 0, 0))
        exceptionTV.text = getString(R.string.exceptionTV_bad_LocationException_firstRect_text)
    }

    fun exceptionTVNoFreeSpace() {
        isNotException = true
        exceptionTV.setBackgroundColor(Color.argb(127, 127, 0, 255))
        exceptionTV.text = getString(R.string.exceptionTV_neutral_NotFreeSpaceException_text)
    }

    override fun onClick(view: View) {
//        val listener = activity as OnFragmentListener?
        when (view.id) {
            R.id.rotationButton -> allData.rotateRect()
            R.id.nextTurnButton -> {
                if (isNotException) {
                    allData.apply {
                        playersRectangles.getOrPut(playerNumber, { mutableListOf() }).add(rectDrawNow)
                        if (isEndGame && playerNumber == playerCount) {
//                            lastTouch = true
                            this@MainGameFragment.listener.onButtonSelected(R.id.mySurfaceView)// id of this surfaceView
                        } else this@MainGameFragment.listener.onButtonSelected(R.id.nextTurnButton)// next turn
                        isRunning = false
//                      Log.d("my", "playersRectangles: $playersRectangles")
                    }
                }
            }
        }
    }

    override fun onExceptionSelected(name: String) {
        when (name) {
            TV_GOOD_NO_EXCEPTION -> activity?.runOnUiThread { exceptionTVNoException() }
            TV_BAD_NO_RECT -> activity?.runOnUiThread { exceptionTVNoRect() }
            TV_BAD_OUT_OF_BOUNDS -> activity?.runOnUiThread { exceptionTVOutOfBounds() }
            TV_BAD_LOCATION -> activity?.runOnUiThread { exceptionTVLocation() }
            TV_BAD_LOCATION_FIRST_RECT -> activity?.runOnUiThread { exceptionTVLocationFirstRect() }
            TV_NEUTRAL_NO_FREE_SPACE -> activity?.runOnUiThread { exceptionTVNoFreeSpace() }
        }
    }
}