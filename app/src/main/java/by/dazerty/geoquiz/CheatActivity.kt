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

class CheatActivity : AppCompatActivity() {
    private var answerIsTrue : Boolean = false
    private lateinit var answerTextView : TextView
    private lateinit var showAnswerButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            val answer = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answer)
            setAnswerShownResult(true)
        }

        findViewById<TextView>(R.id.version_text_view).setText("Andoid SDK version " + Build.VERSION.SDK_INT)
    }

    private fun setAnswerShownResult(isAswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue : Boolean) : Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}
