package com.canvascue.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.canvascue.models.PaintProject
import com.canvascue.utils.ImageProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val imageProcessor: ImageProcessor,
    private val context: android.content.Context
) {
    fun getAllProjects(): Flow<List<PaintProject>> = projectDao.getAllProjects()

    suspend fun createProject(uri: String): Long {
        val fileName = File(uri).nameWithoutExtension
        val project = PaintProject(
            imageUri = uri,
            name = "Project ${fileName.take(20)}", // Generate a default name from the file
            progress = 0f,
            createdAt = System.currentTimeMillis()
        )
        return projectDao.insertProject(project)
    }

    suspend fun createProject(uri: String, name: String): Long {
        val project = PaintProject(
            imageUri = uri,
            name = name,
            progress = 0f,
            createdAt = System.currentTimeMillis()
        )
        return projectDao.insertProject(project)
    }

    suspend fun getBaseImage(project: PaintProject): Bitmap = withContext(Dispatchers.IO) {
        val file = File(project.imageUri)
        BitmapFactory.decodeFile(file.absolutePath)
            ?: throw IllegalStateException("Failed to load base image")
    }

    suspend fun getOverlayImage(project: PaintProject): Bitmap = withContext(Dispatchers.IO) {
        val processedImage = imageProcessor.processImage(
            android.net.Uri.fromFile(File(project.imageUri))
        )
        processedImage.processedBitmap
    }

    suspend fun saveProjectProgress(projectId: Long, drawing: Bitmap) = withContext(Dispatchers.IO) {
        val project = getProjectById(projectId) ?: throw IllegalStateException("Project not found")
        val file = File(context.filesDir, "progress_${project.id}.png")
        file.outputStream().use { out ->
            drawing.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }

    suspend fun updateProjectProgress(projectId: Long, progress: Float) {
        val project = getProjectById(projectId) ?: throw IllegalStateException("Project not found")
        projectDao.updateProject(project.copy(progress = progress))
    }

    suspend fun saveProject(project: PaintProject): Long {
        return projectDao.insertProject(project)
    }

    suspend fun updateProject(project: PaintProject) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(project: PaintProject) {
        // Delete associated files
        File(project.imageUri).delete()
        File(context.filesDir, "progress_${project.id}.png").delete()
        projectDao.deleteProject(project)
    }

    suspend fun getProjectById(id: Long): PaintProject? {
        return projectDao.getProjectById(id)
    }
}