package ru.book.recipes.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.text.DateFormat
import java.util.*


fun getCurrentDateTime(): String {
    val currentDate: Date = Date()
    val local: Locale = Locale.getDefault()
    val df: DateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, local)
    val datetime = df.format(currentDate)
    return datetime
}

//прячем клаву
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, /*flag*/ 0)
}

// проверяем параметры нового рецепта
fun checkRecipeFields(
    title: String,
    reference: String,
    category: String,
    content: String,
    steps: String
): String {
    var errorMessage: String = ""
    if (title == "") {
        errorMessage = "Вы не ввели название Рецепта"
    }
    if (reference == "") {
        errorMessage = "Вы не ввели ссылку на картинку Рецепта"
    }
    if (category == "") {
        errorMessage = "Вы не выбрали категорию"
    }
    if (content == "") {
        errorMessage = "Вы не ввели содержание Рецепта"
    }
    if (steps == "") {
        errorMessage = "Вы не ввели ни одного шага приготовления Рецепта"
    }
    return errorMessage
}

//Выдать что что-то пошло не так при регистрации
fun smthAmiss(
    context: Context, userId: String, userName: String, userEmail: String,
    idDB: String, nameDB: String, emailDB: String
) {
    if (userId != idDB && userName != nameDB && userEmail != emailDB) {
        Toast.makeText(
            context,
            "Something was amiss, turn to the coder",
            Toast.LENGTH_SHORT
        ).show()
    }
        println("$userId userId in func")
        println("$userName userName in func")
        println("$userEmail userEmail in func")
        println("$idDB idDB in func")
        println("$nameDB nameDB in func")
        println("$emailDB emailDB in func")
}

