package com.example.group26.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.group26.database.AppDatabase
import com.example.group26.database.AppRepository
import com.example.group26.database.QuizData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizViewModel(application: Application, private val repository: AppRepository) : AndroidViewModel(application) {
    val allQuizzes = repository.allQuizzes

    fun insertQuiz(quizData: QuizData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(quizData)
    }
    init {
        insertQuiz(QuizData(question = "What is the translation for 'Book'?",
            multipleChoice = listOf("Dance", "Read", "Book", "Library"), correctAnswer = "Book"))
        insertQuiz(QuizData(question = "What is the translation for 'Apple'?",
            multipleChoice = listOf("Watermelon", "Apple", "Pear", "Orange"), correctAnswer = "Apple"))
        insertQuiz(QuizData(question = "What is the translation for 'Water'?",
            multipleChoice = listOf("Water", "Fire", "Stone", "Drink"), correctAnswer = "Water"))
    }

}

class QuizViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            val db = AppDatabase.getDatabase(application)
            val quizDao = db.quizDao()
            val flashcardDao = db.flashcardDao()
            val progressTrackerDao = db.progressTrackerDao()
            val repository = AppRepository(quizDao, flashcardDao, progressTrackerDao)
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

