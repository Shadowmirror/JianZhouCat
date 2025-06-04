package miao.kmirror.jianzhoucat.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import miao.kmirror.jianzhoucat.data.local.entity.WordMemoryDTO

@Dao
interface WordMemoryDao {
    @Query("SELECT * FROM WordMemoryDTO")
    suspend fun getAll(): List<WordMemoryDTO>

    @Insert
    suspend fun insert(task: WordMemoryDTO)




}