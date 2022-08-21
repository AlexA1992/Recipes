package ru.book.recipes.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.book.recipes.DB.DBInterface
import ru.book.recipes.DB.toEntity
import ru.book.recipes.DB.toModel
import ru.book.recipes.data.*

class ROOMImpl(
    private val dao: DBInterface
) : Repository {
    override val dataRecipes = dao.getAllRecipes().map { entities ->
        entities.map {
            it.toModel()
        }
    }


    override val users = dao.getAllUsers().map { entityUser ->
        entityUser.map {
            it.toModel()
        }
    }

    override fun findUser(user: User): User {
        val foundedUser = dao.findUser(user.toEntity().name, user.toEntity().password)
        return foundedUser
    }

    override fun addUser(user: User) {
        dao.insertUser(user.toEntity())
    }

    override fun createRecipe(recipe: Recipe) {
        println("recipe.id in createRecipe ${recipe.id}")
        if (recipe.id == 0L) recipe.toEntity().let {
            dao.insert(it)
        } else println("no recipe")
    }

    override fun deleteRecipe(recipe: Recipe) {
        dao.removeRecipeById(recipe.id)
        dao.removeFavorOfRecipe(recipe.id)
        dao.removeLikesOfRecipe(recipe.id)
        dao.removeDislikesOfRecipe(recipe.id)
        dao.removeSharesOfRecipe(recipe.id)
    }

    override fun updateRecipe(recipe: Recipe) {
        dao.updateRecipe(
            id = recipe.id,
            title = recipe.title,
            category = recipe.category,
            image = recipe.image,
            content = recipe.content,
            steps = recipe.steps,
        )
    }


    //найти Избранное конкретного Юзера
    override fun findFavorites(userId: Long): List<Favorite> = dao.findFavorites(userId)
    //    добавить в Фавориты и увеличить счет
    override fun toFavoritesRecipe(favorite: Favorite, recipeId: Long) {
        println("$favorite favorite in RepoImpl")
        dao.toFavoritesRecipe(favorite.toEntity())
        dao.addFavoriteCount(recipeId)
    }
    //    убрать из Фаворитов и умеьшить счет
    override fun fromFavoritesRecipe(userId: Long, recipeId: Long) {
        dao.fromFavoritesRecipe(userId, recipeId)
        dao.reduceFavoriteCount(recipeId)
    }

    //найти Лайки конкретного Юзера
    override fun findLikes(userId: Long): List<Like> = dao.findLikes(userId)
    //    добавить в Лайки и увеличить счет
    override fun toLikesRecipe(like: Like, recipeId: Long) {
        println("$like like in RepoImpl")
        dao.toLikesRecipe(like.toEntity())
        dao.addLikesCount(recipeId)
    }
    //    убрать из Лайков и умеьшить счет
    override fun fromLikesRecipe(userId: Long, recipeId: Long) {
        dao.fromLikesRecipe(userId, recipeId)
        dao.reduceLikesCount(recipeId)
    }

    //найти Дизлайки конкретного Юзера
    override fun findDislikes(userId: Long): List<Dislike> = dao.findDislikes(userId)
    //    добавить в Дизлайки и увеличить счет
    override fun toDislikesRecipe(dislike: Dislike, recipeId: Long) {
        println("$dislike dislike in RepoImpl")
        dao.toDislikesRecipe(dislike.toEntity())
        dao.addDislikesCount(recipeId)
    }
    //    убрать из Дизлайков и умеьшить счет
    override fun fromDislikesRecipe(userId: Long, recipeId: Long) {
        dao.fromDislikesRecipe(userId, recipeId)
        dao.reduceDislikesCount(recipeId)
    }

    //найти Share конкретного Юзера
    override fun findShares(userId: Long): List<Share> = dao.findShares(userId)
    //поставить Share и увеличить их количество в рецепте
    override fun sharePlus(share: Share, recipeId: Long) {
        dao.toSharesRecipe(share.toEntity())
        dao.addShareCount(recipeId)
    }
}