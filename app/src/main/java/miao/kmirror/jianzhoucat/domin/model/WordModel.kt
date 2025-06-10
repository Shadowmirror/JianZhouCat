package miao.kmirror.jianzhoucat.domin.model

import java.util.Random

data class WordModel(
    val word: String,
    val translate: String,
    val phoneticSymbol: String,
    val exampleSentence: String,
    val level: Int
) {
    companion object {
        fun getMock(): WordModel {
            return WordModel(
                word = "Word ${Random().nextInt(4500)}",
                translate = "translate ${Random().nextInt(4500)}",
                phoneticSymbol = "phoneticSymbol ${Random().nextInt(4500)}",
                exampleSentence = "This is a ${
                    buildString {
                        repeat(Random().nextInt(20)) { append("long ") }
                    }
                }Sentence ${System.currentTimeMillis()}",
                level = 0
            )
        }
    }
}