package ru.book.recipes.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RecipeEntity::class,
        UserEntity::class,
//        StepEntity::class,
        FavoriteEntity::class,
        LikeEntity::class,
        DislikeEntity::class,
        ShareEntity::class,
               ],
    version = 7
)

abstract class AppDb : RoomDatabase() {
    abstract val recipeDao: DBInterface

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
                    .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDb::class.java,
                "app.db"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}
