package com.mascot.app.data.encyclopediadata.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mascot.app.data.encyclopediadata.dao.MascotDao
import com.mascot.app.data.encyclopediadata.entity.MascotEntity
import com.mascot.app.data.encyclopediadata.entity.ZoneEntity
@Database(
    entities = [ZoneEntity::class, MascotEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mascotDao(): MascotDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DB_NAME = "mascot.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration() // 🔥 자동 재생성
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
