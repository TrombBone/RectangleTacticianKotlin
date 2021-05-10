package com.example.rectangletacticiankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.fieldHeight
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.fieldWidth
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playerCount
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playerNumber
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectHeight
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectWidth

class MySurfaceView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    init {
        SurfaceView(context, attrs)
        holder.addCallback(this)
    }

    private var touchX = 0f
    private var touchY = 0f
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var cellSize = 0f
    private var fieldStartX = 0f
    private var fieldStartY = 0f
    private var fieldEndX = 0f
    private var fieldEndY = 0f
    private var canDraw = true

//    fun mainChecker() {
//        //
//    }

    override fun onTouchEvent(event: MotionEvent) : Boolean {
        touchX = event.x
        touchY = event.y

        when(playerNumber) {// check players' rectangles
            1 -> {}
            2 -> {}
            3 -> {}
            4 -> {}
        }

        return true
    }

    inner class DrawThread(private val surfaceHolder: SurfaceHolder) : Thread() {
        var isRunning = true
        private var p = Paint()

        override fun run() {
            while (isRunning) {
                val canvas: Canvas? = surfaceHolder.lockCanvas(null)
                p.color = Color.BLACK
                p.style = Paint.Style.STROKE// контуры

                if (canvas != null) {
                    canvas.drawColor(Color.WHITE)

                    canvasWidth = canvas.width
                    canvasHeight = canvas.height
                    cellSize = (canvasWidth / fieldWidth).toFloat()

                    //draw mesh
                    for (x in 0..(fieldWidth * cellSize).toInt() step cellSize.toInt())
                        canvas.drawLine(x.toFloat(), 0f, x.toFloat(), fieldHeight * cellSize, p)// vertical lines
                    for (y in 0..(fieldHeight * cellSize).toInt() step cellSize.toInt())
                        canvas.drawLine(0f, y.toFloat(), fieldWidth * cellSize, y.toFloat(), p)// horizontal lines

                    drawStartPlaces(canvas)
                    drawPlayerRect(canvas)
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
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

        private fun drawPlayerRect(canvas: Canvas) {
            if (canDraw) {
                p.style = Paint.Style.FILL
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
                when(playerNumber) {
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
                canvas.drawRect(x, y, x + cellSize * rectWidth, y + cellSize * rectHeight, p)
                p.color = Color.BLACK
                p.style = Paint.Style.STROKE
            }
        }
    }

    private lateinit var dt: DrawThread

    override fun surfaceCreated(holder: SurfaceHolder) {
        dt = DrawThread(holder)
        dt.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        dt.isRunning = false
        dt = DrawThread(holder)
        dt.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        dt.isRunning = false
    }
}