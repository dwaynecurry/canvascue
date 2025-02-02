package com.canvascue.ui.home.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.canvascue.databinding.ItemRecentWorkBinding
import com.canvascue.models.PaintProject
import com.google.android.material.card.MaterialCardView

class RecentWorksAdapter(
    private val onProjectSelected: (PaintProject) -> Unit
) : ListAdapter<PaintProject, RecentWorksAdapter.ViewHolder>(RecentWorksDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecentWorkBinding.inflate(
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
        private val binding: ItemRecentWorkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            (binding.root as MaterialCardView).apply {
                setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onProjectSelected(getItem(position))
                    }
                }
            }
        }

        fun bind(project: PaintProject) {
            binding.apply {
                projectImage.setImageURI(Uri.parse(project.imageUri))
                projectName.text = project.name
                progressBar.progress = (project.progress * 100).toInt()
                progressText.text = "${(project.progress * 100).toInt()}%"

                // Add transition name for shared element transition
                root.transitionName = "project_card_${project.id}"
            }
        }
    }

    private class RecentWorksDiffCallback : DiffUtil.ItemCallback<PaintProject>() {
        override fun areItemsTheSame(oldItem: PaintProject, newItem: PaintProject) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PaintProject, newItem: PaintProject) =
            oldItem == newItem
    }
}