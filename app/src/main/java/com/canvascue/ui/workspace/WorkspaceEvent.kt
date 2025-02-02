package com.canvascue.ui.workspace

sealed class WorkspaceEvent {
    data class NavigateToEditor(val projectId: Long) : WorkspaceEvent()
    data class ShowError(val message: String) : WorkspaceEvent()
}