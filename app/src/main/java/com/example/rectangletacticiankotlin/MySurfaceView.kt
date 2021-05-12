package com.example.rectangletacticiankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.fieldHeight
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.fieldWidth
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playerCount
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playerNumber
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playersRectangles
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectHeight
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectWidth
import com.otaliastudios.zoom.ZoomSurfaceView

class MySurfaceView(context: Context, attrs: AttributeSet?) : ZoomSurfaceView(context, attrs),
    SurfaceHolder.Callback {
    init {
        ZoomSurfaceView(context, attrs)
        holder.addCallback(this)
    }

    private var canDraw = false
    private var touchX = 0f
    private var touchY = 0f
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var cellSize = 0f

    //for scale:
    private var fieldStartX = 0f
    private var fieldStartY = 0f
    private var fieldEndX = 0f
    private var fieldEndY = 0f

    lateinit var mainGameFragment: MainGameFragment

    companion object {
        var rectDrawNow = RectF()
    }

    fun mainChecker() {
        if (rectDrawNow.isEmpty) mainGameFragment.exceptionTVNoRectangle()
        else if (rectDrawNow.left < 0 || rectDrawNow.top < 0 || rectDrawNow.right > fieldWidth * cellSize || rectDrawNow.bottom > fieldHeight * cellSize)
            mainGameFragment.exceptionTVOutOfBounds()
//        else if() //main check
//            mainGameFragment.exceptionTVLocation()
        else mainGameFragment.exceptionTVNoException()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        touchX = ev.x
        touchY = ev.y
        canDraw = true

//        // событие
//        val actionMask: Int = ev.actionMasked
//        // индекс касания
//        val pointerIndex: Int = ev.actionIndex
//        // число касаний
//        val pointerCount: Int = ev.pointerCount
//
//        when (actionMask) {
//            MotionEvent.ACTION_DOWN -> {
//                //
//            }
//            MotionEvent.ACTION_POINTER_DOWN -> {
//                mainGameFragment.surfaceView.addCallback(object : Callback {
//                    override fun onZoomSurfaceCreated(view: ZoomSurfaceView) {
//                        val surface: Surface? = view.surface
//                        view.engine.zoomTo(2f, true)
//                        // Use this surface for video players, camera preview, ...
//                    }
//
//                    override fun onZoomSurfaceDestroyed(view: ZoomSurfaceView) {}
//                })
//            }
//            //MotionEvent.ACTION_POINTER_UP -> upPI = pointerIndex
//            MotionEvent.ACTION_MOVE -> {
//                //
//            }
//        }
//        mainChecker()// check players' rectangles
//        when (playerNumber) {
//            1 -> {}
//            2 -> {}
//            3 -> {}
//            4 -> {}
//        }

        return true
    }

    inner class DrawThread(private val surfaceHolder: SurfaceHolder) : Thread() {
        var isRunning = true
        private var p = Paint()

        override fun run() {
            while (isRunning) {
                val canvas: Canvas? = surfaceHolder.lockCanvas(null)
                p.color = Color.BLACK
                p.style = Paint.Style.STROKE// contours
                p.strokeWidth = 1f

                if (canvas != null) {
                    canvas.drawColor(Color.WHITE)

                    canvasWidth = canvas.width
                    canvasHeight = canvas.height
                    cellSize = (canvasWidth / fieldWidth).toFloat()

                    drawMesh(canvas)
                    drawStartPlaces(canvas)


                    drawPlayersRectOld(canvas)
                    drawPlayerRectNow(canvas)
                    if (canDraw) {
                        this@MySurfaceView.apply {
                            zoomTo(realZoom + 100, false)
                        }
                        //canvas.scale(50f, 50f)
                    }

                    mainChecker()

                    surfaceHolder.unlockCanvasAndPost(canvas)
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