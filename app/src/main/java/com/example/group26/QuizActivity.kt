package com.example.group26

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
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
                translator.translateText(option, "zh-CN") // depends on language they choose in sharedprefferences
            }

            val radioGroup = findViewById<RadioGroup>(radioGroupId)
            translatedOptions.forEachIndexed { index, translatedOption ->
                (radioGroup.getChildAt(index) as? RadioButton)?.text = translatedOption
            }
        }
    }

}

