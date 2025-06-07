package miao.kmirror.jianzhoucat.feature.screen.main.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import miao.kmirror.jianzhoucat.data.local.datastore.AppDataStore
import miao.kmirror.jianzhoucat.data.local.datastore.PreferenceKey
import javax.inject.Inject

@HiltViewModel
class MainAtyViewModel @Inject constructor(
    private val appDataStore: AppDataStore,
) : ViewModel() {

    val themeColor: StateFlow<Int> = appDataStore
        .getThemeColorFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PreferenceKey.ThemeColor.default().toArgb()
        )

    fun setThemeColor(color: Color) {
        viewModelScope.launch {
            Log.i("KmirrorTag", "setThemeColor: color alpha = ${color.alpha}, red = ${color.red}, green = ${color.green}, blue = ${color.blue}")
            appDataStore.setThemeColor(color)
        }
    }


    fun getThemeColor() = Color(themeColor.value)

    fun changeCurrentUser() {
        viewModelScope.launch {
            if (appDataStore.mCurrentUser.value == 0) {
                appDataStore.setCurrentUser(1)
            } else {
                appDataStore.setCurrentUser(0)
            }
        }
    }
}