package com.example.group26

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.group26.viewmodels.QuizViewModel
import com.example.group26.viewmodels.QuizViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private lateinit var viewModel: QuizViewModel
    private lateinit var submitButton: Button
    private val translator = Translator("AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A")
    private val originalToTranslatedMap = mutableMapOf<String, String>() //keep track of translations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        submitButton = findViewById(R.id.submitButton)

        // Initialize the ViewModel
        val factory = QuizViewModelFactory(this.application)
        viewModel = ViewModelProvider(this, factory).get(QuizViewModel::class.java)

        setupObservers()

        //TODO: RETRIEVE QUIZ SCORE AND STORE IN DATABASE
        //
        submitButton.setOnClickListener{
            Log.d("QUIZ_SUBMIT", "Submit button clicked")
            val score = computeScore()
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.allQuizzes.observe(this, Observer { quizzes ->
            // Assuming we're handling two questions for simplicity
            val question1 = quizzes.getOrNull(0)
            val question2 = quizzes.getOrNull(1)

            findViewById<TextView>(R.id.question1TextView).text = question1?.question
            findViewById<TextView>(R.id.question2TextView).text = question2?.question

            question1?.multipleChoice?.let { options ->
                translateAndSetOptions(options, R.id.question1Options)
            }

            question2?.multipleChoice?.let { options ->
                translateAndSetOptions(options, R.id.question2Options)
            }
        })
    }

    private fun translateAndSetOptions(options: List<String>, radioGroupId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val translatedOptions = options.map { option ->
                val translatedText = translator.translateText(option, "zh-CN") // depends on the language they choose in sharedpreferrences
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
            // Add more cases as needed for additional questions.
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
        val quizzes = viewModel.allQuizzes.value ?: return 0
        var score = 0
        val totalQuestions = quizzes.size

        quizzes.forEachIndexed { index, quizData ->
            val selectedAnswerTranslated = getSelectedAnswerForQuestion(index)
            // Reverse lookup the original English option from the translated one
            val selectedAnswerOriginal = originalToTranslatedMap.entries.find { it.value == selectedAnswerTranslated }?.key
            Log.d("SELECTED ANSWER", "$selectedAnswerOriginal and ${quizData.correctAnswer}")
            if (quizData.correctAnswer == selectedAnswerOriginal) {
                score++
            }
        }

        // Show the score out of the total number of questions
        Toast.makeText(this, "Your score: $score / $totalQuestions", Toast.LENGTH_LONG).show()

        return score
    }



}

