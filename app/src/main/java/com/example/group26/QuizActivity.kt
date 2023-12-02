package com.example.group26

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.group26.database.AppDatabase
import com.example.group26.database.QuizData
import com.example.group26.viewmodels.QuizViewModel
import com.example.group26.viewmodels.QuizViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private lateinit var viewModel: QuizViewModel
    private lateinit var submitButton: Button
    private val translator = Translator("AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A")
    private val originalToTranslatedMap = mutableMapOf<String, String>() //keep track of translations
    private var currentQuizzes: List<QuizData> = emptyList()
    private lateinit var language: String
    private lateinit var proficiencyLevel: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        submitButton = findViewById(R.id.submitButton)
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        language = sharedPreferences.getString("language_preference", "en") ?: "en"
        proficiencyLevel = sharedPreferences.getString("proficiency_level", "beginner") ?: "beginner"
        val difficulty = getDifficultyLevel(proficiencyLevel)

        //set UI based on proficiency
        val quizTitleTextView = findViewById<TextView>(R.id.quiz_id)
        quizTitleTextView.text = getFormattedQuizTitle(proficiencyLevel)

        // Initialize the ViewModel
        val factory = QuizViewModelFactory(this.application)
        viewModel = ViewModelProvider(this, factory).get(QuizViewModel::class.java)

        setupObservers(difficulty)

        //TODO: RETRIEVE QUIZ SCORE AND STORE IN DATABASE FOR VISUALIZATION
        submitButton.setOnClickListener{
            Log.d("QUIZ_SUBMIT", "Submit button clicked")
            val score = computeScore()
            finish()
        }
    }

    private fun setupObservers(difficulty: Int) {
        viewModel.getRandomQuizzes(difficulty).observe(this, { quizzes ->
            currentQuizzes = quizzes // Update the current quizzes
            // Update the UI with the quizzes
            quizzes.forEachIndexed { index, quiz ->
                when (index) {
                    0 -> updateQuizUI(quiz, R.id.question1TextView, R.id.question1Options)
                    1 -> updateQuizUI(quiz, R.id.question2TextView, R.id.question2Options)
                    2 -> updateQuizUI(quiz, R.id.question3TextView, R.id.question3Options)
                    3 -> updateQuizUI(quiz, R.id.question4TextView, R.id.question4Options)
                    4 -> updateQuizUI(quiz, R.id.question5TextView, R.id.question5Options)
                }
            }
        })
    }

    private fun updateQuizUI(quiz: QuizData, textViewId: Int, radioGroupId: Int) {
        val questionTextView = findViewById<TextView>(textViewId)

        questionTextView.text = quiz.question
        translateAndSetOptions(quiz.multipleChoice, radioGroupId)
    }
    private fun translateAndSetOptions(options: List<String>, radioGroupId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val translatedOptions = options.map { option ->
                val translatedText = translator.translateText(option, language) // depends on the language they choose in sharedpreferrences
                originalToTranslatedMap[option] = translatedText
                translatedText
            }

            val radioGroup = findViewById<RadioGroup>(radioGroupId)
            translatedOptions.forEachIndexed { index, translatedOption ->
                (radioGroup.getChildAt(index) as? RadioButton)?.text = translatedOption
            }
        }
    }

    // Helper function to get the selected answer for a given question index.
    private fun getSelectedAnswerForQuestion(questionIndex: Int): String {
        val radioGroupId = when (questionIndex) {
            0 -> R.id.question1Options
            1 -> R.id.question2Options
            2 -> R.id.question3Options
            3 -> R.id.question4Options
            4 -> R.id.question5Options
            else -> return "" // Return an empty string if the index is out of bounds
        }
        val radioGroup = findViewById<RadioGroup?>(radioGroupId)
        return if (radioGroup != null && radioGroup.checkedRadioButtonId != -1) {
            findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
        } else {
            "" // Empty string if the radio group is null or no button is selected
        }
    }


    // Function to compute the quiz score
    private fun computeScore(): Int {
        if (currentQuizzes.isEmpty()) return 0

        var score = 0
        val totalQuestions = currentQuizzes.size

        currentQuizzes.forEachIndexed { index, quizData ->
            val selectedAnswerTranslated = getSelectedAnswerForQuestion(index)
            val selectedAnswerOriginal = originalToTranslatedMap.entries.find { it.value == selectedAnswerTranslated }?.key
            Log.d("SELECTED ANSWER", "$selectedAnswerOriginal and ${quizData.correctAnswer}")
            if (quizData.correctAnswer == selectedAnswerOriginal) {
                score++
            }
        }

        Toast.makeText(this, "Your score: $score / $totalQuestions", Toast.LENGTH_LONG).show()
        return score
    }

    private fun getFormattedQuizTitle(proficiencyLevel: String): String {
        return when (proficiencyLevel) {
            "Beginner" -> getString(R.string.quiz_title_beginner)
            "Intermediate" -> getString(R.string.quiz_title_intermediate)
            "Advanced" -> getString(R.string.quiz_title_advanced)
            else -> getString(R.string.quiz_title_beginner)
        }
    }

    private fun getDifficultyLevel(proficiencyLevel: String): Int {
         return when (proficiencyLevel) {
            "Beginner" -> 0
            "Intermediate" -> 1
            "Advanced" -> 2
            else -> 0
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

