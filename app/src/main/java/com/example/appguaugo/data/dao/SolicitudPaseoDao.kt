package com.example.appguaugo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.appguaugo.data.entity.SolicitudPaseoEntity

@Dao
interface SolicitudPaseoDao {
    @Insert
    suspend fun insertSolicitud(solicitud: SolicitudPaseoEntity)
}
