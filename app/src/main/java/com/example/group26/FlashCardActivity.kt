package com.example.group26

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.group26.database.FlashcardEntry
import com.example.group26.viewmodels.FlashCardViewModel
import com.example.group26.viewmodels.FlashCardViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FlashCardActivity: AppCompatActivity() {
    private val api: String = "AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A"
    private lateinit var viewModel: FlashCardViewModel
    private lateinit var translator:Translator
    private lateinit var language:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)
        val factory = FlashCardViewModelFactory(this.application)
        viewModel = ViewModelProvider(this, factory).get(FlashCardViewModel::class.java)
        translator = Translator(api)
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        language = sharedPreferences.getString("language_preference", "en") ?: "en"
        setupAddFlashCardButton()
        setupObservers()
        loadFlashCards()
    }

    private fun loadFlashCards()
    {
        val flashcardsContainer: LinearLayout = findViewById(R.id.flashcardsContainer)
        flashcardsContainer.removeAllViews()
        val flashcards: List<FlashcardEntry>? = viewModel.allFlashCards.value
        flashcards?.let { list ->
            for (flashcard in list) {
                addFlashcard(flashcard)
            }
        }
    }
    private fun setupAddFlashCardButton(){
        val button:Button = findViewById(R.id.addFlashcardButton)
        button.setOnClickListener(){
            val editText:EditText = findViewById(R.id.flashCardEditText)
            val inputString = editText.text.toString()
            if(inputString != ""){
                CoroutineScope(Dispatchers.Main).launch {
                        val entry = FlashcardEntry(
                            englishPhrase = inputString,
                            frenchPhrase = translator.translateText(inputString, "fr"),
                            spanishPhrase = translator.translateText(inputString, "es"),
                            chinesePhrase = translator.translateText(inputString, "zh-CN")
                        )
                    viewModel.insertFlashCard(entry)
                }
            }
        }
    }

    private fun setupObservers(){
        viewModel.allFlashCards.observe(this) { flashcards ->
            val flashcard = flashcards.getOrNull(flashcards.size - 1)
            if (flashcard != null) {
                loadFlashCards()
            }
        }
    }

    private fun addFlashcard(word:FlashcardEntry) {
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
            viewModel.deleteFlashCard(word)
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
        flashCardWord.text = word.englishPhrase
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
            if(language == "fr"){
                if(flashCardWord.text == word.frenchPhrase){
                    flashCardWord.text = word.englishPhrase
                }
                else{
                    flashCardWord.text = word.frenchPhrase
                }
            }
            else if(language == "es"){
                if(flashCardWord.text == word.spanishPhrase){
                    flashCardWord.text = word.englishPhrase
                }
                else{
                    flashCardWord.text = word.spanishPhrase
                }
            }
            else if(language == "zh-CN"){
                if(flashCardWord.text == word.chinesePhrase){
                    flashCardWord.text = word.englishPhrase
                }
                else{
                    flashCardWord.text = word.chinesePhrase
                }
            }
        }
    }
}
