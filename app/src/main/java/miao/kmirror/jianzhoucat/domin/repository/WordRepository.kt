package miao.kmirror.jianzhoucat.domin.repository

import miao.kmirror.jianzhoucat.data.local.entity.WordDTO
import miao.kmirror.jianzhoucat.data.local.entity.WordMemoryDTO


interface WordRepository {
    suspend fun getAllWords(): List<WordDTO>
    suspend fun insertWord(wordDTO: WordDTO)
    suspend fun getAllWordMemory(): List<WordMemoryDTO>
    suspend fun insertWordMemory(wordMemoryDTO: WordMemoryDTO)
}