package ru.book.recipes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.book.recipes.DB.AppDb
import ru.book.recipes.Repository.ROOMImpl
import ru.book.recipes.data.Recipe
import ru.book.recipes.data.User
import ru.book.recipes.utils.SingleLiveEvent

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository =
        ROOMImpl(
            dao = AppDb.getInstance(
                context = application
            ).recipeDao
        )
    val dataRecipes = repository.recipes

    val dataUsers = repository.users

    val recipeToTransfer = SingleLiveEvent<Recipe>()
    fun onRecipeClicked(recipe: Recipe) {
        println("in postClicked")
        recipeToTransfer.value = recipe
    }

    fun toFavorites(recipe: Recipe) = repository.toFavoritesRecipe(recipe)
    fun like(recipe: Recipe) = repository.likeRecipe(recipe)
    fun dislike(recipe: Recipe) = repository.dislikeRecipe(recipe)
    fun share(recipe: Recipe) = repository.shareRecipe(recipe)
    fun delete(recipe: Recipe) = repository.deleteRecipe(recipe)

    fun addUser(user: User) = repository.addUser(user)

    val foundedUser = MutableLiveData<User?>()
    fun findUser(user: User) {
        foundedUser.value = repository.findUser(user)
    }

    val currentRecipe = MutableLiveData<Recipe?>()
    fun edit(recipe: Recipe) {
        println("in OnEditClicked")
        currentRecipe.value = recipe
    }

    fun createRecipe(newRecipe: Recipe) = repository.createRecipe(newRecipe)

//    val registeredUser = repository.registeredUser
//    fun registerUser(user: User){
//        repository.registeredUser.value = user
//        println(repository.registeredUser.value)
//    }

}