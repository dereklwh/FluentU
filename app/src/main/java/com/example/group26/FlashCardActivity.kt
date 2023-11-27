package com.example.group26

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.group26.database.FlashcardEntry
import com.example.group26.viewmodels.FlashCardViewModel
import com.example.group26.viewmodels.FlashCardViewModelFactory
import com.example.group26.viewmodels.QuizViewModel
import com.example.group26.viewmodels.QuizViewModelFactory
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashCardActivity: AppCompatActivity() {
    private val api: String = "AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A"
    private lateinit var viewModel: FlashCardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)
        val factory = FlashCardViewModelFactory(this.application)
        viewModel = ViewModelProvider(this, factory).get(FlashCardViewModel::class.java)

        setupAddFlashCardButton()
        setupObservers()
    }

    private fun setupAddFlashCardButton(){
        val button:Button = findViewById(R.id.addFlashcardButton)
        button.setOnClickListener(){
            val editText:EditText = findViewById(R.id.flashCardEditText)
            val inputString = editText.text.toString()
            if(inputString != ""){
                val entry = FlashcardEntry(
                    englishPhrase = inputString,
                    frenchPhrase = translateText(inputString, "fr"),
                    spanishPhrase = translateText(inputString, "es"),
                    chinesePhrase = translateText(inputString, "zh")
                )
                viewModel.insertFlashCard(entry)
            }
        }
    }

    private fun setupObservers(){
        viewModel.allFlashCards.observe(this, Observer{flashcards ->
            val flashcard = flashcards.getOrNull(flashcards.size - 1)
            if (flashcard != null) {
                addFlashcard(flashcard.englishPhrase, flashcard.id)
            }
        })
    }
    private fun addFlashcard(word:String, id:Long) {
        val flashcardsContainer: LinearLayout = findViewById(R.id.flashcardsContainer)

        val flashCard = LinearLayout(this)
        flashCard.orientation = LinearLayout.VERTICAL
        flashCard.setBackgroundResource(R.drawable.flashcard_border)
        flashCard.setPadding(0, 0, 0, 80)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 0, 30)
        flashCard.layoutParams = layoutParams

        val deleteButton = Button(this)
        deleteButton.text = "X"
        deleteButton.setBackgroundColor(Color.TRANSPARENT)
        deleteButton.setOnClickListener {
            flashcardsContainer.removeView(flashCard)
        }
        val deleteParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        deleteParams.gravity = Gravity.END
        deleteButton.layoutParams = deleteParams
        flashCard.addView(deleteButton)

        val flashCardWord = TextView(this)
        flashCardWord.setTypeface(null, Typeface.BOLD)
        flashCardWord.text = word
        flashCardWord.gravity = Gravity.CENTER
        flashCard.addView(flashCardWord)

        val flashCardSubText = TextView(this)
        flashCardSubText.text = "Click To Reveal"
        flashCardSubText.gravity = Gravity.CENTER
        flashCard.addView(flashCardSubText)


        flashcardsContainer.addView(flashCard)

        //animation from chatgpt
        flashCard.setOnClickListener {
            flashCard.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                flashCard.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
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
