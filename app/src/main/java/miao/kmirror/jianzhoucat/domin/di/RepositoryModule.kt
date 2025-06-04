package miao.kmirror.jianzhoucat.domin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import miao.kmirror.jianzhoucat.data.repository.WordRepositoryImpl
import miao.kmirror.jianzhoucat.domin.repository.WordRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWordRepository(
        impl: WordRepositoryImpl
    ): WordRepository
}