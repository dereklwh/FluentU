package com.example.group26

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
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
import java.util.Locale


class FlashCardActivity: AppCompatActivity() {
    private val api: String = "AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A"
    private lateinit var viewModel: FlashCardViewModel
    private lateinit var translator:Translator
    private lateinit var language:String
    private lateinit var textToSpeech: TextToSpeech
    private var deckName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val factory = FlashCardViewModelFactory(this.application)
        viewModel = ViewModelProvider(this, factory).get(FlashCardViewModel::class.java)
        translator = Translator(api)
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        language = sharedPreferences.getString("language_preference", "en") ?: "en"
        deckName = intent.getStringExtra("deckName").toString()
        setupAddFlashCardButton()
        setupObservers()
        loadFlashCards()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.flashcard_delete, menu)
        return true
    }

    private fun loadFlashCards()
    {
        val flashcardsContainer: LinearLayout = findViewById(R.id.flashcardsContainer)
        flashcardsContainer.removeAllViews()
        val flashcards: List<FlashcardEntry>? = viewModel.allFlashCards.value
        flashcards?.let { list ->
            for (flashcard in list) {
                if(flashcard.englishPhrase != "" && flashcard.deckName == deckName){
                    addFlashcard(flashcard)
                }
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
                        chinesePhrase = translator.translateText(inputString, "zh-CN"),
                        deckName = deckName
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
        deleteButton.setTextColor(resources.getColor(R.color.black))
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

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = when (language) {
                    "fr" -> Locale.FRENCH
                    "es" -> Locale("es", "ES")
                    "zh-CN" -> Locale.CHINESE
                    else -> Locale.getDefault()
                }

                // set the language and accent
                textToSpeech.language = locale
            }
        }

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
                    textToSpeech.speak(flashCardWord.text.toString().trim(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
            else if(language == "es"){
                if(flashCardWord.text == word.spanishPhrase){
                    flashCardWord.text = word.englishPhrase
                }
                else{
                    flashCardWord.text = word.spanishPhrase
                    textToSpeech.speak(flashCardWord.text.toString().trim(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
            else if(language == "zh-CN"){
                if(flashCardWord.text == word.chinesePhrase){
                    flashCardWord.text = word.englishPhrase
                }
                else{
                    flashCardWord.text = word.chinesePhrase
                    textToSpeech.speak(flashCardWord.text.toString().trim(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_delete -> {
                viewModel.deleteDeck(deckName)
                finish()
                return true
            }
        }
        return true
    }
}