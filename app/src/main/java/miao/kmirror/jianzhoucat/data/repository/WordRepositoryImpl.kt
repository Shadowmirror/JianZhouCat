package miao.kmirror.jianzhoucat.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import miao.kmirror.jianzhoucat.data.local.dao.WordDao
import miao.kmirror.jianzhoucat.data.local.dao.WordMemoryDao
import miao.kmirror.jianzhoucat.data.local.entity.WordDTO
import miao.kmirror.jianzhoucat.data.local.entity.WordMemoryDTO
import miao.kmirror.jianzhoucat.domin.model.WordModel
import miao.kmirror.jianzhoucat.domin.repository.WordRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepositoryImpl @Inject constructor(
    private val wordDao: WordDao,
    private val wordMemoryDao: WordMemoryDao
) : WordRepository {

    // 内存缓存：单词 -> WordModel
    private val cache = ConcurrentHashMap<String, WordModel>()

    // 使用协程友好的 Mutex 替代 synchronized
    private val cacheMutex = Mutex()
    private var isCacheInitialized = false

    override suspend fun getAllWords(): List<WordModel> {
        // 使用协程互斥锁安全地初始化缓存
        if (!isCacheInitialized) {
            cacheMutex.withLock {
                // 双重检查锁定
                if (!isCacheInitialized) {
                    loadDataIntoCache()
                    isCacheInitialized = true
                }
            }
        }
        return cache.values.toList()
    }

    override suspend fun updateWord(wordModel: WordModel) {
        // 更新数据库
        wordDao.insert(
            WordDTO(
                word = wordModel.word,
                translate = wordModel.translate,
                phoneticSymbol = wordModel.phoneticSymbol,
                exampleSentence = wordModel.exampleSentence,
                customMemory = wordModel.customMemory
            )
        )

        wordMemoryDao.insert(
            WordMemoryDTO(
                word = wordModel.word,
                level = wordModel.level,
                createTime = wordModel.createTime,
                rememberCount = wordModel.rememberCount,
                vagueCount = wordModel.vagueCount,
                forgetCount = wordModel.forgetCount,
                updateTime = wordModel.updateTime
            )
        )

        // 更新缓存 - 使用协程互斥锁保证线程安全
        cacheMutex.withLock {
            cache[wordModel.word] = wordModel
        }
    }

    override suspend fun insertAllWord(wordModelList: List<WordModel>) {
        for (wordModel in wordModelList) {
            updateWord(wordModel)
        }
    }

    /**
     * 从数据库加载数据到缓存
     */
    private suspend fun loadDataIntoCache() {
        val words = wordDao.getAll()
        val memories = wordMemoryDao.getAll().associateBy { it.word }

        words.forEach { word ->
            val memory = memories[word.word] ?: WordMemoryDTO(word = word.word)
            cache[word.word] = WordModel(
                word = word.word,
                translate = word.translate,
                phoneticSymbol = word.phoneticSymbol,
                exampleSentence = word.exampleSentence,
                customMemory = word.customMemory,
                level = memory.level,
                createTime = memory.createTime,
                rememberCount = memory.rememberCount,
                vagueCount = memory.vagueCount,
                forgetCount = memory.forgetCount,
                updateTime = memory.updateTime
            )
        }
    }

    /**
     * 清除缓存（可选，用于强制刷新）
     */
    suspend fun clearCache() {
        cacheMutex.withLock {
            cache.clear()
            isCacheInitialized = false
        }
    }
}