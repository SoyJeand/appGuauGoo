package com.example.appguaugo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appguaugo.data.repository.ClienteRepository

class MascotasViewModelFactory(
    private val repository: ClienteRepository,
    private val duenoId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MascotasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MascotasViewModel(repository, duenoId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}