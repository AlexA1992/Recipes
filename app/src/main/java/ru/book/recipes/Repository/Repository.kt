package ru.book.recipes.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.book.recipes.data.Recipe
import ru.book.recipes.data.User

interface Repository {
    val recipes: LiveData<List<Recipe>>
    fun createRecipe(recipe: Recipe)
    fun deleteRecipe(recipe: Recipe)
    fun editRecipe(recipe: Recipe)
    fun likeRecipe(recipe: Recipe)
    fun dislikeRecipe(recipe: Recipe)
    fun shareRecipe(recipe: Recipe)
    fun toFavoritesRecipe(recipe: Recipe)

    val users: LiveData<List<User>>
    fun addUser(user: User)
    fun findUser(user: User):User

}