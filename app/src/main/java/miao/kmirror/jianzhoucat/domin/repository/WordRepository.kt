package miao.kmirror.jianzhoucat.domin.repository

import miao.kmirror.jianzhoucat.domin.model.WordModel


interface WordRepository {
    suspend fun getAllWords(): List<WordModel>
    suspend fun updateWord(wordModel: WordModel)
    suspend fun insertAllWord(wordModelList: List<WordModel>)
}