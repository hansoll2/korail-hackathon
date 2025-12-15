package com.mascot.app.data.repository

import com.mascot.app.data.repository.MascotRepository
import com.mascot.app.data.repository.MascotRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMascotRepository(
        mascotRepositoryImpl: MascotRepositoryImpl
    ): MascotRepository
}