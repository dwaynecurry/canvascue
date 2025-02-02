package com.canvascue.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.canvascue.R

class ColorAdapter(
    private val colors: List<Int>,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        holder.colorView.setCardBackgroundColor(color)
        holder.itemView.setOnClickListener { onColorSelected(color) }
    }

    override fun getItemCount() = colors.size

    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorView: MaterialCardView = itemView.findViewById(R.id.colorView)
    }
}