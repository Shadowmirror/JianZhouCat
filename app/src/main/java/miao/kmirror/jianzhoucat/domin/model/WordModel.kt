package miao.kmirror.jianzhoucat.domin.model

import miao.kmirror.jianzhoucat.data.local.entity.ExampleSentence
import miao.kmirror.jianzhoucat.data.local.entity.WordDTO
import kotlin.random.Random

data class WordModel(
    val word: String,
    val translate: String,
    val phoneticSymbol: String?,
    val exampleSentence: List<ExampleSentence>?,
    val customMemory: String?,
    val level: Int,
    val createTime: Long,
    val rememberCount: Int,
    val vagueCount: Int,
    val forgetCount: Int,
    val updateTime: Long,
) {
    companion object {
        fun getMock(): WordModel {
            val random = Random(System.currentTimeMillis())
            val wordIndex = random.nextInt(1000, 9999)

            val sentenceOptions = listOf(
                null,
                emptyList(),
                listOf(
                    ExampleSentence("This is a sample sentence.", "这是一个例句。")
                ),
                listOf(
                    ExampleSentence("First example.", "第一个例句。"),
                    ExampleSentence("Second example.", "第二个例句。")
                ),
                listOf(
                    ExampleSentence("One.", "一。"),
                    ExampleSentence("Two.", "二。"),
                    ExampleSentence("Three.", "三。")
                )
            )

            return WordModel(
                word = "word_$wordIndex",
                translate = "翻译_$wordIndex",
                phoneticSymbol = if (random.nextBoolean()) "[fəʊnɛtɪk]" else null,
                exampleSentence = sentenceOptions.random(),
                customMemory = if (random.nextBoolean()) "memory_$wordIndex" else null,
                level = random.nextInt(-2, 6),
                createTime = System.currentTimeMillis() - random.nextLong(0L, 1_000_000L),
                rememberCount = random.nextInt(0, 10),
                vagueCount = random.nextInt(0, 5),
                forgetCount = random.nextInt(0, 3),
                updateTime = System.currentTimeMillis()
            )
        }


        fun getWordModelByWordDTO(wordDTO: WordDTO): WordModel {
            return WordModel(
                word = wordDTO.word,
                translate = wordDTO.translate,
                phoneticSymbol = wordDTO.phoneticSymbol,
                exampleSentence = wordDTO.exampleSentence,
                customMemory = wordDTO.customMemory,
                level = 0,
                createTime = System.currentTimeMillis(),
                rememberCount = 0,
                vagueCount = 0,
                forgetCount = 0,
                updateTime = System.currentTimeMillis()
            )
        }
    }
}
