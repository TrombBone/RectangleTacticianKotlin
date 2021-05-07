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
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playerNumber
//import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playerCount
//import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectWidth
//import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectHeight

class MySurfaceView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    init {
        SurfaceView(context, attrs)
        holder.addCallback(this)
    }

    private var touchX: Float = 0.0f //x-place of touch
    private var touchY: Float = 0.0f //y-place of touch
    private var canvasWidth = 0 //of Canvas
    private var canvasHeight = 0 //of Canvas
    private var cellSize = 0 //cell size
    private var fieldStartX = 0
    private var fieldStartY = 0
    private var fieldEndX = 0
    private var fieldEndY = 0
    private var draw = false

    fun mainChecker() {
        //
    }

    override fun onTouchEvent(event: MotionEvent) : Boolean {
        touchX = event.x
        touchY = event.y

        when(playerNumber) {//check players' rectangles
            1 -> {}
            2 -> {}
            3 -> {}
            4 -> {}
        }

        return true
    }

    inner class DrawThread(private val surfaceHolder: SurfaceHolder) : Thread() {
        var p = Paint()

        override fun run() {
            val canvas: Canvas = surfaceHolder.lockCanvas(null)
            p.color = Color.BLACK
            p.style = Paint.Style.STROKE //контуры

            canvas.drawColor(Color.WHITE)
            canvasWidth = canvas.width
            canvasHeight = canvas.height
            cellSize = canvasWidth / fieldWidth

            //draw mesh
            for (x in 0..fieldWidth * cellSize step cellSize) canvas.drawLine(x.toFloat(), 0f, x.toFloat(), (fieldHeight*cellSize).toFloat(), p) //vertical lines
            for (y in 0..fieldHeight * cellSize step cellSize) canvas.drawLine(0f, y.toFloat(), (fieldWidth*cellSize).toFloat(), y.toFloat(), p) //horizontal lines

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val dt = DrawThread(holder)
        dt.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}