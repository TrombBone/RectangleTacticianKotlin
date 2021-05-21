package com.example.rectangletacticiankotlin

import android.graphics.RectF
import org.junit.Assert.assertEquals
import org.junit.Test

class MyAppDataTest {

    @Test
    fun corners() {
        val myAppData = MyAppData()
        val points = listOf(
            RectF(0f, 0f, 35f, 35f),
            RectF(0f, 0f, 35f, 35f),
            RectF(10f, 10f, 50f, 50f),
            RectF(0f, 0f, 35f, 35f),
            RectF(3f, 0f, 3f, 35f),
            RectF(3f, 0f, 3f, 35f),
            RectF(1f, 1f, 5f, 5f)
        )
        // Method equals in android.graphics.PointF not mocked
        // Вызывается в оригинальной функции
        val result = myAppData.corners(points)

        assertEquals(listOf(
            RectF(10f, 10f, 50f, 50f),
            RectF(0f, 0f, 35f, 35f),
            RectF(1f, 1f, 5f, 5f)
        ), result)
    }
}