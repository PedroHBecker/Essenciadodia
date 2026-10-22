package com.seugrupo.essenciadodia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Perfume::class, Ocasiao::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun perfumeDao(): PerfumeDao
    abstract fun ocasiaoDao(): OcasiaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "essencia_do_dia.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}