package com.canvascue.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.canvascue.R
import com.canvascue.databinding.FragmentEditorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditorFragment : Fragment() {
    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditorViewModel by viewModels()
    private val args: EditorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = android.R.id.content
            duration = 300L
            scrimColor = android.graphics.Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeState()
        viewModel.loadProject(args.projectId)
    }

    private fun setupUI() = with(binding) {
        toolbar.setNavigationOnClickListener {
            confirmExit()
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_save -> {
                    viewModel.saveProject()
                    true
                }
                R.id.menu_settings -> {
                    showSettings()
                    true
                }
                R.id.menu_help -> {
                    showHelp()
                    true
                }
                else -> false
            }
        }

        colorPalette.setOnColorSelectedListener { color ->
            drawingView.setColor(color)
        }

        btnUndo.setOnClickListener { drawingView.undo() }
        btnRedo.setOnClickListener { drawingView.redo() }
        btnEraser.setOnClickListener { drawingView.enableEraser() }
        btnBrush.setOnClickListener { drawingView.enableBrush() }
        btnSave.setOnClickListener { viewModel.saveProject() }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: EditorUiState) = with(binding) {
        when (state) {
            EditorUiState.Loading -> {
                progressIndicator.visibility = View.VISIBLE
                contentGroup.visibility = View.INVISIBLE
            }
            is EditorUiState.Success -> {
                progressIndicator.visibility = View.GONE
                contentGroup.visibility = View.VISIBLE
                drawingView.setBaseImage(state.baseImage)
                drawingView.setOverlayImage(state.overlayImage)
                colorPalette.setColors(state.colorPalette)
                toolbar.title = state.projectName
            }
            is EditorUiState.Error -> {
                progressIndicator.visibility = View.GONE
                showError(state.message)
            }
            EditorUiState.Saved -> {
                Snackbar.make(root, R.string.save_success, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun confirmExit() {
        if (viewModel.hasUnsavedChanges()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.unsaved_changes)
                .setMessage(R.string.unsaved_changes_message)
                .setPositiveButton(R.string.save) { dialog, _ ->
                    viewModel.saveProject()
                    findNavController().navigateUp()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.discard) { dialog, _ ->
                    findNavController().navigateUp()
                    dialog.dismiss()
                }
                .setNeutralButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showSettings() {
        // TODO: Implement settings dialog
    }

    private fun showHelp() {
        // TODO: Implement help dialog
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}