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
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectHeight
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.rectWidth

class MySurfaceView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs),
    SurfaceHolder.Callback {
    init {
        SurfaceView(context, attrs)
        holder.addCallback(this)
    }

    private var canDraw = false
    private var notSpaceCanDraw = true
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
        var isEndGame = MainGameActivity().isEndGame
        var isRunning = true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        touchX = ev.x
        touchY = ev.y
        canDraw = notSpaceCanDraw

//        when(ev.actionMasked) {
//            MotionEvent.ACTION_DOWN -> {
//                rectLocationChecker(rectDrawNow, true)
//            }
////            MotionEvent.ACTION_MOVE -> {
////                rectLocationChecker(rectDrawNow)
////            }
////            MotionEvent.ACTION_UP -> {
////                rectLocationChecker(rectDrawNow)
////            }
//            MotionEvent.ACTION_POINTER_DOWN -> {
//                rectLocationChecker(rectDrawNow, true)
//            }
//            MotionEvent.ACTION_POINTER_UP -> {
//                rectLocationChecker(rectDrawNow, true)
//            }
//        }
//
//        rectLocationChecker(rectDrawNow, true)

//        if (isEndGame) {
//            canDraw = notSpaceCanDraw
//            playersResults = totalPlayersArea()
//            val listener = MainGameActivity() as OnFragmentListener?
//            listener?.onButtonSelected(R.id.mySurfaceView)// id of this surfaceView
//        }

        return true
    }

    fun corners(list: List<RectF>): List<PointF> {// selects all convex and concave corners
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

    fun rectLocationChecker(rectF: RectF, isShowExceptionHint: Boolean): Boolean {
        mainGameFragment.apply {
//            if (isEndGame) {
//                exceptionTVNotFreeSpace()
//                return true
//            }
            val myActivity = activity ?: throw IllegalStateException("Activity cannot be null")
            if (rectF.isEmpty) {
                if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVNoRectangle() }
            } else if (rectF.left < 0 || rectF.top < 0 ||
                rectF.right > fieldWidth * cellSize || rectF.bottom > fieldHeight * cellSize
            ) {
                if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVOutOfBounds() }
            } else if (playersRectangles[playerNumber] == null) {// first rectangle must be in the player's corner
                when (playerNumber) {
                    1 -> if (rectF.left != 0f || rectF.top != 0f) {
                        if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVLocationFirstRect() }
                    } else {
                        if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVNoException() }
                        return true
                    }
                    2 -> if (rectF.right != fieldWidth * cellSize || rectF.bottom != fieldHeight * cellSize) {
                        if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVLocationFirstRect() }
                    } else {
                        if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVNoException() }
                        return true
                    }
                    3 -> if (rectF.right != fieldWidth * cellSize || rectF.top != 0f) {
                        if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVLocationFirstRect() }
                    } else {
                        if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVNoException() }
                        return true
                    }
                    4 -> if (rectF.left != 0f || rectF.bottom != fieldHeight * cellSize) {
                        if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVLocationFirstRect() }
                    } else {
                        if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVNoException() }
                        return true
                    }
                }
            } else {
                for (i in 1..playerCount)
                    for (oldRect in playersRectangles[i]!!)
                        if (RectF.intersects(oldRect, rectF)) {// if intersect, not touch
                            if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVLocation() }
                            return false
                        }
                val corns = corners(playersRectangles[playerNumber]!!)
                for (oldRect in playersRectangles[playerNumber]!!) {
                    if (oldRect.left == rectF.right || oldRect.right == rectF.left) {
                        val points =
                            if (oldRect.left == rectF.right) corns
                                .filter { it.x == oldRect.left }// number of points is always even
                                .sortedBy { it.y }
                            else corns
                                .filter { it.x == oldRect.right }// number of points is always even
                                .sortedBy { it.y }
                        for (i in 0..points.lastIndex step 2) {// take every 2 points = continuous line
                            if ((points[i].y <= rectF.top && points[i + 1].y >= rectF.bottom) ||
                                (points[i].y >= rectF.top && points[i + 1].y <= rectF.bottom)) {
                                if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVNoException() }
                                return true
                            }
                        }
                    }
                    if (oldRect.top == rectF.bottom || oldRect.bottom == rectF.top) {
                        val points = if (oldRect.top == rectF.bottom)
                            corns.filter { it.y == oldRect.top }.sortedBy { it.x }
                        else corns.filter { it.y == oldRect.bottom }.sortedBy { it.x }
                        for (i in 0..points.lastIndex step 2) {// take every 2 points = continuous line
                            if ((points[i].x <= rectF.left && points[i + 1].x >= rectF.right) ||
                                (points[i].x >= rectF.left && points[i + 1].x <= rectF.right)) {
                                if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVNoException() }
                                return true
                            }
                        }
                    }
                }
                if (isShowExceptionHint) myActivity.runOnUiThread { exceptionTVLocation() }// if there was no "return" in cycles in this "else"
            }
        }
        return false// after the first three "if", else never
    }

    private fun isFreeSpace(): Boolean {
        if (playersRectangles[1] == null || playersRectangles[2] == null ||
            playersRectangles[3] == null || playersRectangles[4] == null) { return true }
        for (x in 0..((fieldWidth - minOf(rectWidth, rectHeight)) * cellSize).toInt() step cellSize.toInt()) {
            for (y in 0..((fieldHeight - minOf(rectWidth, rectHeight)) * cellSize).toInt() step cellSize.toInt()) {
                if (rectLocationChecker(RectF(
                        x.toFloat(), y.toFloat(),
                        x + rectWidth * cellSize, y + rectHeight * cellSize
                    ), false) ||
                    rectLocationChecker(RectF(
                        x.toFloat(), y.toFloat(),
                        x + rectHeight * cellSize, y + rectWidth * cellSize
                    ), false)
                ) return true
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

    inner class DrawThread(private val surfaceHolder: SurfaceHolder) : Thread() {
        private val p = Paint()

        override fun run() {
            isRunning = true
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
                        notSpaceCanDraw = false
                        canDraw = notSpaceCanDraw
                        //isRunning = false
                        mainGameFragment.apply {
                            activity?.runOnUiThread {
                                exceptionTVNotFreeSpace()
                            }
                        }
                    } else {
                        notSpaceCanDraw = true
                        canDraw = notSpaceCanDraw
                    }

                    drawPlayerRectNow(canvas)

                    rectLocationChecker(rectDrawNow, true)

                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
//            if (isEndGame) {
//                playersResults = totalPlayersArea()
//                val listener = MainGameActivity() as OnFragmentListener?
//                listener?.onButtonSelected(R.id.mySurfaceView)// id of this surfaceView
//            }
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
    }

    private lateinit var dt: DrawThread

    override fun surfaceCreated(holder: SurfaceHolder) {
        dt = DrawThread(holder)
        dt.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        isRunning = false
        dt = DrawThread(holder)
        dt.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isRunning = false
    }
}