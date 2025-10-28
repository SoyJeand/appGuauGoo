package com.example.appguaugo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appguaugo.data.entity.ClienteEntity

@Dao
interface ClienteDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCliente(cliente: ClienteEntity): Long

    @Query("SELECT * FROM cliente WHERE correo = :correo AND contrasenha = :contrasenha LIMIT 1")
    suspend fun validarCliente(correo: String, contrasenha: String): ClienteEntity?

    @Query("SELECT * FROM cliente WHERE correo = :correo LIMIT 1")
    suspend fun getClientePorCorreo(correo: String): ClienteEntity?
}
