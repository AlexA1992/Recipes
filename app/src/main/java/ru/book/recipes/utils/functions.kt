package ru.book.recipes.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import ru.book.recipes.data.Category
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun getCurrentDateTime(): String {
    val currentDate: Date = Date()
    val local: Locale = Locale.getDefault()
    val df: DateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, local)
    val datetime = df.format(currentDate)
    return datetime
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, /*flag*/ 0)
}

// проверяем параметры нового рецепта
var errorMessage: String = ""
fun checkRecipeFields(
    title: String,
    reference: String,
    category: String,
    content: String,
    steps: String
): String {
    if(title == ""){
        errorMessage = "Вы не ввели название Рецепта"
    }
    if(reference == ""){
        errorMessage = "Вы не ввели ссылку на картинку Рецепта"
    }
    if(category == ""){
        errorMessage = "Вы не выбрали категорию"
    }
    if(content == ""){
        errorMessage = "Вы не ввели содержание Рецепта"
    }
    if(steps == ""){
        errorMessage = "Вы не ввели ниодного шага приготовления Рецепта"
    }
    return errorMessage
}