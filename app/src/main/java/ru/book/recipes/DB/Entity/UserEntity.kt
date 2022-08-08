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

@Entity(tableName = "users")
data class UserEntity(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "email")
    val email: String
)
