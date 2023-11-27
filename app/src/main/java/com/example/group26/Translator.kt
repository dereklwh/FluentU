package com.example.group26

import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Translator(private val apiKey: String) {
    suspend fun translateText(text: String, targetLanguage: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val translate: Translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().service
                val translation: Translation = translate.translate(text, Translate.TranslateOption.targetLanguage(targetLanguage))
                translation.translatedText
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: ${e.localizedMessage}"
            }
        }
    }
}