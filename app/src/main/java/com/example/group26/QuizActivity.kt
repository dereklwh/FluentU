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

class QuizActivity : AppCompatActivity() {

    private lateinit var viewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize the ViewModel
        val factory = QuizViewModelFactory(this.application)
        viewModel = ViewModelProvider(this, factory).get(QuizViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers() {
        // Observe the quiz data
        viewModel.allQuizzes.observe(this, Observer { quizzes ->
            // Update the UI with quiz data
            // For simplicity, let's assume we're only handling two questions
            val question1 = quizzes.getOrNull(0)
            val question2 = quizzes.getOrNull(1)

            findViewById<TextView>(R.id.question1TextView).text = question1?.question
            findViewById<TextView>(R.id.question2TextView).text = question2?.question
            // Set radio button texts for question 1 options

            question1?.multipleChoice?.let { options ->
                for (i in 0 until options.size) {
                    val radioButton = (findViewById<RadioGroup>(R.id.question1Options).getChildAt(i) as RadioButton)
                    radioButton.text = options[i]
                }
            }

            question2?.multipleChoice?.let { options ->
                for (i in 0 until options.size) {
                    val radioButton = (findViewById<RadioGroup>(R.id.question2Options).getChildAt(i) as RadioButton)
                    radioButton.text = options[i]
                }
            }
        })
    }

}

