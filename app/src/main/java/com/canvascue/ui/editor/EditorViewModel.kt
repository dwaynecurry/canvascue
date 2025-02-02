package com.canvascue.ui.editor

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canvascue.data.ProjectRepository
import com.canvascue.models.PaintProject
import com.canvascue.utils.ImageProcessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val imageProcessor: ImageProcessor
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditorUiState>(EditorUiState.Loading)
    val uiState: StateFlow<EditorUiState> = _uiState

    private var currentProject: PaintProject? = null
    private var currentDrawing: Bitmap? = null
    private var hasChanges = false

    fun loadProject(projectId: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = EditorUiState.Loading
                val project = projectRepository.getProjectById(projectId)
                project?.let {
                    currentProject = it
                    val baseImage = projectRepository.getBaseImage(it)
                    val overlayImage = projectRepository.getOverlayImage(it)
                    val colorPalette = imageProcessor.generateColorPalette(baseImage)

                    _uiState.value = EditorUiState.Success(
                        projectName = it.name,
                        baseImage = baseImage,
                        overlayImage = overlayImage,
                        colorPalette = colorPalette
                    )
                } ?: run {
                    _uiState.value = EditorUiState.Error("Project not found")
                }
            } catch (e: Exception) {
                _uiState.value = EditorUiState.Error("Failed to load project: ${e.message}")
            }
        }
    }

    fun saveProject() {
        viewModelScope.launch {
            try {
                currentProject?.let { project ->
                    currentDrawing?.let { drawing ->
                        projectRepository.saveProjectProgress(project.id, drawing)
                        updateProjectProgress(calculateProgress())
                        hasChanges = false
                        _uiState.value = EditorUiState.Saved
                    }
                }
            } catch (e: Exception) {
                _uiState.value = EditorUiState.Error("Failed to save project: ${e.message}")
            }
        }
    }

    fun updateDrawing(bitmap: Bitmap) {
        currentDrawing = bitmap
        hasChanges = true
        updateProjectProgress(calculateProgress())
    }

    private fun updateProjectProgress(progress: Float) {
        viewModelScope.launch {
            currentProject?.let { project ->
                projectRepository.updateProjectProgress(project.id, progress)
            }
        }
    }

    private fun calculateProgress(): Float {
        return currentProject?.progress ?: 0f
    }

    fun hasUnsavedChanges(): Boolean = hasChanges

    override fun onCleared() {
        super.onCleared()
        currentDrawing?.recycle()
        currentDrawing = null
    }
}

sealed class EditorUiState {
    object Loading : EditorUiState()
    data class Success(
        val projectName: String,
        val baseImage: Bitmap,
        val overlayImage: Bitmap,
        val colorPalette: List<Int>
    ) : EditorUiState()
    data class Error(val message: String) : EditorUiState()
    object Saved : EditorUiState()
}