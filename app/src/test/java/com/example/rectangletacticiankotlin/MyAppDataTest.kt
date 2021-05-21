package com.example.rectangletacticiankotlin

import android.graphics.RectF
import org.junit.Assert.assertEquals
import org.junit.Test

class MyAppDataTest {

    @Test
    fun corners() {
        val points = listOf(
            RectF(0f, 0f, 35f, 35f),
            RectF(0f, 0f, 35f, 35f),
            RectF(10f, 10f, 50f, 50f),
            RectF(0f, 0f, 35f, 35f),
            RectF(3f, 0f, 3f, 35f),
            RectF(3f, 0f, 3f, 35f),
            RectF(1f, 1f, 5f, 5f)
        )
        val result = MyAppData().corners(points)

        assertEquals(result, listOf(
            RectF(10f, 10f, 50f, 50f),
            RectF(0f, 0f, 35f, 35f),
            RectF(1f, 1f, 5f, 5f)
        ))
    }
}