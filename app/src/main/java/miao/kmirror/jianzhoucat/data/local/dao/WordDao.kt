package miao.kmirror.jianzhoucat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import miao.kmirror.jianzhoucat.data.local.entity.WordDTO

@Dao
interface WordDao {
    @Query("SELECT * FROM WordDTO")
    suspend fun getAll(): List<WordDTO>

    @Insert
    suspend fun insert(task: WordDTO)

}