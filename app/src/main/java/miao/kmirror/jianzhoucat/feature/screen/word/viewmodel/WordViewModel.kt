package miao.kmirror.jianzhoucat.feature.screen.word.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import miao.kmirror.jianzhoucat.domin.model.WordModel
import miao.kmirror.jianzhoucat.domin.repository.WordRepository
import miao.kmirror.jianzhoucat.feature.state.LoadState
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoadState>(LoadState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _currentWordModel = MutableStateFlow(WordModel.getMock())
    val currentWordModel = _currentWordModel.asStateFlow()

    private val _nextWordModel = MutableStateFlow(WordModel.getMock())
    val nextWordModel = _nextWordModel.asStateFlow()

    fun initData() {
        if (_uiState.value != LoadState.Success){
            viewModelScope.launch {
                Log.i("KmirrorTag", "initData: ")
                _uiState.value = LoadState.Loading
                val initial = WordModel.getMock()
                val next = WordModel.getMock()
                _currentWordModel.value = initial
                _nextWordModel.value = next
                _uiState.value = LoadState.Success
            }
        }

    }

    fun nextWord() {
        viewModelScope.launch {
            Log.i("KmirrorTag", "nextWord: miao")
            _currentWordModel.value = _nextWordModel.value
            _nextWordModel.value = WordModel.getMock()
        }
    }
}
