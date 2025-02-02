package com.canvascue.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.canvascue.databinding.ItemTipBinding
import com.canvascue.models.Tip

class TipsAdapter(
    private val onTipSelected: (Tip) -> Unit
) : ListAdapter<Tip, TipsAdapter.ViewHolder>(TipsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTipBinding.inflate(
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
        private val binding: ItemTipBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTipSelected(getItem(position))
                }
            }
        }

        fun bind(tip: Tip) {
            binding.apply {
                tipIcon.setImageResource(tip.iconResId)
                tipTitle.text = tip.title
                tipDescription.text = tip.description
            }
        }
    }

    private class TipsDiffCallback : DiffUtil.ItemCallback<Tip>() {
        override fun areItemsTheSame(oldItem: Tip, newItem: Tip) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Tip, newItem: Tip) =
            oldItem == newItem
    }
}