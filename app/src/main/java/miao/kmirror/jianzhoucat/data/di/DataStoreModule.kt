package miao.kmirror.jianzhoucat.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import miao.kmirror.jianzhoucat.data.local.datastore.AppDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideAppDataStore(@ApplicationContext context: Context): AppDataStore {
        return AppDataStore(context)
    }
}