package com.canvascue.utils

import android.graphics.Color
import kotlin.math.abs

object ColorUtils {
    fun createPalette(colorCount: Int): List<Int> {
        val hueStep = 360f / colorCount
        return List(colorCount) { index ->
            val hue = index * hueStep
            HSLToColor(hue, 0.8f, 0.6f)
        }
    }

    fun findClosestColor(color: Int, palette: List<Int>): Int {
        return palette.minByOrNull { calculateColorDistance(it, color) } ?: color
    }

    private fun calculateColorDistance(color1: Int, color2: Int): Double {
        val r1 = Color.red(color1)
        val g1 = Color.green(color1)
        val b1 = Color.blue(color1)

        val r2 = Color.red(color2)
        val g2 = Color.green(color2)
        val b2 = Color.blue(color2)

        return abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2).toDouble()
    }

    private fun HSLToColor(hue: Float, saturation: Float, lightness: Float): Int {
        val hsl = FloatArray(3)
        hsl[0] = hue
        hsl[1] = saturation
        hsl[2] = lightness
        return Color.HSVToColor(hsl)
    }
}