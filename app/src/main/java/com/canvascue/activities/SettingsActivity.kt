package com.canvascue.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.canvascue.databinding.ActivitySettingsBinding
import com.canvascue.models.UserPreferences
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadPreferences()
    }

    private fun setupUI() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
                updateDarkMode(isChecked)
            }

            switchGridLines.setOnCheckedChangeListener { _, isChecked ->
                updateGridLines(isChecked)
            }

            sliderBrushSize.addOnChangeListener { _, value, _ ->
                updateBrushSize(value)
            }
        }
    }

    private fun loadPreferences() {
        lifecycleScope.launch {
            // Load preferences from DataStore
            // Update UI accordingly
        }
    }

    private fun updateDarkMode(enabled: Boolean) {
        // Implement dark mode toggle
    }

    private fun updateGridLines(enabled: Boolean) {
        // Implement grid lines toggle
    }

    private fun updateBrushSize(size: Float) {
        // Implement brush size update
    }
}