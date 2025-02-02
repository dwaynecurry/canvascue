package com.canvascue.models

data class ColorPalette(
    val id: Int = 0,
    val colors: List<Int>,
    val name: String = "",
    val isCustom: Boolean = false
)