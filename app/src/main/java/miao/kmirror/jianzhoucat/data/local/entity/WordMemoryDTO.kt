package miao.kmirror.jianzhoucat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity
data class WordMemoryDTO(
    @PrimaryKey val word: String,
    /**
     * 记忆等级，认识 记忆等级 + 1， 模糊记忆等级 -1，忘记记忆等级 -2
     * */
    val level: Int = 0,
    /**
     * 创建时间
     * */
    val createTime: Long = System.currentTimeMillis(),

    /**
     * 认识的次数
     * */
    val rememberCount: Int = 0,

    /**
     * 模糊的次数
     * */
    val vagueCount: Int = 0,
    /**
     * 忘记的次数
     * */
    val forgetCount: Int = 0,
    /**
     * 更新时间
     * */
    val updateTime: Long = System.currentTimeMillis(),
)