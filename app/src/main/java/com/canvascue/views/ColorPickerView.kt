package com.canvascue.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.canvascue.R

class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var colors: List<Int> = emptyList()
    private var selectedColorIndex: Int = -1
    private var onColorSelectedListener: ((Int) -> Unit)? = null
    private var itemSize: Float = 0f
    private var spacing: Float = resources.getDimension(R.dimen.spacing_small)

    init {
        paint.style = Paint.Style.FILL
    }

    fun setColors(newColors: List<Int>) {
        colors = newColors
        requestLayout()
        invalidate()
    }

    fun setOnColorSelectedListener(listener: (Int) -> Unit) {
        onColorSelectedListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        itemSize = (width - (colors.size + 1) * spacing) / colors.size
        val height = itemSize + 2 * spacing

        setMeasuredDimension(width, height.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var x = spacing
        colors.forEachIndexed { index, color ->
            paint.color = color
            canvas.drawCircle(
                x + itemSize / 2,
                height / 2f,
                if (index == selectedColorIndex) itemSize / 2 else itemSize / 2.5f,
                paint
            )
            x += itemSize + spacing
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val index = getTouchedColorIndex(event.x)
                if (index != -1) {
                    selectedColorIndex = index
                    onColorSelectedListener?.invoke(colors[index])
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getTouchedColorIndex(x: Float): Int {
        colors.indices.forEach { index ->
            val startX = spacing + index * (itemSize + spacing)
            val endX = startX + itemSize
            if (x in startX..endX) {
                return index
            }
        }
        return -1
    }
}