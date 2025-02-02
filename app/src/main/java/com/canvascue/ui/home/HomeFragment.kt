package com.canvascue.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.canvascue.R
import com.canvascue.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() = with(binding) {
        // Setup toolbar
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup RecyclerView
        recentWorksList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Setup click listeners
        btnNewProject.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_workspace)
        }
    }

    private fun observeViewModel() {
        // Observe ViewModel states when implemented
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}