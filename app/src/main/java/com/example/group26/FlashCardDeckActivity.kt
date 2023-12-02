package com.example.group26

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.group26.database.FlashcardEntry
import com.example.group26.viewmodels.FlashCardViewModel
import com.example.group26.viewmodels.FlashCardViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FlashCardDeckActivity: AppCompatActivity() {
    private lateinit var viewModel: FlashCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards_deck)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val editText = findViewById<EditText>(R.id.flashCardDeckEditText)
        val button = findViewById<Button>(R.id.addFlashcardDeckButton)
        val factory = FlashCardViewModelFactory(this.application)
        viewModel = ViewModelProvider(this, factory).get(FlashCardViewModel::class.java)
        setupObservers()

        initializeTableFromDatabase()

        button.setOnClickListener(){
            if(editText.text.toString() != ""){
                CoroutineScope(Dispatchers.Main).launch {
                    val entry = FlashcardEntry(
                        englishPhrase = "",
                        frenchPhrase = "",
                        spanishPhrase = "",
                        chinesePhrase = "",
                        deckName = editText.text.toString()
                    )
                    viewModel.insertFlashCard(entry)
                }
                initializeTableFromDatabase()
            }
        }
    }

    private fun setupObservers(){
        viewModel.allFlashCards.observe(this) { flashcards ->
            initializeTableFromDatabase()
        }
    }

    private fun initializeTableFromDatabase(){
        val tableLayout = findViewById<TableLayout>(R.id.flashcardDeckTable)
        tableLayout.removeAllViews()
        val uniqueDeckName: MutableSet<String> = HashSet()
        val flashcards: List<FlashcardEntry>? = viewModel.allFlashCards.value

        flashcards?.let { list ->
            for (flashcard in list) {
                uniqueDeckName.add(flashcard.deckName)
            }
        }

        for(deck in uniqueDeckName){
            val tableRow = TableRow(this)
            val deckName = TextView(this)
            deckName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)

            deckName.text = deck
            tableRow.addView(deckName)
            tableRow.setBackgroundResource(R.drawable.flashcard_table_border)
            tableRow.gravity =  Gravity.CENTER

            tableLayout.addView(tableRow)

            tableRow.setOnClickListener(){
                val intent = Intent(this, FlashCardActivity::class.java)
                intent.putExtra("deckName", deckName.text.toString())
                startActivity(intent)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return true
    }

}