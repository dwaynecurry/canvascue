package com.canvascue.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.canvascue.models.PaintProject
import com.canvascue.models.UserPreferences

@Database(
    entities = [PaintProject::class, UserPreferences::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}