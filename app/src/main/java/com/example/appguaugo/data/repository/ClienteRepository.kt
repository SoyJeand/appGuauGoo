package com.example.appguaugo.data.repository

import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.entity.ClienteEntity
import kotlinx.coroutines.flow.Flow

class ClienteRepository(private val clienteDao: ClienteDao) {

    // Llama a la función correspondiente en el DAO para insertar un cliente.
    suspend fun insertCliente(cliente: ClienteEntity): Long {
        return clienteDao.insertCliente(cliente)
    }

    // Llama a la función correspondiente en el DAO para validar un cliente.
    suspend fun validarCliente(correo: String, contrasenha: String): ClienteEntity? {
        return clienteDao.validarCliente(correo, contrasenha)
    }

    fun getClienteById(userId: Int): Flow<ClienteEntity?> {
        return clienteDao.getClienteById(userId)
    }

    // Llama a la función correspondiente en el DAO para actualizar un cliente.
    suspend fun updateCliente(cliente: ClienteEntity): Int {
        return clienteDao.updateCliente(cliente)
    }

    // Llama a la función correspondiente en el DAO para eliminar un cliente.
    suspend fun deleteCliente(cliente: ClienteEntity): Int {
        return clienteDao.deleteCliente(cliente)
    }

    // Llama a la función correspondiente en el DAO para obtener todos los clientes.
    suspend fun getAllClientes(): List<ClienteEntity> {
        return clienteDao.getAll()
    }

    // Llama a la función correspondiente en el DAO para obtener clientes por sus IDs.
    suspend fun getAllClientesById(clienteIds: IntArray): List<ClienteEntity> {
        return clienteDao.getAllById(clienteIds)
    }
}