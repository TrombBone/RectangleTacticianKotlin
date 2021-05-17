package com.example.rectangletacticiankotlin

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.fieldHeight
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.fieldWidth
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playerCount
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playerNumber
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playersRectangles
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playersResults
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectHeight
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectWidth

class MySurfaceView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs),
    SurfaceHolder.Callback {
    init {
        SurfaceView(context, attrs)
        holder.addCallback(this)
    }

    private var isEndGame = false
    private var canDraw = false
    private var touchX = 0f
    private var touchY = 0f
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var cellSize = 0f

    //for scale:
//    private var fieldStartX = 0f
//    private var fieldStartY = 0f
//    private var fieldEndX = 0f
//    private var fieldEndY = 0f

    lateinit var mainGameFragment: MainGameFragment

    companion object {
        var rectDrawNow = RectF()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        touchX = ev.x
        touchY = ev.y
        canDraw = true

        if (isEndGame) {
            canDraw = false
            playersResults = dt.totalPlayersArea()
            val listener = MainGameActivity() as OnFragmentListener?
            listener?.onButtonSelected(R.id.zoom_Surface_View)// id of this surfaceView
        }
/*
        // событие
        val actionMask: Int = ev.actionMasked
        // индекс касания
        val pointerIndex: Int = ev.actionIndex
        // число касаний
        val pointerCount: Int = ev.pointerCount

        when (actionMask) {
            MotionEvent.ACTION_DOWN -> {
                //
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mainGameFragment.surfaceView.addCallback(object : Callback {
                    override fun onZoomSurfaceCreated(view: ZoomSurfaceView) {
                        val surface: Surface? = view.surface
                        view.engine.zoomTo(2f, true)
                        // Use this surface for video players, camera preview, ...
                    }

                    override fun onZoomSurfaceDestroyed(view: ZoomSurfaceView) {}
                })
            }
            //MotionEvent.ACTION_POINTER_UP -> upPI = pointerIndex
            MotionEvent.ACTION_MOVE -> {
                //
            }
        }
 */
        return true
    }

    inner class DrawThread(private val surfaceHolder: SurfaceHolder) : Thread() {
        var isRunning = true
        private val p = Paint()

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

                    if (!isFreeSpace()) {
                        isEndGame = true
                        canDraw = false
                        isRunning = false
                        sleep(3000)
                        playersResults = totalPlayersArea()
                        val listener = MainGameActivity() as OnFragmentListener?
                        listener?.onButtonSelected(R.id.zoom_Surface_View)// id of this surfaceView
                    }

                    drawPlayerRectNow(canvas)
                    rectLocationChecker(rectDrawNow)

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

        private fun corners(list: List<RectF>): List<PointF> {// selects all convex and concave corners
            val result = mutableListOf<PointF>()
            for (rect in list) {
                val pointsRect = listOf(
                    PointF(rect.left, rect.top),
                    PointF(rect.right, rect.top),
                    PointF(rect.right, rect.bottom),
                    PointF(rect.left, rect.bottom)
                )
                for (rectPoint in pointsRect)
                    if (rectPoint != PointF(0f, 0f) &&
                        rectPoint != PointF(cellSize * fieldWidth, 0f) &&
                        rectPoint != PointF(cellSize * fieldWidth, cellSize * fieldHeight) &&
                        rectPoint != PointF(0f, cellSize * fieldHeight)
                        && !result.contains(rectPoint)
                    ) result.add(rectPoint)
                    else if (result.contains(rectPoint)) result.remove(rectPoint)
            }
            return result
        }

        private fun rectLocationChecker(rectF: RectF): Boolean {
            mainGameFragment.apply {
                if (rectF.isEmpty) exceptionTVNoRectangle()
                else if (rectF.left < 0 || rectF.top < 0 ||
                    rectF.right > fieldWidth * cellSize || rectF.bottom > fieldHeight * cellSize
                ) exceptionTVOutOfBounds()
                else if(playersRectangles[playerNumber] == null) {// first rectangle must be in the player's corner
                    when (playerNumber) {
                        1 -> if (rectF.left != 0f && rectF.top != 0f) exceptionTVLocation()
                        2 -> if (rectF.right != fieldWidth * cellSize && rectF.bottom != fieldHeight * cellSize) exceptionTVLocation()
                        3 -> if (rectF.right != fieldWidth * cellSize && rectF.top != 0f) exceptionTVLocation()
                        4 -> if (rectF.left != 0f && rectF.top != fieldHeight * cellSize) exceptionTVLocation()
                    }
                } else {
                    for (i in 1..playerCount)
                        for (oldRect in playersRectangles[i]!!)
                            if (RectF.intersects(oldRect, rectF)) {// if intersect, not touch
                                exceptionTVLocation()
                                return false
                            }
                    for (oldRect in playersRectangles[playerNumber]!!) {
                        if (oldRect.left == rectF.right || oldRect.right == rectF.left) {
                            val points =
                                if (oldRect.left == rectF.right) corners(playersRectangles[playerNumber]!!)
                                    .filter { it.x == oldRect.left }// number of points is always even
                                    .sortedBy { it.y }
                                else corners(playersRectangles[playerNumber]!!)
                                    .filter { it.x == oldRect.right }// number of points is always even
                                    .sortedBy { it.y }
                            for (i in 0..points.lastIndex step 2) {// take every 2 points = continuous line
                                if ((points[i].y <= rectF.top && points[i + 1].y >= rectF.bottom) ||
                                    (points[i].y >= rectF.top && points[i + 1].y <= rectF.bottom)) {
                                    exceptionTVNoException()
                                    return true
                                }
                            }
                        }
                        if (oldRect.top == rectF.bottom || oldRect.bottom == rectF.top) {
                            val points = if (oldRect.top == rectF.bottom)
                                corners(playersRectangles[playerNumber]!!)
                                    .filter { it.y == oldRect.top }.sortedBy { it.x }
                            else corners(playersRectangles[playerNumber]!!)
                                .filter { it.y == oldRect.bottom }.sortedBy { it.x }
                            for (i in 0..points.lastIndex step 2) {// take every 2 points = continuous line
                                if ((points[i].x <= rectF.left && points[i + 1].x >= rectF.right) ||
                                    (points[i].x >= rectF.left && points[i + 1].x <= rectF.right)) {
                                    exceptionTVNoException()
                                    return true
                                }
                            }
                        }
                    }
                    exceptionTVLocation()// if there was no "return" in cycles in this "else"
                }
            }
            return false// after the first three "if", else never
        }

        private fun isFreeSpace(): Boolean {
            for (x in 0..fieldWidth - minOf(rectWidth, rectHeight)) {
                for (y in 0..fieldHeight - minOf(rectWidth, rectHeight)) {
                    if (rectLocationChecker(RectF(
                            x.toFloat(), y.toFloat(),
                            (x + rectWidth).toFloat(), (y + rectHeight).toFloat()
                        )) || rectLocationChecker(RectF(
                            x.toFloat(), y.toFloat(),
                            (x + rectHeight).toFloat(), (y + rectWidth).toFloat()
                        ))
                    ) {
                        return true
                    }
                }
            }
            return false// if there was no "return" in cycle
        }

        fun totalPlayersArea(): Map<Int, Int> {
            val playersArea = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0)
            for (player in 1..playerCount)
                for (rect in playersRectangles[player]!!) playersArea[player] =
                    playersArea[player]!! + ((rect.bottom - rect.top) / cellSize * ((rect.right - rect.left) / cellSize)).toInt()
            return playersArea
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