package by.dazerty.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_ANSWER_SHOWN = "by.dazerty.gqoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "by.dazerty.geoquiz.answer_is_true"
const val EXTRA_ATTEMPTS = "by.dazerty.geoquiz.attempts"

class CheatActivity : AppCompatActivity() {
    private var answerIsTrue : Boolean = false
    private lateinit var answerTextView : TextView
    private lateinit var showAnswerButton : Button
    private lateinit var attemptsLeft : TextView
    private var attempts : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        attemptsLeft = findViewById(R.id.count_attempts_text)

        var attempts = intent.getIntExtra(EXTRA_ATTEMPTS, 0)
        attemptsLeft.text = "Attempts left ${attempts}"

        showAnswerButton.isEnabled = attempts > 0
        showAnswerButton.setOnClickListener {
            if (attempts > 0) {
                attempts--
            }

            attemptsLeft.text = "Attempts left ${attempts}"
            val answer = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            showAnswerButton.isEnabled = false
            answerTextView.setText(answer)
            setAnswerShownResult(true, attempts)
        }

        findViewById<TextView>(R.id.version_text_view).setText("Andoid SDK version " + Build.VERSION.SDK_INT)
    }

    private fun setAnswerShownResult(isAswerShown: Boolean, attempts : Int) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAswerShown)
            putExtra(EXTRA_ATTEMPTS, attempts)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(
            packageContext: Context,
            answerIsTrue: Boolean,
            currentAttempts: Int
        ) : Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_ATTEMPTS, currentAttempts)
            }
        }
    }
}
