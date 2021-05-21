package com.example.rectangletacticiankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.rectangletacticiankotlin.MainGameActivity.Companion.allData

class MySurfaceView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs),
    SurfaceHolder.Callback {
    init {
        //SurfaceView(context, attrs)
        holder.addCallback(this)
    }

    private var canDraw = false
    private var notSpaceCanDraw = true
    private var touchX = 0f
    private var touchY = 0f
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var cellSize = 0f

    lateinit var mainGameFragment: MainGameFragment

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        touchX = ev.x
        touchY = ev.y
        canDraw = notSpaceCanDraw
//        rectLocationChecker(rectDrawNow, true)

//        if (allData.lastTouch) {//try to show FinishDialog after seeing the field after ending the game
////            canDraw = notSpaceCanDraw
////            playersResults = totalPlayersArea()
//            val listener = MainGameActivity() as OnFragmentListener?
//            listener?.onButtonSelected(R.id.mySurfaceView)// id of this surfaceView
//        }

        return true
    }

    inner class DrawThread(private val surfaceHolder: SurfaceHolder) : Thread() {
        private val p = Paint()

        override fun run() {
            allData.isRunning = true
            while (allData.isRunning) {
                val canvas: Canvas? = surfaceHolder.lockCanvas(null)
                p.color = Color.BLACK
                p.style = Paint.Style.STROKE// contours
                p.strokeWidth = 1f

                if (canvas != null) {
                    canvas.drawColor(Color.WHITE)

                    canvasWidth = canvas.width
                    canvasHeight = canvas.height
                    cellSize = (canvasWidth / allData.fieldWidth).toFloat()
                    allData.cellSize = cellSize

                    drawMesh(canvas)
                    drawStartPlaces(canvas)

                    drawPlayersRectOld(canvas)

                    if (!allData.isFreeSpace()) {
                        allData.isEndGame = true
                        notSpaceCanDraw = false
                        canDraw = notSpaceCanDraw
                        mainGameFragment.apply {
                            activity?.runOnUiThread {
                                exceptionTVNotFreeSpace()
                            }
                        }
                    } else {
                        notSpaceCanDraw = true
                        allData.rectLocationChecker(allData.rectDrawNow, true)
                    }

                    drawPlayerRectNow(canvas)

                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }

        private fun drawMesh(canvas: Canvas) {
            allData.apply {
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
        }

        private fun drawStartPlaces(canvas: Canvas) {
            canvas.apply {
                val width = allData.fieldWidth * cellSize
                val height = allData.fieldHeight * cellSize
                p.style = Paint.Style.FILL
                p.color = resources.getColor(R.color.player1, resources.newTheme())
                p.alpha = 95
                drawRect(0f, 0f, cellSize, cellSize, p)
                p.color = resources.getColor(R.color.player2, resources.newTheme())
                p.alpha = 95
                drawRect(width - cellSize, height - cellSize, width, height, p)
                if (allData.playerCount == 4) {
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

        private fun drawPlayersRectOld(canvas: Canvas) {
            for (player in 1..allData.playerCount) {
                val list = allData.playersRectangles[player]
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
                allData.apply {
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
                    allData.rectDrawNow = RectF(x, y, x + cellSize * rectWidth, y + cellSize * rectHeight)
                    canvas.drawRect(allData.rectDrawNow, p)
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
                }
                p.strokeWidth = 1f
                p.style = Paint.Style.STROKE
                p.color = Color.BLACK
            }
        }
    }

    private lateinit var dt: DrawThread

    override fun surfaceCreated(holder: SurfaceHolder) {
        allData.mainGameFragment = mainGameFragment
        dt = DrawThread(holder)
        dt.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        allData.isRunning = false
        dt = DrawThread(holder)
        dt.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        allData.isRunning = false
    }
}