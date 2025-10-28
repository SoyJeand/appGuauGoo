package com.example.appguaugo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.entity.Converters

@Database(entities = [ClienteEntity:: class],
    version = 2,
    exportSchema = false)

@TypeConverters(Converters::class)
abstract class GuauDb: RoomDatabase() {

    abstract fun clienteDao(): ClienteDao
}