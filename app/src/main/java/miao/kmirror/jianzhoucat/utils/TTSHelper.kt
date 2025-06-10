package miao.kmirror.jianzhoucat.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TTSHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    private var pendingText: String? = null

    init {
        initializeTTS()
    }

    private fun initializeTTS() {
        textToSpeech = TextToSpeech(applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale.CHINA) ?: TextToSpeech.LANG_MISSING_DATA

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("KmirrorTag", "语言设置失败")
            } else {
                isInitialized = true
                Log.i("KmirrorTag", "TTS 初始化成功")
                pendingText?.let { speak(it) }
                pendingText = null
            }
        } else {
            Log.e("KmirrorTag", "TTS 初始化失败")
        }
    }

    fun speak(text: String) {
        if (!isInitialized) {
            pendingText = text
            return
        }

        textToSpeech?.speak(text, TextToSpeech.QUEUE_ADD, null, null)
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false
    }

    fun setSpeechRate(rate: Float) {
        textToSpeech?.setSpeechRate(rate)
    }

    fun setPitch(pitch: Float) {
        textToSpeech?.setPitch(pitch)
    }

    fun isSpeaking(): Boolean {
        return textToSpeech?.isSpeaking == true
    }
}