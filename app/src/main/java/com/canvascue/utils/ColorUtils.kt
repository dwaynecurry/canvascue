package com.canvascue.utils

import android.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

object ColorUtils {
    fun findClosestColor(targetColor: Int, palette: List<Int>): Int {
        if (palette.isEmpty()) return targetColor

        return palette.minByOrNull { referenceColor ->
            colorDistance(targetColor, referenceColor)
        } ?: targetColor
    }

    private fun colorDistance(color1: Int, color2: Int): Double {
        val r1 = Color.red(color1)
        val g1 = Color.green(color1)
        val b1 = Color.blue(color1)

        val r2 = Color.red(color2)
        val g2 = Color.green(color2)
        val b2 = Color.blue(color2)

        val rDiff = (r2 - r1).toDouble()
        val gDiff = (g2 - g1).toDouble()
        val bDiff = (b2 - b1).toDouble()

        return sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff)
    }

    fun createPalette(colorCount: Int = 32): List<Int> {
        val colors = mutableListOf<Int>()

        // Basic colors
        colors.add(Color.BLACK)
        colors.add(Color.WHITE)
        colors.add(Color.RED)
        colors.add(Color.GREEN)
        colors.add(Color.BLUE)
        colors.add(Color.YELLOW)
        colors.add(Color.CYAN)
        colors.add(Color.MAGENTA)

        // Generate additional colors if needed
        while (colors.size < colorCount) {
            val r = (0..255).random()
            val g = (0..255).random()
            val b = (0..255).random()
            colors.add(Color.rgb(r, g, b))
        }

        return colors
    }
}