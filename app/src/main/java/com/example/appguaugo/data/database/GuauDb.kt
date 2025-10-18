package com.example.appguaugo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.entity.ClienteEntity

@Database(entities = [ClienteEntity:: class], version = 1)
abstract class GuauDb: RoomDatabase() {

    abstract fun clienteDao(): ClienteDao
}