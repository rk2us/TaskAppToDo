package com.rahul.taskapptodo.di



import android.app.Application
import androidx.room.Room
import com.rahul.taskapptodo.data.local.AppDatabase
import com.rahul.taskapptodo.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provide the Room Database instance
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "task_db"
        ).fallbackToDestructiveMigration()
            .build()

    // Provide DAO from the database
    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()
}
