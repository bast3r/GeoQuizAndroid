package by.dazerty.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import by.dazerty.geoquiz.model.Question
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.PersistableBundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import by.dazerty.geoquiz.view.model.QuizVewModel


class MainActivity : AppCompatActivity() {
    private lateinit var trueButton : Button
    private lateinit var falseButton: Button
    private lateinit var prevButton : Button;
    private lateinit var nextButton : Button;
    private lateinit var questionText : TextView

    private val TAG : String = "MainActivity"
    private val CURRENT_QUEST_ID = "current_quest_id"

    private val quizVewModel : QuizVewModel by lazy {
        ViewModelProviders.of(this).get(QuizVewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate")

//        val provider : ViewModelProvider = ViewModelProviders.of(this)
//        val quizVewModel = provider.get(QuizVewModel::class.java)
//        Log.d(TAG, "created new viewmodel $quizVewModel")

        val currentIndex = savedInstanceState?.getInt(CURRENT_QUEST_ID, 0) ?: 0
        quizVewModel.currentQuestionId = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionText = findViewById(R.id.question_textView)

        trueButton.setOnClickListener { view: View ->
            println("click truebutton")
            checkAnswer(true)
            trueButton.setEnabled(false)
            falseButton.setEnabled(false)
        }

        falseButton.setOnClickListener { view : View ->
            println("click falsebutton")
            checkAnswer(false)
            trueButton.setEnabled(false)
            falseButton.setEnabled(false)
        }

        prevButton.setOnClickListener {view : View ->
            quizVewModel.moveToPrev()
            updateQuestion()
        }

        nextButton.setOnClickListener {view : View ->
            quizVewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.d(TAG, "onSaveInstanceState")
        outState.putInt(CURRENT_QUEST_ID, quizVewModel.currentQuestionId)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    private fun checkAnswer(answer : Boolean) {
        val correctAnswer : Boolean = quizVewModel.currentQuestionAnswer
        var toast : Int
        if (answer == correctAnswer) {
            toast = R.string.correct
            quizVewModel.increaseCorrectAnswers()
        } else {
            toast = R.string.incorrect
        }

        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()

        val res = resources
        var congrat : String = String.format(resources.getString(R.string.correct_answers), quizVewModel.correctAnswers)
        if (quizVewModel.lastAnswer()) {
            Toast.makeText(this, congrat, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateQuestion() {
        val question : Int = quizVewModel.currentAnwerText
        questionText.setText(question)

        trueButton.setEnabled(true)
        falseButton.setEnabled(true)
    }

}
