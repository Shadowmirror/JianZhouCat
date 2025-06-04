package miao.kmirror.jianzhoucat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class WordMemoryDTO(
    @PrimaryKey val word: String,
    /**
     * 记忆等级
     * */
    val level: Int = 0
)