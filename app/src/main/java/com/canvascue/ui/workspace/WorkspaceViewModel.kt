package com.canvascue.ui.workspace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canvascue.data.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkspaceViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _events = MutableStateFlow<WorkspaceEvent?>(null)
    val events: StateFlow<WorkspaceEvent?> = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var selectedImageUri: String? = null
    private var quality: String = "simple"

    fun setSelectedImage(uri: String) {
        selectedImageUri = uri
    }

    fun setQuality(newQuality: String) {
        quality = newQuality
    }

    fun createProject() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                selectedImageUri?.let { uri ->
                    val projectId = projectRepository.createProject(uri)
                    _events.value = WorkspaceEvent.NavigateToEditor(projectId)
                } ?: run {
                    _events.value = WorkspaceEvent.ShowError("Please select an image first")
                }
            } catch (e: Exception) {
                _events.value = WorkspaceEvent.ShowError(e.message ?: "Unknown error occurred")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearEvent() {
        _events.value = null
    }
}