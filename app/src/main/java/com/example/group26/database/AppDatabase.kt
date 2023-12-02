package com.example.group26.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.group26.Translator
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

            val translator = Translator("AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A")
            val foods: ArrayList<String> = ArrayList(
                listOf(
                    "Pizza",
                    "Burger",
                    "Pasta",
                    "Sushi",
                    "Salad",
                    "Ice Cream",
                    "Steak",
                    "Chicken Curry",
                    "Tacos",
                    "Smoothie"
                )
            )

            val sports: ArrayList<String> = ArrayList(
                listOf(
                    "Soccer",
                    "Basketball",
                    "Tennis",
                    "Golf",
                    "Swimming",
                    "Running",
                    "Cycling",
                    "Baseball",
                    "Volleyball",
                    "Hiking"
                )
            )

            val adjectives: ArrayList<String> = ArrayList(
                listOf(
                    "Happy",
                    "Exciting",
                    "Adventurous",
                    "Clever",
                    "Energetic",
                    "Gentle",
                    "Brilliant",
                    "Creative",
                    "Fierce",
                    "Inquisitive"
                )
            )

            val actions: ArrayList<String> = ArrayList(
                listOf(
                    "Run",
                    "Jump",
                    "Read",
                    "Write",
                    "Sing",
                    "Dance",
                    "Cook",
                    "Explore",
                    "Learn",
                    "Create"
                )
            )

            for(food in foods){
                CoroutineScope(Dispatchers.Main).launch {
                    val entry = FlashcardEntry(
                        englishPhrase = food,
                        frenchPhrase = translator.translateText(food, "fr"),
                        spanishPhrase = translator.translateText(food, "es"),
                        chinesePhrase = translator.translateText(food, "zh-CN"),
                        deckName = "Foods"
                    )
                    db.flashcardDao().insert(entry)
                }
            }

            for(sport in sports){
                CoroutineScope(Dispatchers.Main).launch {
                    val entry = FlashcardEntry(
                        englishPhrase = sport,
                        frenchPhrase = translator.translateText(sport, "fr"),
                        spanishPhrase = translator.translateText(sport, "es"),
                        chinesePhrase = translator.translateText(sport, "zh-CN"),
                        deckName = "Sports"
                    )
                    db.flashcardDao().insert(entry)
                }
            }

            for(adjective in adjectives){
                CoroutineScope(Dispatchers.Main).launch {
                    val entry = FlashcardEntry(
                        englishPhrase = adjective,
                        frenchPhrase = translator.translateText(adjective, "fr"),
                        spanishPhrase = translator.translateText(adjective, "es"),
                        chinesePhrase = translator.translateText(adjective, "zh-CN"),
                        deckName = "Adjectives"
                    )
                    db.flashcardDao().insert(entry)
                }
            }

            for(action in actions){
                CoroutineScope(Dispatchers.Main).launch {
                    val entry = FlashcardEntry(
                        englishPhrase = action,
                        frenchPhrase = translator.translateText(action, "fr"),
                        spanishPhrase = translator.translateText(action, "es"),
                        chinesePhrase = translator.translateText(action, "zh-CN"),
                        deckName = "Actions"
                    )
                    db.flashcardDao().insert(entry)
                }
            }
        }
    }
}
