package miao.kmirror.jianzhoucat.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import miao.kmirror.jianzhoucat.data.local.AppDatabase
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
            "app_database_test.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordDao(database: AppDatabase) = database.wordDao()

    @Provides
    @Singleton
    fun provideWordMemoryDao(database: AppDatabase) = database.wordMemoryDao()
}