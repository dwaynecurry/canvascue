package com.canvascue.data

import androidx.room.*
import com.canvascue.models.PaintProject
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<PaintProject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: PaintProject): Long

    @Update
    suspend fun updateProject(project: PaintProject)

    @Delete
    suspend fun deleteProject(project: PaintProject)

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Long): PaintProject?
}