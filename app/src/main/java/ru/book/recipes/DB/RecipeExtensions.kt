package ru.book.recipes.DB

import ru.book.recipes.data.*

//приводим User
internal fun UserEntity.toModel() = User(
    id = id,
    name = name,
    password = password,
    email = email
)

internal fun User.toEntity() = UserEntity(
    id = id,
    name = name,
    password = password,
    email = email
)

//приводим Recipe
internal fun RecipeEntity.toModel() = Recipe(
    id = id,
    title = title,
    authorId = authorId,
    author = author,
    image = image,
    category = category,
    date = date,
//    isFavorite = isFavorite,
//    isLikedByMe = isLikedByMe,
//    isDislikedByMe = isDislikedByMe,
//    isMine = isMine,
    countFavorites = countFavorites,
    countLikes = countLikes,
    countDislikes = countDislikes,
    countShares = countShares,
    content = content,
    steps = steps,
)

internal fun Recipe.toEntity() = RecipeEntity(
    id = id,
    title = title,
    authorId = authorId,
    author = author,
    image = image,
    category = category,
    date = date,
//    isFavorite = isFavorite,
//    isLikedByMe = isLikedByMe,
//    isDislikedByMe = isDislikedByMe,
//    isMine = isMine,
    countFavorites = countFavorites,
    countLikes = countLikes,
    countDislikes = countDislikes,
    countShares = countShares,
    content = content,
    steps = steps,
)

//приводим Step
//internal fun StepEntity.toModel() = Step(
//    id = id,
//    recipeId = recipeId,
//    steps = steps,
//)
//
//internal fun Step.toEntity() = StepEntity(
//    id = id,
//    recipeId = recipeId,
//    steps = steps,
//)

//приводим Favorites
internal fun FavoriteEntity.toModel() = Favorite(
    id = id,
    userId = userId,
    recipeId = recipeId,
)

internal fun Favorite.toEntity() = FavoriteEntity(
    id = id,
    userId = userId,
    recipeId = recipeId,
)

//приводим Likes
internal fun LikeEntity.toModel() = Like(
    id = id,
    userId = userId,
    recipeId = recipeId,
)

internal fun Like.toEntity() = LikeEntity(
    id = id,
    userId = userId,
    recipeId = recipeId,
)

//приводим Dislikes
internal fun DislikeEntity.toModel() = Dislike(
    id = id,
    userId = userId,
    recipeId = recipeId,
)

internal fun Dislike.toEntity() = DislikeEntity(
    id = id,
    userId = userId,
    recipeId = recipeId,
)

internal fun Share.toEntity() = ShareEntity(
    id = id,
    userId = userId,
    recipeId = recipeId,
)