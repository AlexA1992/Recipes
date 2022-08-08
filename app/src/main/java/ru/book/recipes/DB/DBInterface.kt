package ru.book.recipes.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.book.recipes.DB.RecipeEntity
import ru.book.recipes.data.User

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

//    //Взять последнего Юзера
//    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
//    fun findLastUser(): LiveData<UserEntity>


    @Query("UPDATE recipes SET content = :content WHERE id = :id")
    fun update(id: Long, content: String)

//    fun save(post: PostEntity) =
//        if (post.id == 0) insert(post) else update(post.id, post.content)

//    @Query("""
//        UPDATE recipes SET
//        countLikes = countLikes + CASE WHEN islikedByMe THEN -1 ELSE 1 END,
//        islikedByMe = CASE WHEN islikedByMe THEN 0 ELSE 1 END
//        WHERE id =  :id
//    """)
//    fun likedByMe(id: Int)

//    @Query("DELETE FROM recipes WHERE id = :id")
//    fun removeById(id: Int)
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