package com.mascot.app.data.encyclopediadata.db

import android.content.Context
import com.mascot.app.data.encyclopediadata.dao.MascotDao
import com.mascot.app.data.encyclopediadata.db.AppDatabase
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideMascotDao(
        db: AppDatabase
    ): MascotDao =
        db.mascotDao()
}