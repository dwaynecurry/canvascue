package com.canvascue.ui.workspace

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.canvascue.R
import com.canvascue.databinding.FragmentWorkspaceBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkspaceFragment : Fragment() {
    private var _binding: FragmentWorkspaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WorkspaceViewModel by viewModels()

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { handleSelectedImage(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkspaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() = with(binding) {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        uploadButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        startButton.setOnClickListener {
            viewModel.createProject()
        }

        qualityGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.qualitySimple -> viewModel.setQuality("simple")
                    R.id.qualityDetailed -> viewModel.setQuality("detailed")
                }
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    handleEvent(event)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun handleEvent(event: WorkspaceEvent?) {
        when (event) {
            is WorkspaceEvent.NavigateToEditor -> {
                val action = WorkspaceFragmentDirections.actionWorkspaceToEditor(event.projectId)
                findNavController().navigate(action)
            }
            is WorkspaceEvent.ShowError -> {
                Snackbar.make(
                    binding.root,
                    event.message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
            null -> { /* Handle null case */ }
        }

    }

    private fun handleSelectedImage(uri: Uri) {
        viewModel.setSelectedImage(uri.toString())
        binding.apply {
            previewCard.visibility = View.VISIBLE
            previewImage.setImageURI(uri)
            startButton.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}