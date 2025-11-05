package com.example.appguaugo.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date


@Entity(
    tableName = "mascota",
    foreignKeys = [
        ForeignKey(
            entity = ClienteEntity::class,
            parentColumns = ["id_cliente"],
            childColumns = ["duenoId"],
            onDelete = ForeignKey.CASCADE // Si se borra el dueño, se borran sus mascotas
        )
    ],
    indices = [Index(value = ["duenoId"])]
)
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_mascota")
    val id: Int = 0,
    @ColumnInfo(name = "duenoId") val duenoId: Int,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "tipo") val tipo: String,
    @ColumnInfo(name = "raza") val raza: String,
    @ColumnInfo(name = "edad") val edad: Int,
    @ColumnInfo(name = "peso") val peso: Double,
    @ColumnInfo(name = "comportamiento") val comportamiento: String

    )
