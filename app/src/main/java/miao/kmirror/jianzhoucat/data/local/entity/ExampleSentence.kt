package miao.kmirror.jianzhoucat.data.local.entity

import kotlinx.serialization.Serializable

/**
 * 例句数据结构
 * */

@Serializable
data class ExampleSentence(
    // 例句英文
    val english: String,
    // 例句中文
    val chinese: String
)