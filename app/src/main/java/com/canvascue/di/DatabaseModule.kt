package com.canvascue.di

import android.content.Context
import androidx.room.Room
import com.canvascue.data.AppDatabase
import com.canvascue.data.ProjectDao
import com.canvascue.data.ProjectRepository
import com.canvascue.utils.ImageProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "canvascue_database"
        ).build()
    }

    @Provides
    fun provideProjectDao(database: AppDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    @Singleton
    fun provideImageProcessor(@ApplicationContext context: Context): ImageProcessor {
        return ImageProcessor(context)
    }

    @Provides
    @Singleton
    fun provideProjectRepository(
        projectDao: ProjectDao,
        imageProcessor: ImageProcessor,
        @ApplicationContext context: Context
    ): ProjectRepository {
        return ProjectRepository(projectDao, imageProcessor, context)
    }
}