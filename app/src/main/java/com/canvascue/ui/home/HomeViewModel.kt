package com.canvascue.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canvascue.R
import com.canvascue.data.ProjectRepository
import com.canvascue.models.PaintProject
import com.canvascue.models.Tip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            projectRepository.getAllProjects()
                .catch { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown error")
                }
                .collect { projects ->
                    _uiState.value = HomeUiState.Success(
                        recentWorks = projects,
                        tips = getTips(),
                        projectCount = projects.size,
                        totalHours = calculateTotalHours(projects)
                    )
                }
        }
    }

    private fun getTips(): List<Tip> {
        return listOf(
            Tip(
                id = 1,
                title = "Color Selection",
                description = "Choose colors that complement each other for better results",
                iconResId = R.drawable.ic_tip_color
            ),
            Tip(
                id = 2,
                title = "Brush Techniques",
                description = "Use smooth strokes for better line quality",
                iconResId = R.drawable.ic_tip_brush
            )
        )
    }

    private fun calculateTotalHours(projects: List<PaintProject>): Int {
        return projects.sumOf { it.duration ?: 0 } // Specify Int explicitly
    }
}

// Replace HomeUiState class with:
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val recentWorks: List<PaintProject>,
        val tips: List<Tip>,
        val projectCount: Int,
        val totalHours: Int
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}