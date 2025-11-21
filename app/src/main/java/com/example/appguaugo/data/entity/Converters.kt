package com.example.appguaugo.data.entity

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        // Convierte de Long (base de datos) a Date (código)
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        // Convierte de Date (código) a Long (base de datos)
        return date?.time
    }

}