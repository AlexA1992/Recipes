package ru.book.recipes.data

import kotlinx.serialization.Serializable
import ru.book.recipes.utils.getCurrentDateTime

@Serializable
data class User(
    val id: Long = 0,
    var name: String,
    val password: String,
    var email: String
)


@Serializable
data class Recipe(
    val id: Long = 0,
    val title: String,
    val authorId: Long,
    val author: String,
    val image: String = "R.drawable.peasoup",
    var category: Category,
    var date: String = getCurrentDateTime(),
//    var isFavorite: Boolean = false,
//    var isLikedByMe: Boolean = false,
//    var isDislikedByMe: Boolean = false,
    var isMine: Boolean = false,
    var countFavorites: Long = 0,
    var countLikes: Long = 0,
    var countDislikes: Long = 0,
    var countShares: Long = 0,
    var content: String,
    var steps: String,
)

//@Serializable
//data class Step(
//    val id: Long,
//    val recipeId: Long,
//    val steps: String,
//)

enum class Category {
    EUROPEAN,
    ASIAN,
    PANASIAN,
    EASTERN,
    AMERICAN,
    RUSSIAN,
    MEDITERRANEAN
}

@Serializable
data class Favorite(
    val id: Long = 0,
    var userId: Long,
    var recipeId: Long,
)

@Serializable
data class Like(
    val id: Long = 0,
    var userId: Long,
    var recipeId: Long,
)

@Serializable
data class Dislike(
    val id: Long = 0,
    var userId: Long,
    var recipeId: Long,
)



