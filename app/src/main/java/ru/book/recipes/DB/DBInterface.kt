package ru.book.recipes.DB

import android.icu.text.CaseMap
import android.media.Image
import androidx.lifecycle.LiveData
import androidx.room.*
import ru.book.recipes.data.*


@Dao
interface DBInterface {

    //Получить все рецепты
    @Query("SELECT * FROM recipes ORDER BY id Desc")
    fun getAllRecipes(): LiveData<List<RecipeEntity>>

    //Получить всех юзеров
    @Query("SELECT * FROM users ORDER BY id Desc")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Insert
    fun insert(recipe: RecipeEntity)

    //Вставить Юзера
    @Insert
    fun insertUser(user: UserEntity)

    //Найти все Избранные рецепты конкретного Юзера
    @Query("SELECT * FROM favorites WHERE userId = :userId")
    fun findFavorites(userId: Long): List<Favorite>

    //Вставить в Фавориты и увеличить счет фаворитов в Рецептах
    @Insert
    fun toFavoritesRecipe(favorite: FavoriteEntity)

    @Query(
        """
        UPDATE recipes SET
        countFavorites = countFavorites + 1 WHERE id = :recipeId
    """
    )
    fun addFavoriteCount(recipeId: Long)

    //Убрать из Фаворитов и умньшить счет фаворитов в Рецептах
    @Query("DELETE FROM favorites WHERE userId = :userId AND recipeId = :recipeId")
    fun fromFavoritesRecipe(userId: Long, recipeId: Long)

    @Query(
        """
        UPDATE recipes SET
        countFavorites = countFavorites - 1 WHERE id = :recipeId
    """
    )
    fun reduceFavoriteCount(recipeId: Long)

    //Найти все Лайки конкретного Юзера
    @Query("SELECT * FROM likes WHERE userId = :userId")
    fun findLikes(userId: Long): List<Like>

    //Вставить в Лайки и увеличить счет фаворитов в Рецептах
    @Insert
    fun toLikesRecipe(like: LikeEntity)

    @Query(
        """
        UPDATE recipes SET
        countLikes = countLikes + 1 WHERE id = :recipeId
    """
    )
    fun addLikesCount(recipeId: Long)

    //Убрать из Лайков и умньшить счет фаворитов в Рецептах
    @Query("DELETE FROM likes WHERE userId = :userId AND recipeId = :recipeId")
    fun fromLikesRecipe(userId: Long, recipeId: Long)

    @Query(
        """
        UPDATE recipes SET
        countLikes = countLikes - 1 WHERE id = :recipeId
    """
    )
    fun reduceLikesCount(recipeId: Long)

    //Найти все Дизлайки конкретного Юзера
    @Query("SELECT * FROM dislikes WHERE userId = :userId")
    fun findDislikes(userId: Long): List<Dislike>

    //Вставить в Дизлайки и увеличить счет фаворитов в Рецептах
    @Insert
    fun toDislikesRecipe(dislike: DislikeEntity)

    @Query(
        """
        UPDATE recipes SET
        countDislikes = countDislikes + 1 WHERE id = :recipeId
    """
    )
    fun addDislikesCount(recipeId: Long)

    //Убрать из Дизлайков и умньшить счет фаворитов в Рецептах
    @Query("DELETE FROM dislikes WHERE userId = :userId AND recipeId = :recipeId")
    fun fromDislikesRecipe(userId: Long, recipeId: Long)

    @Query(
        """
        UPDATE recipes SET
        countDislikes = countDislikes - 1 WHERE id = :recipeId
    """
    )
    fun reduceDislikesCount(recipeId: Long)


    //Найти все Share конкретного Юзера
    @Query("SELECT * FROM shares WHERE userId = :userId")
    fun findShares(userId: Long): List<Share>

    //вствить Share и увеличить счет шарингов в Рецептах
    @Insert
    fun toSharesRecipe(share: ShareEntity)

    @Query(
        """
        UPDATE recipes SET
        countShares = countShares + 1 WHERE id = :recipeId
    """
    )
    fun addShareCount(recipeId: Long)

    //удалить Рецепт
    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeRecipeById(id: Long)
    //удалить Избранное Рецепта
    @Query("DELETE FROM favorites WHERE recipeId = :recipeId")
    fun removeFavorOfRecipe(recipeId: Long)
    //удалить Лайки Рецепта
    @Query("DELETE FROM likes WHERE recipeId = :recipeId")
    fun removeLikesOfRecipe(recipeId: Long)
    //удалить Дизлайки Рецепта
    @Query("DELETE FROM dislikes WHERE recipeId = :recipeId")
    fun removeDislikesOfRecipe(recipeId: Long)
    //удалить Shares Рецепта
    @Query("DELETE FROM shares WHERE recipeId = :recipeId")
    fun removeSharesOfRecipe(recipeId: Long)


    //редактировать Рецепт
    @Query("UPDATE recipes SET title = :title," +
            " category = :category, " +
            " image = :image, " +
            " content = :content, " +
            " steps = :steps " +
            "WHERE id = :id")
    fun updateRecipe(id: Long, title: String, category: Category,
                     image: String, content: String, steps: String)

//    fun save(post: PostEntity) =
//        if (post.id == 0) insert(post) else update(post.id, post.content)

//    @Query("""
//        UPDATE recipes SET
//        countLikes = countLikes + CASE WHEN islikedByMe THEN -1 ELSE 1 END,
//        islikedByMe = CASE WHEN islikedByMe THEN 0 ELSE 1 END
//        WHERE id =  :id
//    """)
//    fun likedByMe(id: Int)


//
//    @Query("UPDATE recipes SET countDislikes = countDislikes + 1 WHERE id = :id")
//    fun dislikePlus(id: Int)
//
//    @Query("UPDATE recipes SET countShares = countShares + 1 WHERE id = :id")
//    fun sharePlus(id: Int)

    //Найти Юзера
    @Query("SELECT * FROM users WHERE name = :name AND password = :password")
    fun findUser(name: String, password: String): User
}
