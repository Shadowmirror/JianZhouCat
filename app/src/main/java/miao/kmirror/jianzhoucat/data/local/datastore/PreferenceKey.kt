package miao.kmirror.jianzhoucat.data.local.datastore

import androidx.compose.ui.graphics.Color

enum class PreferenceKey {
    CurrentUser,
    ThemeColor,
}

object PreferenceKeyDefault {
    val currentUser = 0
    val themeColor = Color.Red
}



