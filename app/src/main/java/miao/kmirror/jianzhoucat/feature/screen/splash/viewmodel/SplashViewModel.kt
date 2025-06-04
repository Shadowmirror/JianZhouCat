package miao.kmirror.jianzhoucat.feature.screen.splash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import miao.kmirror.jianzhoucat.data.local.AppDatabase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {

    private val _timeOut = MutableStateFlow(1)
    val timeOut = _timeOut.asStateFlow()

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized = _dataInitialized.asStateFlow()

    fun initData() {
        viewModelScope.launch {
            val timerJob = async {
                while (_timeOut.value > 0) {
                    delay(1000)
                    _timeOut.value--
                }
            }

            val dbJob = async {
                initDatabase()
            }

            // 等两个任务都完成才更新为 true
            awaitAll(timerJob, dbJob)

            _dataInitialized.value = true

        }

    }


    private suspend fun initDatabase() {
        database.openHelper.writableDatabase
        Log.i("KmirrorTag", "initDatabase: 模拟数据库加载")
    }
}