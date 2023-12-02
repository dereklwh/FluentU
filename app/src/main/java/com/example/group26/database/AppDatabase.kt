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
                //EASY QUESTIONS (difficulty 0)
                QuizData(question = "What is the translation for 'Book'?",
                    multipleChoice = listOf("Dance", "Read", "Book", "Library"), correctAnswer = "Book", difficulty = 0),
                QuizData(question = "What is the translation for 'Apple'?",
                    multipleChoice = listOf("Watermelon", "Apple", "Pear", "Orange"), correctAnswer = "Apple", difficulty = 0),
                QuizData(question = "What is the translation for 'Water'?",
                    multipleChoice = listOf("Water", "Fire", "Stone", "Drink"), correctAnswer = "Water", difficulty = 0),
                QuizData(question = "What is the translation for 'Mom'?",
                    multipleChoice = listOf("Brother", "Sister", "Dad", "Mom"), correctAnswer = "Mom", difficulty = 0),
                QuizData(question = "What is the translation for 'Dad'?",
                    multipleChoice = listOf("Brother", "Sister", "Dad", "Mom"), correctAnswer = "Dad", difficulty = 0),
                QuizData(question = "What is the translation for 'Sister'?",
                    multipleChoice = listOf("Brother", "Sister", "Dad", "Mom"), correctAnswer = "Sister", difficulty = 0),
                QuizData(question = "What is the translation for 'Brother'?",
                    multipleChoice = listOf("Brother", "Sister", "Dad", "Mom"), correctAnswer = "Brother", difficulty = 0),
                QuizData(question = "What is the translation for 'Hello'?",
                    multipleChoice = listOf("Hello", "Bye", "Good", "Bad"), correctAnswer = "Hello", difficulty = 0),
                QuizData(question = "What is the translation for 'Bad'?",
                    multipleChoice = listOf("Hello", "Bye", "Good", "Bad"), correctAnswer = "Bad", difficulty = 0),
                QuizData(question = "What is the translation for 'Good'?",
                    multipleChoice = listOf("Hello", "Bye", "Good", "Bad"), correctAnswer = "Good", difficulty = 0),
                QuizData(question = "What is the translation for 'Thank you'?",
                    multipleChoice = listOf("Thank you", "Excuse me", "How are you", "Sorry"), correctAnswer = "Thank you", difficulty = 0),
                QuizData(question = "What is the translation for 'Excuse me'?",
                    multipleChoice = listOf("Thank you", "Excuse me", "How are you", "Sorry"), correctAnswer = "Excuse me", difficulty = 0),
                QuizData(question = "What is the translation for 'How are you'?",
                    multipleChoice = listOf("Thank you", "Excuse me", "How are you", "Sorry"), correctAnswer = "How are you", difficulty = 0),
                QuizData(question = "What is the translation for 'Sorry'?",
                    multipleChoice = listOf("Thank you", "Excuse me", "How are you", "Sorry"), correctAnswer = "Sorry", difficulty = 0),
                QuizData(question = "What is the translation for 'My name is'?",
                    multipleChoice = listOf("My name is", "Washroom", "I am", "Please"), correctAnswer = "My name is", difficulty = 0),
                QuizData(question = "What is the translation for 'Washroom'?",
                    multipleChoice = listOf("My name is", "Washroom", "I am", "Please"), correctAnswer = "Washroom", difficulty = 0),
                QuizData(question = "What is the translation for 'I am'?",
                    multipleChoice = listOf("My name is", "Washroom", "I am", "Please"), correctAnswer = "I am", difficulty = 0),
                QuizData(question = "What is the translation for 'Please'?",
                    multipleChoice = listOf("My name is", "Washroom", "I am", "Please"), correctAnswer = "Please", difficulty = 0),
                QuizData(question = "What is the translation for 'Sun'?",
                    multipleChoice = listOf("Moon", "Star", "Sun", "Sky"), correctAnswer = "Sun", difficulty = 0),
                QuizData(question = "What is the translation for 'Moon'?",
                    multipleChoice = listOf("Moon", "Star", "Sun", "Sky"), correctAnswer = "Moon", difficulty = 0),
                QuizData(question = "What is the translation for 'Rain'?",
                    multipleChoice = listOf("Cloud", "Storm", "Snow", "Rain"), correctAnswer = "Rain", difficulty = 0),
                QuizData(question = "What is the translation for 'Snow'?",
                    multipleChoice = listOf("Cloud", "Storm", "Snow", "Rain"), correctAnswer = "Snow", difficulty = 0),
                QuizData(question = "What is the translation for 'Cloud'?",
                    multipleChoice = listOf("Cloud", "Storm", "Snow", "Rain"), correctAnswer = "Cloud", difficulty = 0),
                QuizData(question = "What is the translation for 'Happy'?",
                    multipleChoice = listOf("Sad", "Angry", "Happy", "Excited"), correctAnswer = "Happy", difficulty = 0),
                QuizData(question = "What is the translation for 'Sad'?",
                    multipleChoice = listOf("Sad", "Angry", "Happy", "Excited"), correctAnswer = "Sad", difficulty = 0),
                QuizData(question = "What is the translation for 'Angry'?",
                    multipleChoice = listOf("Sad", "Angry", "Happy", "Excited"), correctAnswer = "Angry", difficulty = 0),
                QuizData(question = "What is the translation for 'Excited'?",
                    multipleChoice = listOf("Sad", "Angry", "Happy", "Excited"), correctAnswer = "Excited", difficulty = 0),



                // INTERMEDIATE QUESTIONS
                QuizData(question = "What is the translation for 'Ran'?",
                    multipleChoice = listOf("Running", "Ran", "Will run", "Run"), correctAnswer = "Ran", difficulty = 1),
                QuizData(question = "What is the translation for 'Running'?",
                    multipleChoice = listOf("Running", "Ran", "Will run", "Run"), correctAnswer = "Running", difficulty = 1),
                QuizData(question = "What is the translation for 'Run'?",
                    multipleChoice = listOf("Running", "Ran", "Will run", "Run"), correctAnswer = "Run", difficulty = 1),
                QuizData(question = "What is the translation for 'What time is it'?",
                    multipleChoice = listOf("What time is it", "Where is the washroom", "Do you speak english", "I do not understand"), correctAnswer = "What time is it", difficulty = 1),
                QuizData(question = "What is the translation for 'Where is the washroom'?",
                    multipleChoice = listOf("What time is it", "Where is the washroom", "Do you speak english", "I do not understand"), correctAnswer = "Where is the washroom", difficulty = 1),
                QuizData(question = "What is the translation for 'Do you speak english'?",
                    multipleChoice = listOf("What time is it", "Where is the washroom", "Do you speak english", "I do not understand"), correctAnswer = "Do you speak english", difficulty = 1),
                QuizData(question = "What is the translation for 'I do not understand'?",
                    multipleChoice = listOf("What time is it", "Where is the washroom", "Do you speak english", "I do not understand"), correctAnswer = "I do not understand", difficulty = 1),
                QuizData(question = "What is the translation for 'Yesterday was sunny'?",
                    multipleChoice = listOf("Today is cloudy", "Tomorrow will be hot", "Yesterday was sunny", "I enjoy the rain"), correctAnswer = "Yesterday was sunny", difficulty = 1),
                QuizData(question = "What is the translation for 'Tomorrow will be hot'?",
                    multipleChoice = listOf("Today is cloudy", "Tomorrow will be hot", "Yesterday was sunny", "I enjoy the rain"), correctAnswer = "Tomorrow will be hot", difficulty = 1),
                QuizData(question = "What is the translation for 'I enjoy the rain'?",
                    multipleChoice = listOf("Today is cloudy", "Tomorrow will be hot", "Yesterday was sunny", "I enjoy the rain"), correctAnswer = "I enjoy the rain", difficulty = 1),
                QuizData(question = "What is the translation for 'Could you help me?'?",
                    multipleChoice = listOf("Can I help you?", "Could you help me?", "Do you need assistance?", "I want to help"), correctAnswer = "Could you help me?", difficulty = 1),
                QuizData(question = "What is the translation for 'Can I help you?'?",
                    multipleChoice = listOf("Can I help you?", "Could you help me?", "Do you need assistance?", "I want to help"), correctAnswer = "Can I help you?", difficulty = 1),
                QuizData(question = "What is the translation for 'I am learning to swim'?",
                    multipleChoice = listOf("I am learning to swim", "Swimming is fun", "I swim every day", "The pool is closed"), correctAnswer = "I am learning to swim", difficulty = 1),
                QuizData(question = "What is the translation for 'I swim every day'?",
                    multipleChoice = listOf("I am learning to swim", "Swimming is fun", "I swim every day", "The pool is closed"), correctAnswer = "I swim every day", difficulty = 1),



                //ADVANCED QUESTIONS (difficulty 2)
                QuizData(question = "Translate the phrase 'I would like some water'",
                    multipleChoice = listOf("I would like some water", "I would like to leave", "I want a sandwich", "I need a chair"), correctAnswer = "I would like some water", difficulty = 2),
                QuizData(question = "Translate the phrase 'I need a chair'",
                    multipleChoice = listOf("I would like some water", "I would like to leave", "I want a sandwich", "I need a chair"), correctAnswer = "I need a chair", difficulty = 2),
                QuizData(question = "Translate the phrase 'I want a sandwich'",
                    multipleChoice = listOf("I would like some water", "I would like to leave", "I want a sandwich", "I need a chair"), correctAnswer = "I want a sandwich", difficulty = 2),
                QuizData(question = "Translate the phrase 'I am going to the university now'",
                    multipleChoice = listOf("I am going to the university now", "I will walk my dog tomorrow", "I like raccoons", "I was at the library yesterday"), correctAnswer = "I am going to the university now", difficulty = 2),
                QuizData(question = "Translate the phrase 'I will walk my dog tomorrow'",
                    multipleChoice = listOf("I am going to the university now", "I will walk my dog tomorrow", "I like raccoons", "I was at the library yesterday"), correctAnswer = "I will walk my dog tomorrow", difficulty = 2),
                QuizData(question = "Translate the phrase 'I was at the library yesterday'",
                    multipleChoice = listOf("I am going to the university now", "I will walk my dog tomorrow", "I like raccoons", "I was at the library yesterday"), correctAnswer = "I was at the library yesterday", difficulty = 2),
                QuizData(question = "Translate the phrase 'I like to blow bubbles'",
                    multipleChoice = listOf("I am going to the university now", "I will walk my dog tomorrow", "I like to blow bubbles", "I was at the library yesterday"), correctAnswer = "I like to blow bubbles", difficulty = 2),
                QuizData(question = "Translate the phrase 'Video games are fun to play'",
                    multipleChoice = listOf("Video games are fun to play", "Sports are not very hard", "She likes to play football", "He is studying for an exam"), correctAnswer = "Video games are fun to play", difficulty = 2),
                QuizData(question = "Translate the phrase 'He is studying for an exam'",
                    multipleChoice = listOf("Video games are fun to play", "Sports are not very hard", "She likes to play football", "He is studying for an exam"), correctAnswer = "He is studying for an exam", difficulty = 2),
                QuizData(question = "Translate the phrase 'Sports are not very hard'",
                    multipleChoice = listOf("Video games are fun to play", "Sports are not very hard", "She likes to play football", "He is studying for an exam"), correctAnswer = "Sports are not very hard", difficulty = 2),
                QuizData(question = "Translate the phrase 'The complexity of language fascinates me'",
                    multipleChoice = listOf("The complexity of language fascinates me", "Language learning is challenging", "I will write a book about languages", "Understanding syntax is essential"), correctAnswer = "The complexity of language fascinates me", difficulty = 2),
                QuizData(question = "Translate the phrase 'Understanding syntax is essential'",
                    multipleChoice = listOf("The complexity of language fascinates me", "Language learning is challenging", "I will write a book about languages", "Understanding syntax is essential"), correctAnswer = "Understanding syntax is essential", difficulty = 2),
                QuizData(question = "Translate the phrase 'Language learning is challenging'",
                    multipleChoice = listOf("The complexity of language fascinates me", "Language learning is challenging", "I will write a book about languages", "Understanding syntax is essential"), correctAnswer = "Language learning is challenging", difficulty = 2),
                QuizData(question = "Translate the phrase 'Philosophy involves critical thinking and logic'",
                    multipleChoice = listOf("I think critically when I read", "Logic is not always straightforward", "Philosophy involves critical thinking and logic", "Critical analysis is crucial in philosophy"), correctAnswer = "Philosophy involves critical thinking and logic", difficulty = 2),
                QuizData(question = "Translate the phrase 'I think critically when I read'",
                    multipleChoice = listOf("I think critically when I read", "Logic is not always straightforward", "Philosophy involves critical thinking and logic", "Critical analysis is crucial in philosophy"), correctAnswer = "I think critically when I read", difficulty = 2),
                QuizData(question = "Translate the phrase 'Economic theories often reflect societal values'",
                    multipleChoice = listOf("Economic theories often reflect societal values", "Theories are abstract concepts", "Society is influenced by economic conditions", "Values are inherent in our culture"), correctAnswer = "Economic theories often reflect societal values", difficulty = 2),
                QuizData(question = "Translate the phrase 'Society is influenced by economic conditions'",
                    multipleChoice = listOf("Economic theories often reflect societal values", "Theories are abstract concepts", "Society is influenced by economic conditions", "Values are inherent in our culture"), correctAnswer = "Society is influenced by economic conditions", difficulty = 2),
                QuizData(question = "Translate the phrase 'Theories are abstract concepts'",
                    multipleChoice = listOf("Economic theories often reflect societal values", "Theories are abstract concepts", "Society is influenced by economic conditions", "Values are inherent in our culture"), correctAnswer = "Theories are abstract concepts", difficulty = 2)
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
