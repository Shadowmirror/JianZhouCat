package miao.kmirror.jianzhoucat.feature.screen.main.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainAtyViewModel @Inject constructor() : ViewModel() {
    private val _seedColor = MutableStateFlow<Color>(Color(0xFFFF0000))
    val seedColor: StateFlow<Color> = _seedColor.asStateFlow()

    fun setSeedColor(color: Color) {
        Log.i("KmirrorTag", "setSeedColor: color alpha = ${color.alpha}, red = ${color.red}, green = ${color.green}, blue = ${color.blue}")
        _seedColor.value = color
    }

    fun randomColor(): Color {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color(red, green, blue)
    }
}