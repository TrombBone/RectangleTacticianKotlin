package com.example.rectangletacticiankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
//import com.example.rectangletacticiankotlin.MainActivity.Companion.fieldHeight
//import com.example.rectangletacticiankotlin.MainActivity.Companion.fieldWidth

class MySurfaceView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    init {
        SurfaceView(context, attrs)
        holder.addCallback(this)
    }

    val mainGameFragment = MainGameFragment()
    val fieldWidth = mainGameFragment.rectWidth
    val fieldHeight = mainGameFragment.rectHeight

    var touchX: Float = 0.0f //x-place of touch
    var touchY: Float = 0.0f //y-place of touch
    var canvasWidth = 0 //of Canvas
    var canvasHeight = 0 //of Canvas
    var cellSize = 0 //cell size
    var fieldStartX = 0
    var fieldStartY = 0
    var fieldEndX = 0
    var fieldEndY = 0
    //var draw = false

    fun mainChecker() {
        //
    }

    override fun onTouchEvent(event: MotionEvent) : Boolean {
        touchX = event.x
        touchY = event.y

        //check players' rectangles
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