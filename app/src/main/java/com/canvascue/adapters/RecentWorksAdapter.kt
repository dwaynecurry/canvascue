package com.canvascue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.canvascue.databinding.ItemRecentWorkBinding
import com.canvascue.models.PaintProject
import com.canvascue.utils.setImageURIString

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

        fun bind(project: PaintProject) {
            binding.apply {
                projectImage.setImageURIString(project.imageUri)
                projectName.text = project.name
                progressBar.progress = (project.progress * 100).toInt()
                root.setOnClickListener { onProjectSelected(project) }
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