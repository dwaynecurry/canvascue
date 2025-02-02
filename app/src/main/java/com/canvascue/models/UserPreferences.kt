package com.canvascue.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey
    val id: Int = 1,
    val darkMode: Boolean = false,
    val showTutorial: Boolean = true,
    val autoSave: Boolean = true
)