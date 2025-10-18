package com.example.appguaugo.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClienteEntity (
    @PrimaryKey(autoGenerate = true) val idcli:Int = 0,
    @ColumnInfo(name = "nombres") val nombres: String?,
    @ColumnInfo(name = "apellidos") val apellidos: String?,
    @ColumnInfo(name = "correo") val correo: String?,
    @ColumnInfo(name = "contrasenha") val contrasenha: String?,
    @ColumnInfo(name = "fecha_nacimiento") val fecNacimiento: String?,
    @ColumnInfo(name = "direccion") val direccion: String?,
    @ColumnInfo(name = "telefono") val telefono: String?,
    )
