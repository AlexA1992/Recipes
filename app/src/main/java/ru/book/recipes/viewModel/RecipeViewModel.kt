package ru.book.recipes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.book.recipes.DB.AppDb
import ru.book.recipes.Repository.ROOMImpl
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.data.*
import ru.book.recipes.utils.SingleLiveEvent

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository =
        ROOMImpl(
            dao = AppDb.getInstance(
                context = application
            ).recipeDao
        )

    val dataRecipes = repository.dataRecipes

    val dataUsers = repository.users

    val recipeToTransfer = SingleLiveEvent<Recipe>()
    fun onRecipeClicked(recipe: Recipe) {
        println("in recipeClicked")
        recipeToTransfer.value = recipe
    }

    //все Избранное конкретнонго юзера
    val recipesInFavor = MutableLiveData<List<Favorite>>()
    fun findFavorites(userId: Long) {
        recipesInFavor.value = repository.findFavorites(userId)
    }
    //добавить рецепт в Избранное
    fun toFavorites(favorite: Favorite, recipeId: Long) =
        repository.toFavoritesRecipe(favorite, recipeId)
    //убрать рецепт из Избранного
    fun fromFavorites(userId: Long, recipeId: Long) =
        repository.fromFavoritesRecipe(userId, recipeId)


    //все Лайки конкретнонго юзера
    val recipesInLike = MutableLiveData<List<Like>>()
    fun findLikes(userId: Long) {
        recipesInLike.value = repository.findLikes(userId)
    }
    //добавить рецепт в Лайки
    fun toLikes(like: Like, recipeId: Long) =
        repository.toLikesRecipe(like, recipeId)
    //убрать рецепт из Лайков
    fun fromLikes(userId: Long, recipeId: Long) =
        repository.fromLikesRecipe(userId, recipeId)


    //все Дизлайки конкретнонго юзера
    val recipesInDislike = MutableLiveData<List<Dislike>>()
    fun findDislikes(userId: Long) {
        recipesInDislike.value = repository.findDislikes(userId)
    }
    //добавить рецепт в Дизлайки
    fun toDislikes(dislike: Dislike, recipeId: Long) =
        repository.toDislikesRecipe(dislike, recipeId)
    //убрать рецепт из Дизлайков
    fun fromDislikes(userId: Long, recipeId: Long) =
        repository.fromDislikesRecipe(userId, recipeId)

    //Sharing
    //все Shares конкретнонго юзера
    val recipesInShare = MutableLiveData<List<Share>>()
    fun findShares(userId: Long) {
        recipesInShare.value = repository.findShares(userId)
    }
    val shareActionNeeded = SingleLiveEvent<StringBuilder>()
    fun shareClicked(recipeToString: StringBuilder, recipeId: Long, share: Share) {
        repository.sharePlus(share, recipeId)
        shareActionNeeded.value = recipeToString
    }

    fun delete(recipe: Recipe) = repository.deleteRecipe(recipe)

    fun addUser(user: User) = repository.addUser(user)

    val foundedUser = MutableLiveData<User>()
    fun findUser(user: User) {
        foundedUser.value = repository.findUser(user)
    }

    val currentRecipe = MutableLiveData<Recipe?>()
    fun edit(recipe: Recipe) {
        println("in OnEditClicked")
        currentRecipe.value = recipe
        currentRecipe.value = null
    }

    fun createRecipe(newRecipe: Recipe) = repository.createRecipe(newRecipe)
    fun updateRecipe(oldRecipe: Recipe) = repository.updateRecipe(oldRecipe)

//    val userFavoriteRe = MutableLiveData<User>()
//    fun findUser(user: User) {
//        foundedUser.value = repository.findUser(user)
//    }

}