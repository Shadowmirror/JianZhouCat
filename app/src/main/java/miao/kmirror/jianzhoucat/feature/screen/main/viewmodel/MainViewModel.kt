package miao.kmirror.jianzhoucat.feature.screen.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import miao.kmirror.jianzhoucat.feature.state.LoadState

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow<LoadState>(LoadState.Loading)
    val uiState: StateFlow<LoadState> = _uiState.asStateFlow()

    fun loadMain() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = LoadState.Loading
            delay(5000)
            _uiState.value = LoadState.Success
        }
    }
}