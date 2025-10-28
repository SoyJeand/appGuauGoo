package com.example.appguaugo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.entity.ClienteEntity

@Database(
    entities = [ClienteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guaugo_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}