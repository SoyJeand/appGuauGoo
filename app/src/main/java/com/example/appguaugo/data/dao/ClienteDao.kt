package com.example.appguaugo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appguaugo.data.entity.ClienteEntity

@Dao
interface ClienteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCliente(cliente: ClienteEntity): Long


    @Query("Select * from cliente where correo=:correo AND contrasenha =:contrasenha LIMIT 1")
    suspend fun validarCliente(correo: String, contrasenha: String): ClienteEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertClienteList(people:List<ClienteEntity>): List<Long>

    @Update
    suspend fun updateCliente(cliente: ClienteEntity): Int

    @Delete
    suspend fun deleteCliente(cliente: ClienteEntity): Int

    @Query("Select * from cliente")
    suspend fun getAll(): List<ClienteEntity>

    @Query("Select * from cliente where id_cliente in (:clienteIds)")
    suspend fun getAllById(clienteIds: IntArray): List<ClienteEntity>



}