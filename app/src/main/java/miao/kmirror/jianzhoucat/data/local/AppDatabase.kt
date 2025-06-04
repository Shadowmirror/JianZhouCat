package miao.kmirror.jianzhoucat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import miao.kmirror.jianzhoucat.data.local.dao.WordDao
import miao.kmirror.jianzhoucat.data.local.dao.WordMemoryDao
import miao.kmirror.jianzhoucat.data.local.entity.WordDTO
import miao.kmirror.jianzhoucat.data.local.entity.WordMemoryDTO

@Database(entities = [WordDTO::class, WordMemoryDTO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun wordMemoryDao(): WordMemoryDao
}