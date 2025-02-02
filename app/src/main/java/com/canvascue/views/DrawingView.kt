package com.canvascue.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withSave
import kotlin.math.abs

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val drawingPath = Path()
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = 4f

    private var currentColor: Int = Color.BLACK
    private var brushSize: Float = 20f
    private var isErasing = false

    private val pathList = mutableListOf<PathProperties>()
    private val redoList = mutableListOf<PathProperties>()

    private var baseImage: Bitmap? = null
    private var overlayImage: Bitmap? = null

    fun setBaseImage(bitmap: Bitmap) {
        baseImage = bitmap
        invalidate()
    }

    fun setOverlayImage(bitmap: Bitmap) {
        overlayImage = bitmap
        invalidate()
    }

    fun setBrushSize(size: Float) {
        brushSize = size
        paint.strokeWidth = size
    }

    fun setColor(color: Int) {
        currentColor = color
        isErasing = false
        paint.color = color
        paint.xfermode = null
    }

    fun enableBrush() {
        isErasing = false
        paint.color = currentColor
        paint.xfermode = null
        paint.strokeWidth = brushSize
    }

    fun enableEraser() {
        isErasing = true
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun undo() {
        if (pathList.isNotEmpty()) {
            redoList.add(pathList.removeAt(pathList.lastIndex))
            invalidate()
        }
    }

    fun redo() {
        if (redoList.isNotEmpty()) {
            pathList.add(redoList.removeAt(redoList.lastIndex))
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw base image
        baseImage?.let { canvas.drawBitmap(it, 0f, 0f, null) }

        // Draw overlay image with reduced alpha
        overlayImage?.let {
            paint.alpha = 128
            canvas.drawBitmap(it, 0f, 0f, paint)
            paint.alpha = 255
        }

        // Draw all paths
        for (pathProp in pathList) {
            paint.color = pathProp.color
            paint.strokeWidth = pathProp.brushSize
            paint.xfermode = if (pathProp.isEraser) {
                PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            } else null
            canvas.drawPath(pathProp.path, paint)
        }

        // Draw current path
        paint.color = currentColor
        paint.strokeWidth = brushSize
        paint.xfermode = if (isErasing) {
            PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else null
        canvas.drawPath(drawingPath, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    private fun touchStart(x: Float, y: Float) {
        drawingPath.reset()
        drawingPath.moveTo(x, y)
        currentX = x
        currentY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - currentX)
        val dy = abs(y - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            drawingPath.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)
            currentX = x
            currentY = y
        }
    }

    private fun touchUp() {
        drawingPath.lineTo(currentX, currentY)
        // Save the path
        pathList.add(PathProperties(
            Path(drawingPath),
            currentColor,
            brushSize,
            isErasing
        ))
        // Reset the path for next draw
        drawingPath.reset()
        // Clear redo list as new path is drawn
        redoList.clear()
    }

    private data class PathProperties(
        val path: Path,
        val color: Int,
        val brushSize: Float,
        val isEraser: Boolean
    )
}