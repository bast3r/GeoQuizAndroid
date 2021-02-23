package by.dazerty.geoquiz.model

import androidx.annotation.StringRes

data class Question ( @StringRes var textResId : Int, var answer : Boolean ) {
}