package com.loc.kotlinassignmentproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.kotlinassignmentproject.data.api.RetrofitProvider
import com.loc.kotlinassignmentproject.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository = MainRepository(RetrofitProvider.apiService)) :
    ViewModel() {

    private val _data = MutableStateFlow<String>("Loading...")
    val data: StateFlow<String> = _data

    fun loadData() {
        viewModelScope.launch {
            try {
                val response = repository.fetchPost()
                _data.value = response.body
            } catch (e: Exception) {
                _data.value = "Error: ${e.message}"
            }
        }
    }
}
