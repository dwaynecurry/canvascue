package com.canvascue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.canvascue.databinding.ItemColorPaletteBinding
import com.canvascue.models.ColorPalette

class ColorPaletteAdapter(
    private val onColorSelected: (Int) -> Unit
) : ListAdapter<ColorPalette, ColorPaletteAdapter.ViewHolder>(ColorPaletteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemColorPaletteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemColorPaletteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(palette: ColorPalette) {
            binding.colorView.setBackgroundColor(palette.colors.first())
            binding.root.setOnClickListener { onColorSelected(palette.colors.first()) }
        }
    }

    private class ColorPaletteDiffCallback : DiffUtil.ItemCallback<ColorPalette>() {
        override fun areItemsTheSame(oldItem: ColorPalette, newItem: ColorPalette) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ColorPalette, newItem: ColorPalette) =
            oldItem == newItem
    }
}