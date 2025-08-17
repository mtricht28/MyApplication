package com.example.myapplication

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [MainActivity]'s internal logic.
 */
class MainActivityTest {
    private val activity = MainActivity()

    @Test
    fun faceResourcesMatchIds() {
        val method = MainActivity::class.java.getDeclaredMethod("getFaceResource", Int::class.javaPrimitiveType)
        method.isAccessible = true

        assertEquals(R.drawable.card_0, method.invoke(activity, 0) as Int)
        assertEquals(R.drawable.card_1, method.invoke(activity, 1) as Int)
        assertEquals(R.drawable.card_2, method.invoke(activity, 2) as Int)
        assertEquals(R.drawable.card_3, method.invoke(activity, 3) as Int)
        assertEquals(R.drawable.joker, method.invoke(activity, 4) as Int)
    }
}

