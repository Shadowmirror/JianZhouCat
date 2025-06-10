package miao.kmirror.jianzhoucat.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import miao.kmirror.jianzhoucat.data.local.entity.ExampleSentence

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromExampleSentenceList(list: List<ExampleSentence>?): String? {
        return list?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toExampleSentenceList(jsonString: String?): List<ExampleSentence>? {
        return jsonString?.let {
            try {
                json.decodeFromString(it)
            } catch (e: Exception) {
                null // 解析失败返回 null
            }
        }
    }
}