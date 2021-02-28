package by.dazerty.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import by.dazerty.geoquiz.view.model.QuizVewModel


class MainActivity : AppCompatActivity() {
    private lateinit var trueButton : Button
    private lateinit var falseButton: Button
    private lateinit var prevButton : Button
    private lateinit var nextButton : Button
    private lateinit var cheatButton : Button
    private lateinit var questionText : TextView

    private val TAG : String = "MainActivity"
    private val CURRENT_QUEST_ID = "current_quest_id"
    private val IS_CHEATER = "is_cheater"
    private val REQUEST_CODE_CHEAT = 0

    private val quizVewModel : QuizVewModel by lazy {
        ViewModelProvider(this).get(QuizVewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate")
        Log.d(TAG, "created new viewmodel $quizVewModel")

//        val provider : ViewModelProvider = ViewModelProviders.of(this)
//        val quizVewModel = provider.get(QuizVewModel::class.java)
//        Log.d(TAG, "created new viewmodel $quizVewModel")

        quizVewModel.currentQuestionId = savedInstanceState?.getInt(CURRENT_QUEST_ID, 0) ?: 0
        quizVewModel.isCheater = savedInstanceState?.getBoolean(IS_CHEATER, false) ?: false

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionText = findViewById(R.id.question_textView)

        trueButton.setOnClickListener { view: View ->
            println("click truebutton")
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view : View ->
            println("click falsebutton")
            checkAnswer(false)
        }

        prevButton.setOnClickListener {view : View ->
            quizVewModel.moveToPrev()
            updateQuestion()
        }

        nextButton.setOnClickListener {view : View ->
            quizVewModel.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener {view : View ->
            println("Cheat button click!")
            val answerIsTrue = quizVewModel.currentQuestionAnswer
            //explicit call
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue, quizVewModel.currentAttempts())

            //проверка подходящей версии сдк для использования фичи
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }

        updateQuestion()
    }
//waiting for result from the second activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizVewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizVewModel.attemtps = data?.getIntExtra(EXTRA_ATTEMPTS, 0) ?: 0
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        outState.putInt(CURRENT_QUEST_ID, quizVewModel.currentQuestionId)
        outState.putBoolean(IS_CHEATER, quizVewModel.isCheater)
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
        val toast : Int

        toast = when {
            quizVewModel.isCheater -> R.string.judgement_toast
            answer == correctAnswer -> R.string.correct
            else -> R.string.incorrect
        }

        quizVewModel.addAnsweredQuestion()
        if (answer == correctAnswer) {
            quizVewModel.increaseCorrectAnswers()
        }

        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()

        trueButton.isEnabled = quizVewModel.showButtonsIfNoAnswers()
        falseButton.isEnabled = quizVewModel.showButtonsIfNoAnswers()

        val congrat : String = String.format(resources.getString(R.string.correct_answers), quizVewModel.correctAnswers)
        if (quizVewModel.lastAnswer()) {
            Toast.makeText(this, congrat, Toast.LENGTH_SHORT).show()
            quizVewModel.clearAnswers()
        }
    }

    private fun updateQuestion() {
        val question : Int = quizVewModel.currentAnwerText
        questionText.setText(question)

//        trueButton.isEnabled = true
//        falseButton.isEnabled = true

        trueButton.isEnabled = quizVewModel.showButtonsIfNoAnswers()
        falseButton.isEnabled = quizVewModel.showButtonsIfNoAnswers()
    }

}
