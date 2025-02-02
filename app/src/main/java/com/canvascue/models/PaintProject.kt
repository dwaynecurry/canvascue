package com.canvascue.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class PaintProject(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageUri: String,
    val name: String,
    val progress: Float = 0f,
    val createdAt: Long,
    val lastModified: Long = createdAt,
    val duration: Int? = 0
)