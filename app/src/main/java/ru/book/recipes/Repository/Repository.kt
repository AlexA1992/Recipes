package ru.book.recipes.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.book.recipes.data.*

interface Repository {
    val dataRecipes: LiveData<List<Recipe>>
    fun createRecipe(recipe: Recipe)
    fun deleteRecipe(recipe: Recipe)
    fun updateRecipe(recipe: Recipe)

    fun findFavorites(userId: Long): List<Favorite>
    fun toFavoritesRecipe(favorite: Favorite, recipeId: Long)
    fun fromFavoritesRecipe(userId: Long, recipeId: Long)

    fun findLikes(userId: Long): List<Like>
    fun toLikesRecipe(like: Like, recipeId: Long)
    fun fromLikesRecipe(userId: Long, recipeId: Long)

    fun findDislikes(userId: Long): List<Dislike>
    fun toDislikesRecipe(dislike: Dislike, recipeId: Long)
    fun fromDislikesRecipe(userId: Long, recipeId: Long)

    fun findShares(userId: Long): List<Share>
    fun sharePlus(share: Share, recipeId: Long)

    val users: LiveData<List<User>>
    fun addUser(user: User)
    fun findUser(user: User): User
}