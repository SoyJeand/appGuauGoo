package com.example.appguaugo.data.repository

import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.entity.ClienteEntity

class ClienteRepository(private val clienteDao: ClienteDao) {

    suspend fun insertCliente(cliente: ClienteEntity): Long {
        return clienteDao.insertCliente(cliente)
    }

    suspend fun validarCliente(correo: String, contrasenha: String): ClienteEntity? {
        return clienteDao.validarCliente(correo, contrasenha)
    }

    suspend fun getClientePorCorreo(correo: String): ClienteEntity? {
        return clienteDao.getClientePorCorreo(correo)
    }
}
