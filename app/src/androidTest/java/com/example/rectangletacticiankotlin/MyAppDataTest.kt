package com.example.rectangletacticiankotlin

import android.graphics.PointF
import android.graphics.RectF
import org.junit.Assert.assertEquals
import org.junit.Test

class MyAppDataTest {

    @Test
    fun corners() {
        val myAppData = MyAppDataStub()
        val rectangles = listOf(
            RectF(0f, 0f, 35f, 35f),
            RectF(0f, 35f, 35f, 50f),
            RectF(35f, 0f, 50f, 50f),
        )
        // Method equals in android.graphics.PointF not mocked
        // Вызывается в оригинальной функции
        val result = myAppData.corners(rectangles)

        assertEquals(
            listOf(
                PointF(0f, 50f), PointF(50f, 0f), PointF(50f, 50f),
            ), result
        )
    }
}