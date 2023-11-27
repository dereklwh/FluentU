package com.example.group26.database

import androidx.lifecycle.LiveData

class AppRepository(
    private val quizDao: QuizDao,
    private val flashcardDao: FlashcardDao,
    private val progressTrackerDao: ProgressDao
) {
    // For Quiz
    val allQuizzes: LiveData<List<QuizData>> = quizDao.getAllQuizzes()
    fun insert(quizData: QuizData) {
        quizDao.insert(quizData)
    }

    fun getRandomQuizzes(): List<QuizData> {
        return quizDao.getRandomQuizzes()
    }


    // For Flashcard
    val allFlashcards: LiveData<List<FlashcardEntry>> = flashcardDao.getAllFlashcards()
    fun insert(flashcardEntry: FlashcardEntry) {
        flashcardDao.insert(flashcardEntry)
    }


    // For ProgressTracker
    val allProgress: LiveData<List<ProgressData>> = progressTrackerDao.getAllProgress()
    fun insert(progressData: ProgressData) {
        progressTrackerDao.insert(progressData)
    }

    // Add more methods for progress tracker operations
}
