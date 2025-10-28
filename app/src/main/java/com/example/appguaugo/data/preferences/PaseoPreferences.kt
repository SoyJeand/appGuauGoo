package com.example.appguaugo.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONArray

class PaseoPreferences(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "paseos_prefs"
        private const val KEY_PASEOS = "paseos_guardados"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    val paseosGuardados: Flow<List<String>> = flow {
        emit(getPaseosSync())
    }.distinctUntilChanged()

    suspend fun guardarPaseo(paseo: String) {
        withContext(Dispatchers.IO) {
            val actual = prefs.getString(KEY_PASEOS, "[]") ?: "[]"
            val array = JSONArray(actual)
            array.put(paseo)
            prefs.edit().putString(KEY_PASEOS, array.toString()).apply()
        }
    }

    suspend fun limpiarPaseos() {
        withContext(Dispatchers.IO) {
            prefs.edit().remove(KEY_PASEOS).apply()
        }
    }

    fun getPaseosSync(): List<String> {
        val json = prefs.getString(KEY_PASEOS, "[]") ?: "[]"
        val array = JSONArray(json)
        val lista = mutableListOf<String>()
        for (i in 0 until array.length()) {
            lista.add(array.getString(i))
        }
        return lista
    }
}
