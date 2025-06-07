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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


/**
 * AppDataStore 是一个用于管理应用中偏好设置的类，支持根据当前用户 ID 动态切换数据。
 * 它通过 DataStore 维护当前用户 ID、主题颜色等偏好设置，并支持多用户数据隔离。
 */
class AppDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    applicationScope: CoroutineScope
) {

    // 创建名为 "AppDataStore" 的 DataStore<Preferences> 实例
    private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "AppDataStore")

    // 默认用户 ID（0 表示默认或未登录用户）
    val defaultUser = 0

    /**
     * 当前用户 ID 的状态流（StateFlow）
     * 通过读取 DataStore 中保存的 CurrentUser 键值获取当前用户 ID。
     */
    val mCurrentUser: StateFlow<Int> = context.appDataStore.data
        .map { preferences ->
            preferences[intPreferencesKey(PreferenceKey.CurrentUser.name())] ?: defaultUser
        }
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = defaultUser
        )

    /**
     * 设置当前用户 ID（全局偏好设置）
     */
    suspend fun setCurrentUser(userId: Int) {
        context.appDataStore.setGlobalValue(
            intPreferencesKey(PreferenceKey.CurrentUser.multiName()),
            userId
        )
    }

    fun getCurrentUserId(): Flow<Int> {
        return context.appDataStore.getGlobalFlow(
            key = intPreferencesKey(PreferenceKey.CurrentUser.name()),
            default = defaultUser
        )
    }


    /**
     * 获取当前用户对应的主题颜色 Flow
     */
    fun getThemeColorFlow(): Flow<Int> {
        return context.appDataStore.getUserBasedFlow(
            mCurrentUser,
            { userId -> intPreferencesKey(PreferenceKey.ThemeColor.multiName(userId)) },
            PreferenceKey.ThemeColor.default().toArgb()
        )
    }

    /**
     * 设置当前用户的主题颜色，默认为当前 mCurrentUser
     */
    suspend fun setThemeColor(color: Color, userId: Int = mCurrentUser.value) {
        context.appDataStore.setUserBasedValue(
            userId = userId,
            keyProvider = { intPreferencesKey(PreferenceKey.ThemeColor.multiName(userId)) },
            value = color.toArgb()
        )
    }

    // -------------------- 私有辅助工具函数 --------------------

    /**
     * 为多用户生成独立的偏好键名称
     * 比如 user_1_ThemeColor，user_2_ThemeColor 等
     */
    private fun PreferenceKey.multiName(currentUser: Int = defaultUser): String {
        return if (currentUser == defaultUser) {
            this@multiName.name()
        } else {
            "user_${currentUser}_${this@multiName.name()}"
        }
    }

    /**
     * 根据用户 ID 和 keyProvider 获取对应偏好值的 Flow
     */
    private inline fun <reified T> DataStore<Preferences>.getUserBasedFlow(
        userIdFlow: Flow<Int>,
        crossinline keyProvider: (Int) -> Preferences.Key<T>,
        default: T
    ): Flow<T> {
        return combine(userIdFlow, data) { userId, preferences ->
            preferences[keyProvider(userId)] ?: default
        }
    }

    /**
     * 设置特定用户对应的偏好值
     */
    private suspend inline fun <reified T> DataStore<Preferences>.setUserBasedValue(
        userId: Int,
        crossinline keyProvider: (Int) -> Preferences.Key<T>,
        value: T
    ) {
        edit { preferences ->
            preferences[keyProvider(userId)] = value
        }
    }

    /**
     * 获取全局偏好设置中的值（不区分用户）
     */
    private inline fun <reified T> DataStore<Preferences>.getGlobalFlow(
        key: Preferences.Key<T>,
        default: T
    ): Flow<T> {
        return data.map { preferences ->
            preferences[key] ?: default
        }
    }

    /**
     * 设置全局偏好设置的值（不区分用户）
     */
    private suspend inline fun <reified T> DataStore<Preferences>.setGlobalValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * 将普通 Flow 转为 StateFlow，并提供默认值及生命周期控制
     */
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





