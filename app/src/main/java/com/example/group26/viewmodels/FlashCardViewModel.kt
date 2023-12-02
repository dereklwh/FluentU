package com.example.group26.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.group26.database.AppDatabase
import com.example.group26.database.AppRepository
import com.example.group26.database.FlashcardEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlashCardViewModel(application: Application, private val repository: AppRepository) : AndroidViewModel(application) {
    val allFlashCards = repository.allFlashcards

    fun insertFlashCard(FlashCardEntry: FlashcardEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(FlashCardEntry)
    }

    fun deleteFlashCard(FlashCardEntry: FlashcardEntry) =viewModelScope.launch(Dispatchers.IO){
        repository.delete(FlashCardEntry)
    }

    fun deleteDeck(deckName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDeck(deckName)
        }
    }
}

class FlashCardViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashCardViewModel::class.java)) {
            val db = AppDatabase.getDatabase(application)
            val quizDao = db.quizDao()
            val flashcardDao = db.flashcardDao()
            val progressTrackerDao = db.progressTrackerDao()
            val repository = AppRepository(quizDao, flashcardDao, progressTrackerDao)
            @Suppress("UNCHECKED_CAST")
            return FlashCardViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
