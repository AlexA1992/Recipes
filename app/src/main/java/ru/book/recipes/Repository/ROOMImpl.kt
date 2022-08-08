package ru.book.recipes.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.book.recipes.DB.DBInterface
import ru.book.recipes.DB.toEntity
import ru.book.recipes.DB.toModel
import ru.book.recipes.data.Recipe
import ru.book.recipes.data.User

class ROOMImpl(
    private val dao: DBInterface
) : Repository {
    override val recipes = dao.getAllRecipes().map { entityRecipe ->
        entityRecipe.map {
            it.toModel()
        }
    }

    override val users = dao.getAllUsers().map { entityUser ->
        entityUser.map {
            it.toModel()
        }
    }

    override fun addUser(user: User) {
        dao.insertUser(user.toEntity())
    }

    override fun findUser(user: User): User {
        val foundedUser = dao.findUser(user.toEntity().name, user.toEntity().password)
        return foundedUser
    }

    //override val registeredUser: MutableLiveData<User?> = MutableLiveData(null)
//    override val registeredUser = dao.findLastUser().map { entityUser ->
//        entityUser.toModel()
//    }


    override fun createRecipe(recipe: Recipe) {
        println("post.id ${recipe.id}")
        if (recipe.id == 0L) recipe.toEntity().let {
            dao.insert(it) } else recipe.toEntity()
                .let { dao.update(recipe.toEntity().id, it.content) }
    }

    override fun deleteRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun editRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun likeRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun dislikeRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun shareRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override fun toFavoritesRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }
}