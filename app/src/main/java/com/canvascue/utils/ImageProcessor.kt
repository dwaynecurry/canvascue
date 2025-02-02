package com.canvascue.utils

import android.graphics.*
import android.net.Uri
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

class ImageProcessor(private val context: Context) {

    suspend fun processImage(
        imageUri: Uri,
        colorCount: Int = 32,
        maxSize: Int = 2048
    ): ProcessedImage = withContext(Dispatchers.IO) {
        // Load and resize the image
        val originalBitmap = loadAndResizeBitmap(imageUri, maxSize)

        // Create color palette
        val palette = ColorUtils.createPalette(colorCount)

        // Process the image
        val processedBitmap = createPaintByNumbers(originalBitmap, palette)

        // Save processed image
        val processedUri = FileUtils.saveBitmapToFile(
            context,
            processedBitmap,
            "processed_images"
        )

        ProcessedImage(
            originalBitmap = originalBitmap,
            processedBitmap = processedBitmap,
            processedUri = processedUri,
            palette = palette
        )
    }

    private fun loadAndResizeBitmap(uri: Uri, maxSize: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        context.contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it, null, options)
        }

        val scale = min(
            maxSize.toFloat() / options.outWidth,
            maxSize.toFloat() / options.outHeight
        )

        options.apply {
            inJustDecodeBounds = false
            if (scale < 1) {
                inSampleSize = (1 / scale).toInt()
            }
        }

        return context.contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it, null, options)
                ?: throw IllegalStateException("Failed to decode bitmap")
        }
    }
    fun generateColorPalette(bitmap: Bitmap): List<Int> {
        // Simple implementation - you can make this more sophisticated
        val colors = mutableSetOf<Int>()
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (pixel in pixels) {
            if (colors.size >= 32) break // Limit palette size
            colors.add(pixel)
        }

        return colors.toList()
    }
    private fun createPaintByNumbers(
        bitmap: Bitmap,
        palette: List<Int>
    ): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        // Process each pixel
        for (i in pixels.indices) {
            pixels[i] = ColorUtils.findClosestColor(pixels[i], palette)
        }

        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }

    data class ProcessedImage(
        val originalBitmap: Bitmap,
        val processedBitmap: Bitmap,
        val processedUri: Uri,
        val palette: List<Int>
    )
}