package miao.kmirror.jianzhoucat.data.repository

import miao.kmirror.jianzhoucat.data.local.dao.WordDao
import miao.kmirror.jianzhoucat.data.local.dao.WordMemoryDao
import miao.kmirror.jianzhoucat.data.local.entity.WordDTO
import miao.kmirror.jianzhoucat.data.local.entity.WordMemoryDTO
import miao.kmirror.jianzhoucat.domin.repository.WordRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepositoryImpl @Inject constructor(private val wordDao: WordDao, private val wordMemoryDao: WordMemoryDao) : WordRepository {
    override suspend fun getAllWords(): List<WordDTO> {
        return wordDao.getAll()
    }

    override suspend fun insertWord(wordDTO: WordDTO) {
        wordDao.insert(wordDTO)
    }

    override suspend fun getAllWordMemory(): List<WordMemoryDTO> {
        return wordMemoryDao.getAll()
    }

    override suspend fun insertWordMemory(wordMemoryDTO: WordMemoryDTO) {
        wordMemoryDao.insert(wordMemoryDTO)

    }
}