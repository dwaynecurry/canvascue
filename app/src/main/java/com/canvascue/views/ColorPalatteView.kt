package com.canvascue.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.canvascue.R

class ColorPaletteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private var onColorSelectedListener: ((Int) -> Unit)? = null
    private val colors = mutableListOf<Int>()

    init {
        orientation = VERTICAL

        // Inflate layout manually since we're not using view binding
        LayoutInflater.from(context).inflate(R.layout.view_color_palette, this, true)

        recyclerView = findViewById(R.id.recyclerView)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = ColorAdapter(colors) { color ->
                onColorSelectedListener?.invoke(color)
            }
        }
    }

    fun setOnColorSelectedListener(listener: (Int) -> Unit) {
        onColorSelectedListener = listener
    }

    fun setColors(newColors: List<Int>) {
        colors.clear()
        colors.addAll(newColors)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}