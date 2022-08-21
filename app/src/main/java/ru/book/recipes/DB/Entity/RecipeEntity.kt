package ru.book.recipes.DB

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.book.recipes.data.Category
import ru.book.recipes.data.Recipe
import ru.book.recipes.data.User
import ru.book.recipes.utils.getCurrentDateTime

@Entity(
    tableName = "recipes",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("authorId")
    )]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "authorId")
    val authorId: Long,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "category")
    val category: Category,
    @ColumnInfo(name = "date")
    val date: String = getCurrentDateTime(),
//    @ColumnInfo(name = "isFavorite")
//    val isFavorite: Boolean = false,
//    @ColumnInfo(name = "isLikedByMe")
//    val isLikedByMe: Boolean = false,
//    @ColumnInfo(name = "isDislikedByMe")
//    val isDislikedByMe: Boolean = false,
//    @ColumnInfo(name = "isMine")
//    val isMine: Boolean = false,
    @ColumnInfo(name = "countFavorites")
    val countFavorites: Long = 0,
    @ColumnInfo(name = "countLikes")
    val countLikes: Long = 0,
    @ColumnInfo(name = "countDislikes")
    val countDislikes: Long = 0,
    @ColumnInfo(name = "countShares")
    val countShares: Long = 0,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "steps")
    val steps: String,
)
