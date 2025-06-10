package miao.kmirror.jianzhoucat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import miao.kmirror.jianzhoucat.data.local.entity.WordMemoryDTO

@Dao
interface WordMemoryDao {
    @Query("SELECT * FROM WordMemoryDTO")
    suspend fun getAll(): List<WordMemoryDTO>

    @Query("SELECT * FROM WordMemoryDTO WHERE word = :word")
    suspend fun getByWord(word: String): WordMemoryDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wordMemory: WordMemoryDTO)
}