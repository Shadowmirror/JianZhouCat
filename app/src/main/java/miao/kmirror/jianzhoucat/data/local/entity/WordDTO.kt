package miao.kmirror.jianzhoucat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import miao.kmirror.jianzhoucat.data.local.converter.Converters

@Serializable
@Entity
@TypeConverters(Converters::class)
data class WordDTO(
    /**
     * 单词内容
     * */
    @PrimaryKey val word: String,
    /**
     * 翻译
     * */
    val translate: String,
    /**
     * 音标, Json 中可能没有英标
     * */
    val phoneticSymbol: String?,
    /**
     * 例句, 是 ExampleSentence 数组，也就是 Json 数据, Json 中可能没有例句
     * */
    val exampleSentence: List<ExampleSentence>?,

    /**
     * 自定义记忆内容，String 对象， Json 中可能没有
     * */
    val customMemory: String?
)