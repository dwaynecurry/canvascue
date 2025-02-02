package com.canvascue.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.canvascue.databinding.ActivityEditorBinding
import com.canvascue.utils.ImageProcessor
import kotlinx.coroutines.launch

class EditorActivity : ComponentActivity() {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var imageProcessor: ImageProcessor
    private var currentColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageProcessor = ImageProcessor(this)
        setupUI()
        processImage()
    }

    private fun setupUI() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            colorPicker.setOnColorSelectedListener { color ->
                currentColor = color
                drawingView.setColor(color)
            }

            btnUndo.setOnClickListener { drawingView.undo() }
            btnRedo.setOnClickListener { drawingView.redo() }
            btnEraser.setOnClickListener { drawingView.enableEraser() }

            btnSave.setOnClickListener { saveProject() }
        }
    }

    private fun processImage() {
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
            ?: return finish()

        lifecycleScope.launch {
            val processed = imageProcessor.processImage(imageUri)

            binding.apply {
                drawingView.setBaseImage(processed.originalBitmap)
                drawingView.setOverlayImage(processed.processedBitmap)
                colorPicker.setColors(processed.palette)
            }
        }
    }

    private fun saveProject() {
        // Implement save functionality
    }

    companion object {
        private const val EXTRA_IMAGE_URI = "extra_image_uri"

        fun createIntent(context: Context, imageUri: Uri) =
            Intent(context, EditorActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI, imageUri.toString())
            }
    }
}