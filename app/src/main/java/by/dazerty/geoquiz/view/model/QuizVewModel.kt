package by.dazerty.geoquiz.view.model

import android.util.Log
import androidx.lifecycle.ViewModel
import by.dazerty.geoquiz.R
import by.dazerty.geoquiz.model.Question

private const val TAG = "QuizViewModel"

class QuizVewModel : ViewModel() {

    private var questionBank = listOf(
        Question(R.string.question_1, true),
        Question(R.string.question_2, false),
        Question(R.string.question_3, true),
        Question(R.string.question_4, false),
        Question(R.string.question_5, true),
        Question(R.string.question_6, false),
        Question(R.string.question_7, false),
        Question(R.string.question_8, true),
        Question(R.string.question_9, false)
    )

    private var answeredQuestions : MutableSet<Int> = mutableSetOf()

    var currentQuestionId : Int = 0
    var correctAnswers : Int = 0
    var isCheater = false

    val currentQuestionAnswer : Boolean
        get() = questionBank[currentQuestionId].answer

    val currentAnwerText : Int
        get() = questionBank[currentQuestionId].textResId

    fun moveToNext() {
        currentQuestionId = (currentQuestionId + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentQuestionId--
        if (currentQuestionId == -1)
            currentQuestionId = questionBank.size - 1
    }

    fun showButtonsIfNoAnswers() : Boolean {
        return !answeredQuestions.contains(currentQuestionId)
    }

    fun addAnsweredQuestion() {
        answeredQuestions.add(currentQuestionId)
    }

    fun increaseCorrectAnswers() {
        correctAnswers++
    }

    fun lastAnswer(): Boolean {
        return answeredQuestions.size == questionBank.size
    }

    init {
        Log.d(TAG, "QuizVewModel is created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "QuizVewModel about to be destroyed")
    }

    //clear answers for the new start
    fun clearAnswers() {
        answeredQuestions.clear()
    }
}