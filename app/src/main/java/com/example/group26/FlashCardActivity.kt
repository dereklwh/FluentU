package com.example.group26

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashCardActivity: AppCompatActivity() {

    private lateinit var flashcard2: TextView
    private val api: String = "AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)

        flashcard2 = findViewById(R.id.flashcard2)
        val flashcardText = flashcard2.text.toString()

        val targetLang = "ar"

        CoroutineScope(Dispatchers.IO).launch {
            val translatedText = translateText(flashcardText, targetLang)
            withContext(Dispatchers.Main) {
                if (!translatedText.startsWith("Error:")) {
                    flashcard2.text = translatedText
                } else {
                    // Handle error, show message to user etc.
                }
            }
        }
    }

    private fun translateText(text: String, targetLanguage: String): String {
        return try {
            val translate: Translate = TranslateOptions.newBuilder().setApiKey(api).build().service
            val translation: Translation = translate.translate(text, Translate.TranslateOption.targetLanguage(targetLanguage))
            translation.translatedText
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.localizedMessage}"
        }
    }
}
