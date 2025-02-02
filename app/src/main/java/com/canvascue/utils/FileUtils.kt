package com.canvascue.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object FileUtils {
    private const val TEMP_DIR = "temp_images"
    private const val PROJECTS_DIR = "projects"

    fun saveBitmapToFile(context: Context, bitmap: Bitmap, directory: String = TEMP_DIR): Uri {
        val dir = File(context.filesDir, directory).apply {
            if (!exists()) mkdirs()
        }

        val file = File(dir, "${UUID.randomUUID()}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        return Uri.fromFile(file)
    }

    fun clearTempFiles(context: Context) {
        File(context.filesDir, TEMP_DIR).deleteRecursively()
    }

    fun getProjectDirectory(context: Context): File {
        return File(context.filesDir, PROJECTS_DIR).apply {
            if (!exists()) mkdirs()
        }
    }

    fun deleteProject(context: Context, projectId: Long) {
        File(getProjectDirectory(context), projectId.toString()).deleteRecursively()
    }
}