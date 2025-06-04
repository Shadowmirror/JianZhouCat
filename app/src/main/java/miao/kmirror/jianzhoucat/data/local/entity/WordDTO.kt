package miao.kmirror.jianzhoucat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Random

@Entity
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
     * 音标
     * */
    val phoneticSymbol: String,
    /**
     * 例句
     * */
    val exampleSentence: String
) {
    companion object {
        fun getMock(): WordDTO {
            return WordDTO(
                word = "Word${Random().nextInt(4500)}",
                translate = "translate${Random().nextInt(4500)}",
                phoneticSymbol = "phoneticSymbol${Random().nextInt(4500)}",
                exampleSentence = "This is a ${
                    buildString {
                        repeat(Random().nextInt(20)) { append("long ") }
                    }
                }Sentence ${System.currentTimeMillis()}"
            )
        }
    }
}