package com.example.appguaugo.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class UserPreferences(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USER_EMAIL = "user_email"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    val userEmail: Flow<String?> = flow {
        emit(prefs.getString(KEY_USER_EMAIL, null))
    }.distinctUntilChanged()

    suspend fun saveUserEmail(email: String) {
        withContext(Dispatchers.IO) {
            prefs.edit().putString(KEY_USER_EMAIL, email).apply()
        }
    }

    suspend fun clearUserEmail() {
        withContext(Dispatchers.IO) {
            prefs.edit().remove(KEY_USER_EMAIL).apply()
        }
    }

    fun getUserEmailSync(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
}
