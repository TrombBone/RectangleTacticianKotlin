package com.example.rectangletacticiankotlin

import android.graphics.PointF
import android.graphics.RectF
import kotlin.random.Random

open class MyAppData(var playerCount: Int, var fieldWidth: Int, var fieldHeight: Int) {

    var playerNumber = 1

    var cellSize = 0f
    private var rectDrawNow = RectF()
    val playersRectangles = mutableMapOf<Int, MutableList<RectF>>()

    var isRunning = true
    var isEndGame = false
//    var lastTouch = false

    lateinit var buttonListener: OnFragmentListener
    lateinit var exceptionListener: OnExceptionHintListener

    private var rectWidth = 0
    private var rectHeight = 0

    fun setRectDrawNowSizes() {
        this.rectWidth = Random.nextInt(1, 7)
        this.rectHeight = Random.nextInt(1, 7)
    }
    fun getRectDrawNowSizes() = Pair(this.rectWidth, this.rectHeight)

    fun nextPlayer() {
        if (playerNumber == playerCount) playerNumber = 1 else playerNumber++
    }

    fun nextTurn() {
        playersRectangles.getOrPut(playerNumber, { mutableListOf() }).add(rectDrawNow)
        if (isEndGame && playerNumber == playerCount) {
//            lastTouch = true
            buttonListener.onButtonSelected(R.id.mySurfaceView)// id of this surfaceView
        } else buttonListener.onButtonSelected(R.id.nextTurnButton)// next turn
        isRunning = false
    }

    fun rotateRect() {
        rectWidth += rectHeight
        rectHeight = rectWidth - rectHeight
        rectWidth -= rectHeight
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

    private fun checkFirstRect(conditional: Boolean, isShowExceptionHint: Boolean): Boolean {
        exceptionListener.apply {
            if (conditional) {
                if (isShowExceptionHint) onExceptionSelected(TV_BAD_LOCATION_FIRST_RECT)
            } else {
                if (isShowExceptionHint) { onExceptionSelected(TV_GOOD_NO_EXCEPTION) }
                return true
            }
        }
        return false
    }


    private fun rectLocationChecker(rectF: RectF, isShowExceptionHint: Boolean): Boolean {
        rectDrawNow = rectF
        exceptionListener.apply {
            if (rectF.isEmpty) {
                if (isShowExceptionHint) onExceptionSelected(TV_BAD_NO_RECT)
            } else if (rectF.left < 0 || rectF.top < 0 ||
                rectF.right > fieldWidth * cellSize || rectF.bottom > fieldHeight * cellSize
            ) {
                if (isShowExceptionHint) onExceptionSelected(TV_BAD_OUT_OF_BOUNDS)
            } else if (playersRectangles[playerNumber] == null) {
                when (playerNumber) {
                    1 -> if (checkFirstRect(
                            rectF.left != 0f || rectF.top != 0f,
                            isShowExceptionHint
                        )
                    ) return true
                    2 -> if (checkFirstRect(
                            rectF.right != fieldWidth * cellSize || rectF.bottom != fieldHeight * cellSize,
                            isShowExceptionHint
                        )
                    ) return true
                    3 -> if (checkFirstRect(
                            rectF.right != fieldWidth * cellSize || rectF.top != 0f,
                            isShowExceptionHint
                        )
                    ) return true
                    4 -> if (checkFirstRect(
                            rectF.left != 0f || rectF.bottom != fieldHeight * cellSize,
                            isShowExceptionHint
                        )
                    ) return true
                }
            } else {
                for (i in 1..playerCount)
                    for (oldRect in playersRectangles[i]!!)
                        if (RectF.intersects(oldRect, rectF)) {// if intersect, not touch
                            if (isShowExceptionHint) onExceptionSelected(TV_BAD_LOCATION)
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
                                if (isShowExceptionHint) onExceptionSelected(TV_GOOD_NO_EXCEPTION)
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
                                if (isShowExceptionHint) onExceptionSelected(TV_GOOD_NO_EXCEPTION)
                                return true
                            }
                        }
                    }
                }
                if (isShowExceptionHint) onExceptionSelected(TV_BAD_LOCATION)// if there was no "return" in cycles in this "else"
            }
        }
        return false// after the first three "if", else never
    }

    fun rectLocationChecker(rectF: RectF) = rectLocationChecker(rectF, true)

    fun isFreeSpace(): Boolean {
        if (playersRectangles[1] == null || playersRectangles[2] == null ||
            playersRectangles[3] == null || playersRectangles[4] == null) { return true }
        for (x in 0..((fieldWidth - minOf(rectWidth, rectHeight)) * cellSize).toInt() step cellSize.toInt()) {
            for (y in 0..((fieldHeight - minOf(rectWidth, rectHeight)) * cellSize).toInt() step cellSize.toInt()) {
                if (rectLocationChecker(
                        RectF(
                            x.toFloat(), y.toFloat(),
                            x + rectWidth * cellSize, y + rectHeight * cellSize
                        ),
                        false
                    ) ||
                    rectLocationChecker(
                        RectF(
                            x.toFloat(), y.toFloat(),
                            x + rectHeight * cellSize, y + rectWidth * cellSize
                        ),
                        false
                    )
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
}