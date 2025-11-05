package com.example.appguaugo.application

import android.app.Application
import androidx.room.Room
import com.example.appguaugo.data.database.GuauDb

class GuauApp: Application() {

    /*val db = Room.databaseBuilder(
    applicationContext,
    GuauDb::class.java,
    "guaudb"
    ).build()*/

    companion object{
        lateinit var db: GuauDb
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            GuauDb::class.java,
            "guaudb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}