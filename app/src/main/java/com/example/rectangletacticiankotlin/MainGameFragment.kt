package com.example.rectangletacticiankotlin

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rectangletacticiankotlin.MySurfaceView.Companion.rectDrawNow
import com.otaliastudios.zoom.ZoomSurfaceView


class MainGameFragment : Fragment(), View.OnClickListener {

    private lateinit var exceptionTV: TextView
    private var isNotException = false

    private lateinit var rotationButton: Button

    companion object {
        var playerCount = 0
        var playerNumber = 0

        var fieldWidth = 0
        var fieldHeight = 0

        var rectWidth = 0
        var rectHeight = 0

        val playersRectangles = mutableMapOf<Int, MutableList<RectF>>()
    }



//    lateinit var surfaceView: MySurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_game, container, false)
//        Log.d("my", "arguments_MainGameFragment: $arguments")

        playerCount = arguments?.getInt("playerCount") ?: 2
        fieldWidth = arguments?.getInt("fieldWidth") ?: 25
        fieldHeight = arguments?.getInt("fieldHeight") ?: 35
        playerNumber = arguments?.getInt("playerNumber") ?: 0
        rectWidth = arguments?.getInt("rectWidth") ?: 0
        rectHeight = arguments?.getInt("rectHeight") ?: 0

        exceptionTV = view.findViewById(R.id.exceptionTV)

        rotationButton = view.findViewById(R.id.rotationButton)
        rotationButton.setOnClickListener(this)

//        surfaceView = view.findViewById(R.id.mySurfaceView)
//        surfaceView.mainGameFragment = this

        //-------------------------------

        val surfaceView: ZoomSurfaceView = view.findViewById(R.id.zoom_Surface_View)


        surfaceView.addCallback(object : ZoomSurfaceView.Callback {
            lateinit var drawThread: DrawThread

            override fun onZoomSurfaceCreated(view: ZoomSurfaceView) {
                val surface: Surface? = view.surface
                view.realZoomTo(view.realZoom / 100, true)
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
                    while (isRunning) {
                        val canvas: Canvas? = surface?.lockCanvas(null)
                        p.color = Color.BLACK
                        p.style = Paint.Style.STROKE// contours
                        //p.strokeWidth = 1f

                        if (canvas != null) {
                            canvas.drawColor(Color.WHITE)

                            val canvasWidth = canvas.width
                            val canvasHeight = canvas.height
                            Log.d("my", "canvasWidth: $canvasWidth")
                            cellSize = canvasWidth.toFloat() / fieldWidth
                            Log.d("my", "cellSize: $cellSize")

                            canvas.drawLine(0f, 0f, 0.5f, 0.5f, p)
//                            drawMesh(canvas)
//                            drawStartPlaces(canvas)
//
//                            drawPlayersRectOld(canvas)
//                            drawPlayerRectNow(canvas)

                            //mainChecker()
                            surface?.unlockCanvasAndPost(canvas)
                        }
                    }
                }

                private fun drawMesh(canvas: Canvas) {
                    for (x in 0..(fieldWidth * cellSize).toInt() step cellSize.toInt()) canvas.drawLine(
                        x.toFloat(), 0f,
                        x.toFloat(), fieldHeight * cellSize,
                        p
                    )// vertical lines
                    for (y in 0..(fieldHeight * cellSize).toInt() step cellSize.toInt()) canvas.drawLine(
                        0f, y.toFloat(),
                        fieldWidth * cellSize, y.toFloat(),
                        p
                    )// horizontal lines
                }

                private fun drawStartPlaces(canvas: Canvas) {
                    canvas.apply {
                        val width = fieldWidth * cellSize
                        val height = fieldHeight * cellSize
                        p.style = Paint.Style.FILL
                        p.color = resources.getColor(R.color.player1, resources.newTheme())
                        p.alpha = 95
                        drawRect(0f, 0f, cellSize, cellSize, p)
                        p.color = resources.getColor(R.color.player2, resources.newTheme())
                        p.alpha = 95
                        drawRect(width - cellSize, height - cellSize, width, height, p)
                        if (playerCount == 4) {
                            p.color = resources.getColor(R.color.player3, resources.newTheme())
                            p.alpha = 95
                            drawRect(width - cellSize, 0f, width, cellSize, p)
                            p.color = resources.getColor(R.color.player4, resources.newTheme())
                            p.alpha = 95
                            drawRect(0f, height - cellSize, cellSize, height, p)
                        }
                        p.color = Color.BLACK
                        p.style = Paint.Style.STROKE
                    }
                }

                private fun drawPlayerRectNow(canvas: Canvas) {
                    if (canDraw) {
                        var tx = touchX
                        var ty = touchY
                        var x = 0f
                        var y = 0f
                        var i = 0
                        var j = 0
                        while (tx > cellSize) {
                            i++
                            tx -= cellSize
                        }
                        while (ty > cellSize) {
                            j++
                            ty -= cellSize
                        }
                        when (playerNumber) {
                            1 -> {
                                p.color = resources.getColor(R.color.player1, resources.newTheme())
                                x = i * cellSize
                                y = j * cellSize
                            }
                            2 -> {
                                p.color = resources.getColor(R.color.player2, resources.newTheme())
                                x = (i - rectWidth + 1) * cellSize
                                y = (j - rectHeight + 1) * cellSize
                            }
                            3 -> {
                                p.color = resources.getColor(R.color.player3, resources.newTheme())
                                x = (i - rectWidth + 1) * cellSize
                                y = j * cellSize
                            }
                            4 -> {
                                p.color = resources.getColor(R.color.player4, resources.newTheme())
                                x = i * cellSize
                                y = (j - rectHeight + 1) * cellSize
                            }
                        }
                        p.style = Paint.Style.FILL
                        rectDrawNow = RectF(x, y, x + cellSize * rectWidth, y + cellSize * rectHeight)
                        canvas.drawRect(rectDrawNow, p)
                        when (playerNumber) {
                            1 -> p.color = resources.getColor(R.color.player1_border, resources.newTheme())
                            2 -> p.color = resources.getColor(R.color.player2_border, resources.newTheme())
                            3 -> p.color = resources.getColor(R.color.player3_border, resources.newTheme())
                            4 -> p.color = resources.getColor(R.color.player4_border, resources.newTheme())
                        }
                        p.strokeWidth = 6f
                        p.style = Paint.Style.STROKE
                        canvas.drawRect(
                            RectF(
                                x + 4, y + 4,
                                x + cellSize * rectWidth - 4, y + cellSize * rectHeight - 4
                            ), p
                        )
                        p.strokeWidth = 1f
                        p.style = Paint.Style.STROKE
                        p.color = Color.BLACK
                    }
                }

                private fun drawPlayersRectOld(canvas: Canvas) {
                    for (player in 1..playerCount) {
                        val list = playersRectangles[player]
                        if (list != null) {
                            for (i in 0..list.lastIndex) {
                                val rect = list[i]
                                when (player) {
                                    1 -> p.color = resources.getColor(R.color.player1, resources.newTheme())
                                    2 -> p.color = resources.getColor(R.color.player2, resources.newTheme())
                                    3 -> p.color = resources.getColor(R.color.player3, resources.newTheme())
                                    4 -> p.color = resources.getColor(R.color.player4, resources.newTheme())
                                }
                                p.style = Paint.Style.FILL
                                canvas.drawRect(rect, p)
                                when (player) {
                                    1 -> p.color =
                                        resources.getColor(R.color.player1_border, resources.newTheme())
                                    2 -> p.color =
                                        resources.getColor(R.color.player2_border, resources.newTheme())
                                    3 -> p.color =
                                        resources.getColor(R.color.player3_border, resources.newTheme())
                                    4 -> p.color =
                                        resources.getColor(R.color.player4_border, resources.newTheme())
                                }
                                p.strokeWidth = 6f
                                p.style = Paint.Style.STROKE
                                canvas.drawRect(
                                    RectF(
                                        rect.left + 4, rect.top + 4,
                                        rect.right - 4, rect.bottom - 4
                                    ), p
                                )
                            }
                        }
                    }
                    p.strokeWidth = 1f
                    p.style = Paint.Style.STROKE
                    p.color = Color.BLACK
                }
            }

//            private fun drawMesh(canvas: Canvas) {
//                for (x in 0..(fieldWidth * cellSize).toInt() step cellSize.toInt()) canvas.drawLine(
//                    x.toFloat(), 0f,
//                    x.toFloat(), fieldHeight * cellSize,
//                    p
//                )// vertical lines
//                for (y in 0..(fieldHeight * cellSize).toInt() step cellSize.toInt()) canvas.drawLine(
//                    0f, y.toFloat(),
//                    fieldWidth * cellSize, y.toFloat(),
//                    p
//                )// horizontal lines
//            }
//
//            private fun drawStartPlaces(canvas: Canvas) {
//                canvas.apply {
//                    val width = fieldWidth * cellSize
//                    val height = fieldHeight * cellSize
//                    p.style = Paint.Style.FILL
//                    p.color = resources.getColor(R.color.player1, resources.newTheme())
//                    p.alpha = 95
//                    drawRect(0f, 0f, cellSize, cellSize, p)
//                    p.color = resources.getColor(R.color.player2, resources.newTheme())
//                    p.alpha = 95
//                    drawRect(width - cellSize, height - cellSize, width, height, p)
//                    if (playerCount == 4) {
//                        p.color = resources.getColor(R.color.player3, resources.newTheme())
//                        p.alpha = 95
//                        drawRect(width - cellSize, 0f, width, cellSize, p)
//                        p.color = resources.getColor(R.color.player4, resources.newTheme())
//                        p.alpha = 95
//                        drawRect(0f, height - cellSize, cellSize, height, p)
//                    }
//                    p.color = Color.BLACK
//                    p.style = Paint.Style.STROKE
//                }
//            }
//
//            private fun drawPlayerRectNow(canvas: Canvas) {
//                if (canDraw) {
//                    var tx = touchX
//                    var ty = touchY
//                    var x = 0f
//                    var y = 0f
//                    var i = 0
//                    var j = 0
//                    while (tx > cellSize) {
//                        i++
//                        tx -= cellSize
//                    }
//                    while (ty > cellSize) {
//                        j++
//                        ty -= cellSize
//                    }
//                    when (playerNumber) {
//                        1 -> {
//                            p.color = resources.getColor(R.color.player1, resources.newTheme())
//                            x = i * cellSize
//                            y = j * cellSize
//                        }
//                        2 -> {
//                            p.color = resources.getColor(R.color.player2, resources.newTheme())
//                            x = (i - rectWidth + 1) * cellSize
//                            y = (j - rectHeight + 1) * cellSize
//                        }
//                        3 -> {
//                            p.color = resources.getColor(R.color.player3, resources.newTheme())
//                            x = (i - rectWidth + 1) * cellSize
//                            y = j * cellSize
//                        }
//                        4 -> {
//                            p.color = resources.getColor(R.color.player4, resources.newTheme())
//                            x = i * cellSize
//                            y = (j - rectHeight + 1) * cellSize
//                        }
//                    }
//                    p.style = Paint.Style.FILL
//                    rectDrawNow = RectF(x, y, x + cellSize * rectWidth, y + cellSize * rectHeight)
//                    canvas.drawRect(rectDrawNow, p)
//                    when (playerNumber) {
//                        1 -> p.color = resources.getColor(R.color.player1_border, resources.newTheme())
//                        2 -> p.color = resources.getColor(R.color.player2_border, resources.newTheme())
//                        3 -> p.color = resources.getColor(R.color.player3_border, resources.newTheme())
//                        4 -> p.color = resources.getColor(R.color.player4_border, resources.newTheme())
//                    }
//                    p.strokeWidth = 6f
//                    p.style = Paint.Style.STROKE
//                    canvas.drawRect(
//                        RectF(
//                            x + 4, y + 4,
//                            x + cellSize * rectWidth - 4, y + cellSize * rectHeight - 4
//                        ), p
//                    )
//                    p.strokeWidth = 1f
//                    p.style = Paint.Style.STROKE
//                    p.color = Color.BLACK
//                }
//            }
//
//            private fun drawPlayersRectOld(canvas: Canvas) {
//                for (player in 1..playerCount) {
//                    val list = playersRectangles[player]
//                    if (list != null) {
//                        for (i in 0..list.lastIndex) {
//                            val rect = list[i]
//                            when (player) {
//                                1 -> p.color = resources.getColor(R.color.player1, resources.newTheme())
//                                2 -> p.color = resources.getColor(R.color.player2, resources.newTheme())
//                                3 -> p.color = resources.getColor(R.color.player3, resources.newTheme())
//                                4 -> p.color = resources.getColor(R.color.player4, resources.newTheme())
//                            }
//                            p.style = Paint.Style.FILL
//                            canvas.drawRect(rect, p)
//                            when (player) {
//                                1 -> p.color =
//                                    resources.getColor(R.color.player1_border, resources.newTheme())
//                                2 -> p.color =
//                                    resources.getColor(R.color.player2_border, resources.newTheme())
//                                3 -> p.color =
//                                    resources.getColor(R.color.player3_border, resources.newTheme())
//                                4 -> p.color =
//                                    resources.getColor(R.color.player4_border, resources.newTheme())
//                            }
//                            p.strokeWidth = 6f
//                            p.style = Paint.Style.STROKE
//                            canvas.drawRect(
//                                RectF(
//                                    rect.left + 4, rect.top + 4,
//                                    rect.right - 4, rect.bottom - 4
//                                ), p
//                            )
//                        }
//                    }
//                }
//                p.strokeWidth = 1f
//                p.style = Paint.Style.STROKE
//                p.color = Color.BLACK
//            }
        })

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
        isNotException = true
        exceptionTV.setBackgroundColor(Color.argb(127, 0, 255, 0))
        exceptionTV.text = activity?.getString(R.string.exceptionTV_good_text)
    }

    fun exceptionTVNoRectangle() {
        isNotException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 0, 0, 255))
        exceptionTV.text = activity?.getString(R.string.exceptionTV_bad_NoRectangle_text)
    }

    fun exceptionTVOutOfBounds() {
        isNotException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 255, 0, 0))
        exceptionTV.text = activity?.getString(R.string.exceptionTV_bad_OutOfBoundsException_text)
    }

    fun exceptionTVLocation() {
        isNotException = false
        exceptionTV.setBackgroundColor(Color.argb(127, 255, 0, 0))
        exceptionTV.text = activity?.getString(R.string.exceptionTV_bad_LocationException_text)
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
                if (isNotException) {
                    playersRectangles.getOrPut(playerNumber, { mutableListOf() }).add(rectDrawNow)
                    rotationButton.isEnabled = isNotException
//                Log.d("my", "playersRectangles: $playersRectangles")
                    listener?.onButtonSelected(R.id.nextTurnButton)// next turn
                }

            }
        }
    }
}