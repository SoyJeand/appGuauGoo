package com.example.appguaugo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.dao.MascotaDao
import com.example.appguaugo.data.dao.SolicitudPaseoDao
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.entity.Converters
import com.example.appguaugo.data.entity.MascotaEntity
import com.example.appguaugo.data.entity.SolicitudPaseoEntity

@Database(entities = [ClienteEntity:: class, MascotaEntity::class, SolicitudPaseoEntity::class],
    version = 4,
    exportSchema = false)

@TypeConverters(Converters::class)
abstract class GuauDb: RoomDatabase() {

    abstract fun clienteDao(): ClienteDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun solicitudPaseoDao(): SolicitudPaseoDao
}