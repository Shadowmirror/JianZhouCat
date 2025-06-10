package miao.kmirror.jianzhoucat.feature.screen.splash.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import miao.kmirror.jianzhoucat.data.local.AppDatabase
import miao.kmirror.jianzhoucat.data.local.entity.WordDTO
import miao.kmirror.jianzhoucat.data.repository.WordRepositoryImpl
import miao.kmirror.jianzhoucat.domin.model.WordModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val database: AppDatabase,
    private val wordRepositoryImpl: WordRepositoryImpl
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
        val allWords = wordRepositoryImpl.getAllWords()
        Log.i("KmirrorTag", "initDatabase: allWords.size = ${allWords.size}")
        if (allWords.isEmpty()) {
            var wordList = mutableListOf<WordDTO>()

            try {
                val jsonString = context.assets.open("wordTest.json").bufferedReader().use { it.readText() }
                val json = Json { ignoreUnknownKeys = true }
                wordList = json.decodeFromString(jsonString)
                val wordModelList = wordList.map {
                    WordModel.getWordModelByWordDTO(it)
                }


                wordRepositoryImpl.insertAllWord(wordModelList)

            } catch (e: Exception) {
                Log.i("KmirrorTag", "initDatabase: 初始化数据失败")
                e.printStackTrace()
            }
        }
    }
}