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
    tableName = "steps",
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("recipeId")
    )]
)
data class StepEntity(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "recipeId")
    val recipeId: Long,
    @ColumnInfo(name = "step")
    val steps: String,
    )