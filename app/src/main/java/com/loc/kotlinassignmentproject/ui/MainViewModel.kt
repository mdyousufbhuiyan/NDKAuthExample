package com.loc.kotlinassignmentproject.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.loc.kotlinassignmentproject.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MainRepository
): ViewModel() {
    private val _text = MutableStateFlow("Tap to call API")
    val text = _text.asStateFlow()

    fun callApi() {
        viewModelScope.launch {
            runCatching { repo.ping() }
                .onSuccess { _text.value = "Success: $it" }
                .onFailure { _text.value = "Error: " + it.message }
        }
    }
}
