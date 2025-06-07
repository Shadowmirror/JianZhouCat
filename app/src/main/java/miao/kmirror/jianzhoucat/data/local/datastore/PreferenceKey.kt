package miao.kmirror.jianzhoucat.data.local.datastore

import androidx.compose.ui.graphics.Color

sealed class PreferenceKey() {
    object CurrentUser : PreferenceKey() {
        fun default() = 0
    }

    object ThemeColor : PreferenceKey() {
        fun default() = Color.Red
    }

    fun name(): String = this::class.java.simpleName
}


