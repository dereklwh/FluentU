package com.example.group26.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [QuizData::class, FlashcardEntry::class, ProgressData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun progressTrackerDao(): ProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "language_learning_db"
                )
                    // Add a callback to prepopulate the database after it's created
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Prepopulate thread
                            CoroutineScope(Dispatchers.IO).launch {
                                prePopulateDatabase(getDatabase(context))
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Prepopulate the database
        suspend fun prePopulateDatabase(db: AppDatabase) {
            val quizDataList = listOf(
                QuizData(question = "What is the translation for 'Book'?",
                    multipleChoice = listOf("Dance", "Read", "Book", "Library"), correctAnswer = "Book"),
                QuizData(question = "What is the translation for 'Apple'?",
                    multipleChoice = listOf("Watermelon", "Apple", "Pear", "Orange"), correctAnswer = "Apple"),
                QuizData(question = "What is the translation for 'Water'?",
                    multipleChoice = listOf("Water", "Fire", "Stone", "Drink"), correctAnswer = "Water"),
                QuizData(question = "What is the translation for 'Mom'?",
                    multipleChoice = listOf("Brother", "Sister", "Dad", "Mom"), correctAnswer = "Mom"),
                QuizData(question = "What is the translation for 'Dad'?",
                    multipleChoice = listOf("Brother", "Sister", "Dad", "Mom"), correctAnswer = "Dad"),
                QuizData(question = "What is the translation for 'Sister'?",
                    multipleChoice = listOf("Brother", "Sister", "Dad", "Mom"), correctAnswer = "Sister"),
                QuizData(question = "What is the translation for 'Brother'?",
                    multipleChoice = listOf("Brother", "Sister", "Dad", "Mom"), correctAnswer = "Brother")
                // Add more quizzes as needed
            )

            db.quizDao().insertAll(quizDataList)

            //TODO: PREPOPULATE SOME FLASHCARD DATA
        }
    }
}
