package miao.kmirror.jianzhoucat.data.local.datastore

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


class AppDataStore @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "AppDataStore")

    val mCurrentUser: StateFlow<Int> = context.appDataStore.data
        .map { preferences ->
            preferences[intPreferencesKey(PreferenceKey.CurrentUser.name)] ?: PreferenceKeyDefault.currentUser
        }
        .stateIn(
            scope = GlobalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PreferenceKeyDefault.currentUser
        )


    suspend fun setCurrentUser(userId: Int) {
        context.appDataStore.setGlobalValue(intPreferencesKey(PreferenceKey.CurrentUser.multiName()), userId)
    }

    fun getThemeColorFlow(): Flow<Int> {
        return context.appDataStore.getUserBasedFlow(
            mCurrentUser,
            { userId -> intPreferencesKey(PreferenceKey.ThemeColor.multiName(userId)) },
            PreferenceKeyDefault.themeColor.toArgb()
        )
    }


    suspend fun setThemeColor(color: Color, userId: Int = mCurrentUser.value) {
        context.appDataStore.edit { preferences ->
            preferences[intPreferencesKey(PreferenceKey.ThemeColor.multiName(userId))] = color.toArgb()
        }
    }


    /**
     *  下方都是辅助工具，建议都是 private 这样防止影响其他地方调用
     * */
    private fun PreferenceKey.multiName(currentUser: Int = PreferenceKeyDefault.currentUser): String {
        return if (currentUser == PreferenceKeyDefault.currentUser) {
            this@multiName.name
        } else {
            "user_${currentUser}_${this@multiName.name}"
        }
    }

    private inline fun <reified T> DataStore<Preferences>.getUserBasedFlow(
        userIdFlow: Flow<Int>,
        crossinline keyProvider: (Int) -> Preferences.Key<T>,
        default: T
    ): Flow<T> {
        return combine(userIdFlow, data) { userId, preferences ->
            preferences[keyProvider(userId)] ?: default
        }
    }

    private suspend inline fun <reified T> DataStore<Preferences>.setUserBasedValue(
        userId: Int,
        crossinline keyProvider: (Int) -> Preferences.Key<T>,
        value: T
    ) {
        edit { preferences ->
            preferences[keyProvider(userId)] = value
        }
    }

    private inline fun <reified T> DataStore<Preferences>.getGlobalFlow(
        key: Preferences.Key<T>,
        default: T
    ): Flow<T> {
        return data.map { preferences ->
            preferences[key] ?: default
        }
    }

    private suspend inline fun <reified T> DataStore<Preferences>.setGlobalValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        edit { preferences ->
            preferences[key] = value
        }
    }


    private fun <T> Flow<T>.defaultValue(
        scope: CoroutineScope,
        started: SharingStarted = SharingStarted.WhileSubscribed(5000),
        initialValue: T
    ): StateFlow<T> {
        return this.stateIn(
            scope = scope,
            started = started,
            initialValue = initialValue
        )
    }
}




