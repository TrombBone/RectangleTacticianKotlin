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
import com.example.rectangletacticiankotlin.MainGameActivity.Companion.allData


class MainGameFragment(private val listener: OnFragmentListener?) : Fragment(), View.OnClickListener {

    private lateinit var linearLayout: LinearLayout
    private lateinit var exceptionTV: TextView

    private var isNotException = false

    private lateinit var rotationButton: Button

    lateinit var mySurfaceView: MySurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_game, container, false)
//        Log.d("my", "arguments_MainGameFragment: $arguments")

//        playerCount = arguments?.getInt("playerCount") ?: 2
//        fieldWidth = arguments?.getInt("fieldWidth") ?: 25
//        fieldHeight = arguments?.getInt("fieldHeight") ?: 35
//        playerNumber = arguments?.getInt("playerNumber") ?: 0
//        rectWidth = arguments?.getInt("rectWidth") ?: 0
//        rectHeight = arguments?.getInt("rectHeight") ?: 0

        linearLayout = view.findViewById(R.id.fragment_mainGame_LL)
        exceptionTV = view.findViewById(R.id.exceptionTV)

        rotationButton = view.findViewById(R.id.rotationButton)
        rotationButton.setOnClickListener(this)

        view.findViewById<Button>(R.id.nextTurnButton).setOnClickListener(this)

        mySurfaceView = view.findViewById(R.id.mySurfaceView)
        mySurfaceView.mainGameFragment = this

/*
        val surfaceView: ZoomSurfaceView = view.findViewById(R.id.zoom_Surface_View)
        surfaceView.setContentSize(linearLayout.width.toFloat(), linearLayout.height.toFloat())

        surfaceView.addCallback(object : ZoomSurfaceView.Callback {

            lateinit var drawThread: DrawThread
            override fun onZoomSurfaceCreated(view: ZoomSurfaceView) {
                val surface: Surface? = view.surface
                //view.setContentSize(surfaceView.width.toFloat(), surfaceView.height.toFloat())
                //view.realZoomTo(view.realZoom / 100, true)
                drawThread = DrawThread(surface)
                drawThread.start()
            }

            override fun onZoomSurfaceDestroyed(view: ZoomSurfaceView) {
                drawThread.isRunning = false
            }

            val a = view.setOnTouchListener { _, event ->
                touchX = event.x
                touchY = event.y
                return@setOnTouchListener false
            }

            var cellSize = 0f
            var canDraw = true
            var touchX = 0f
            var touchY = 0f

            inner class DrawThread(private val surface: Surface?) : Thread() {
                var isRunning = true
                private val p = Paint()

                override fun run() {
//                    var canvas: Canvas? = surface?.lockCanvas(null)
//
////                    val bitmap = Bitmap.createBitmap(linearLayout.width, linearLayout.height, Bitmap.Config.ARGB_8888)
////                    canvas?.setBitmap(bitmap)
//                    surface?.unlockCanvasAndPost(canvas)
                    while (isRunning) {
                        val canvas = surface?.lockCanvas(null)
                        p.color = Color.BLACK
                        p.style = Paint.Style.STROKE// contours
                        p.strokeWidth = 0.1f

                        if (canvas != null) {
                            canvas.drawColor(Color.WHITE)

                            val canvasWidth = canvas.width
                            val canvasHeight = canvas.height
                            //Log.d("my", "canvasWidth: $canvasWidth")
                            Log.d("my", "canvasWidth: ${canvas.width}, canvasHeight: ${canvas.height}")

//                            cellSize = canvasWidth.toFloat() / fieldWidth
//                            Log.d("my", "cellSize: $cellSize")

                            canvas.drawLine(0f, 0f, 0.5f, 0.5f, p)
//                            drawMesh(canvas)
//                            drawStartPlaces(canvas)
//
//                            drawPlayersRectOld(canvas)
//                            drawPlayerRectNow(canvas)

                            //mainChecker()
                            surface?.unlockCanvasAndPost(canvas)
                            sleep(1000)
                        }
                    }
                }
                //my methods
            }

        })
 */

//        allData.apply {
//            Log.d("my", "playerCountMainGameFragment: $playerCount")
//            Log.d("my", "fieldWidthMainGameFragment: $fieldWidth")
//            Log.d("my", "fieldHeightMainGameFragment: $fieldHeight")
//            Log.d("my", "playerNumberMainGameFragment: $playerNumber")
//            Log.d("my", "rectWidthMainGameFragment: $rectWidth")
//            Log.d("my", "rectHeightMainGameFragment: $rectHeight")
//        }

        exceptionTVNoRectangle()
        return view
    }

    fun exceptionTVNoException() {
        isNotException = true
        exceptionTV.setBackgroundColor(Color.argb(127, 0, 255, 0))
        exceptionTV.text = getString(R.string.exceptionTV_good_text)
    }

    fun exceptionTVNoRectangle() {
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

    fun exceptionTVNotFreeSpace() {
        isNotException = true
        exceptionTV.setBackgroundColor(Color.argb(127, 127, 0, 255))
        exceptionTV.text = getString(R.string.exceptionTV_neutral_NotFreeSpaceException_text)
    }

    override fun onClick(view: View) {
//        val listener = activity as OnFragmentListener?
        when(view.id) {
            R.id.rotationButton -> {
                allData.apply {
                    rectWidth += rectHeight
                    rectHeight = rectWidth - rectHeight
                    rectWidth -= rectHeight
                }
            }
            R.id.nextTurnButton -> {
                if (isNotException) {
                    allData.apply {
                        playersRectangles.getOrPut(playerNumber, { mutableListOf() }).add(rectDrawNow)
                        if (isEndGame && playerNumber == playerCount) {
//                            lastTouch = true
                            listener?.onButtonSelected(R.id.mySurfaceView)// id of this surfaceView
                        } else listener?.onButtonSelected(R.id.nextTurnButton)// next turn
                        isRunning = false
//                      Log.d("my", "playersRectangles: $playersRectangles")
                    }
                }
            }
        }
    }
}