package ru.book.recipes.RecipeAdapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.book.recipes.Draganddrop.ItemTouchHelperAdapter
import ru.book.recipes.Draganddrop.ItemTouchHelperViewHolder
import ru.book.recipes.R
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.data.*
import ru.book.recipes.databinding.RecipecardBinding
import kotlin.properties.Delegates


@SuppressLint("NotifyDataSetChanged")
internal class RecipeAdapter(
//    val context: Context?,
    private val toFavoritesClicked: (Favorite, Long) -> Unit,
    private val fromFavoritesClicked: (Long, Long) -> Unit,
    private val toLikesClicked: (Like, Long) -> Unit,
    private val fromLikesClicked: (Long, Long) -> Unit,
    private val toDislikesClicked: (Dislike, Long) -> Unit,
    private val fromDislikesClicked: (Long, Long) -> Unit,
    private val onShareClicked: (StringBuilder, Long, Share) -> Unit,
    private val onDeleteClicked: (Recipe) -> Unit,
    private val onEditClicked: (Recipe) -> Unit,
    private val onRecipeClicked: (Recipe) -> Unit,

    ) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(Diffcallback), ItemTouchHelperAdapter {
    var recipes: List<Recipe> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipecardBinding.inflate(inflater, parent, false)
        return ViewHolder(
            binding,
            toFavoritesClicked,
            fromFavoritesClicked,
            toLikesClicked,
            fromLikesClicked,
            toDislikesClicked,
            fromDislikesClicked,
            onShareClicked,
            onDeleteClicked,
            onEditClicked,
            onRecipeClicked,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev: Recipe = recipes.toMutableList().removeAt(fromPosition)
        recipes.toMutableList().add(if (toPosition > fromPosition) toPosition - 1 else toPosition, prev)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        onDeleteClicked(recipes.get(position))
//        recipes.toMutableList().removeAt(position)
        notifyItemRemoved(position)
    }

    private object Diffcallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id != newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem != newItem
    }

    class ViewHolder(
        private val recipeCardBinding: RecipecardBinding,
        private val toFavoritesClicked: (Favorite, Long) -> Unit,
        private val fromFavoritesClicked: (Long, Long) -> Unit,
        private val toLikesClicked: (Like, Long) -> Unit,
        private val fromLikesClicked: (Long, Long) -> Unit,
        private val toDislikesClicked: (Dislike, Long) -> Unit,
        private val fromDislikesClicked: (Long, Long) -> Unit,
        private val onShareClicked: (StringBuilder, Long, Share) -> Unit,
        private val onDeleteClicked: (Recipe) -> Unit,
        private val onEditClicked: (Recipe) -> Unit,
        private val onRecipeClicked: (Recipe) -> Unit,
    ) :
        RecyclerView.ViewHolder(recipeCardBinding.root), ItemTouchHelperViewHolder {

        @SuppressLint("SetTextI18n")
        fun bind(recipe: Recipe) = with(recipeCardBinding) {

            //сделать иконки enabled при зареганном Юзере
            if (AppActivity.userId != "") {
                val userId: Long = AppActivity.userId.toLong()
                if (userId == recipe.authorId) {
                    recipeCardBinding.mine.setVisibility(android.view.View.VISIBLE)
                }
                recipeCardBinding.favorites.isEnabled = true
                //recipeCardBinding.favorites.iconTint = ColorStateList.valueOf(Color.WHITE)
                recipeCardBinding.likes.isEnabled = true
                //recipeCardBinding.likes.iconTint = ColorStateList.valueOf(Color.WHITE)
                recipeCardBinding.dislikes.isEnabled = true
                //recipeCardBinding.dislikes.iconTint = ColorStateList.valueOf(Color.WHITE)
                recipeCardBinding.shares.isEnabled = true
                //recipeCardBinding.shares.iconTint = ColorStateList.valueOf(Color.WHITE)
            }

            //надуваем меню - три точки
            val popupMenu by lazy {
                //println(recipe)
                PopupMenu(itemView.context, recipeCardBinding.menuButton).apply {
                    inflate(R.menu.options_recipe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                onDeleteClicked(recipe)
                                true
                            }
                            R.id.edit -> {
                                onEditClicked(recipe)
                                true
                            }
                            else -> false
                        }
                    }
                }
            }

            //управление Избранным
            var allUsersFavorites: List<Favorite>?
            recipeCardBinding.favorites.setOnClickListener {
                allUsersFavorites = AppActivity.allUserFavors
                println("$allUsersFavorites allUsersFavorites")
                if (allUsersFavorites != null) {
                    val favoriteExists = allUsersFavorites!!.find() {
                        println(it.recipeId)
                        println(recipe.id)
                        it.recipeId == recipe.id
                    }
                    if (favoriteExists != null) {
                        println("favoriteExists $favoriteExists")
                        recipeCardBinding.favorites.isChecked = false
                        val thisUserId = AppActivity.userId.toLong()
                        val thisRecipeId = recipe.id
                        fromFavoritesClicked(thisUserId, thisRecipeId)
                    } else {
                        println("no favorite")
                        val newFavorite = Favorite(0, AppActivity.userId.toLong(), recipe.id)
                        println("$newFavorite newFavorite")
                        toFavoritesClicked(newFavorite, recipe.id)
                        recipeCardBinding.favorites.isChecked = true
                    }
                } else {
                    println("no favorite")
                    val newFavorite = Favorite(0, AppActivity.userId.toLong(), recipe.id)
                    println("$newFavorite newFavorite")
                    toFavoritesClicked(newFavorite, recipe.id)
                    println(recipeCardBinding.favorites.isChecked)
                }
            }
            //управление цветом иконки Избранного
            allUsersFavorites = AppActivity.allUserFavors
            println("$allUsersFavorites allUsersFavorites")
            if (allUsersFavorites != null) {
                val favoriteExists = allUsersFavorites!!.find {
                    println(it.recipeId)
                    println(recipe.id)
                    it.recipeId == recipe.id
                }
                if (favoriteExists != null) {
                    println("favoriteExists $favoriteExists")
                    recipeCardBinding.favorites.isChecked = recipe.countFavorites > 0
                    recipeCardBinding.favorites.setIconResource(R.drawable.thefavorites)
                    println(recipe.countFavorites)
                    println(recipeCardBinding.favorites.isChecked)
                } else {
                    recipeCardBinding.favorites.isChecked = false
                }
            }


//          //управление Лайком
            var allUsersLikes: List<Like>?
            recipeCardBinding.likes.setOnClickListener {
                allUsersLikes = AppActivity.allUserLikes
                println("$allUsersLikes allUsersLikes")
                if (allUsersLikes != null) {
                    val likeExists = allUsersLikes!!.find {
                        println(it.recipeId)
                        println(recipe.id)
                        it.recipeId == recipe.id
                    }
                    if (likeExists != null) {
                        println("likeExists")
                        recipeCardBinding.likes.isChecked = false
                        val thisUserId = AppActivity.userId.toLong()
                        val thisRecipeId = recipe.id
                        fromLikesClicked(thisUserId, thisRecipeId)
                    } else {
                        println("no favorite")
                        val newLike = Like(0, AppActivity.userId.toLong(), recipe.id)
                        println("$newLike newLike")
                        toLikesClicked(newLike, recipe.id)
                        recipeCardBinding.likes.isChecked = true
                    }
                } else {
                    println("no like")
                    val newLike = Like(0, AppActivity.userId.toLong(), recipe.id)
                    println("$newLike newLike")
                    toLikesClicked(newLike, recipe.id)
                    println(recipeCardBinding.likes.isChecked)
                }
            }
            //управление цветом иконки Лайка
            allUsersLikes = AppActivity.allUserLikes
            println("$allUsersLikes allUsersLikes")
            if (allUsersLikes != null) {
                val likeExists = allUsersLikes!!.find {
                    println(it.recipeId)
                    println(recipe.id)
                    it.recipeId == recipe.id
                }
                if (likeExists != null) {
                    println("likeExists")
                    recipeCardBinding.likes.isChecked = recipe.countLikes > 0
                    recipeCardBinding.likes.setIconResource(R.drawable.thelikes)
                    println(recipe.countLikes)
                    println(recipeCardBinding.likes.isChecked)
                } else {
                    recipeCardBinding.likes.isChecked = false
                }
            }

            //управление Дислайком
            var allUsersDislikes: List<Dislike>?
            recipeCardBinding.dislikes.setOnClickListener {
                allUsersDislikes = AppActivity.allUserDislikes
                println("$allUsersDislikes allUsersDislikes")
                if (allUsersDislikes != null) {
                    val dislikeExists = allUsersDislikes!!.find {
                        println(it.recipeId)
                        println(recipe.id)
                        it.recipeId == recipe.id
                    }
                    if (dislikeExists != null) {
                        println("dislikeExists")
                        recipeCardBinding.dislikes.isChecked = false
                        val thisUserId = AppActivity.userId.toLong()
                        val thisRecipeId = recipe.id
                        fromDislikesClicked(thisUserId, thisRecipeId)
                    } else {
                        println("no dislike")
                        val newDislike = Dislike(0, AppActivity.userId.toLong(), recipe.id)
                        println("$newDislike newDislike")
                        toDislikesClicked(newDislike, recipe.id)
                        recipeCardBinding.dislikes.isChecked = true
                    }
                } else {
                    println("no favorite")
                    val newDislike = Dislike(0, AppActivity.userId.toLong(), recipe.id)
                    println("$newDislike newDislike")
                    toDislikesClicked(newDislike, recipe.id)
                    println(recipeCardBinding.dislikes.isChecked)
                }
            }
            //управление цветом иконки Дизлайка
            allUsersDislikes = AppActivity.allUserDislikes
            println("$allUsersDislikes allUsersDislikes")
            if (allUsersDislikes != null) {
                val dislikeExists = allUsersDislikes!!.find {
                    println(it.recipeId)
                    println(recipe.id)
                    it.recipeId == recipe.id
                }
                if (dislikeExists != null) {
                    println("dislikeExists")
                    recipeCardBinding.dislikes.isChecked = recipe.countDislikes > 0
                    recipeCardBinding.dislikes.setIconResource(R.drawable.thedislikes)
                    println(recipe.countDislikes)
                    println(recipeCardBinding.dislikes.isChecked)
                } else {
                    recipeCardBinding.dislikes.isChecked = false
                }
            }

            //Sharing
            var allUsersShares: List<Share>?
            recipeCardBinding.shares.setOnClickListener {
                val recipeToString = StringBuilder()
                recipeToString.append("Title: ").append(recipe.title).append("\n")
                    .append("Category: ").append(recipe.category).append("\n")
                    .append("Recipe itself: ").append(recipe.content).append("\n")
                    .append("Steps: ").append(recipe.steps)
                val theShare = Share(0, AppActivity.userId.toLong(), recipe.id)
                onShareClicked(recipeToString, recipe.id, theShare)
                recipeCardBinding.shares.isChecked = recipe.countShares > 0
                recipeCardBinding.shares.setIconResource(R.drawable.theshares)
            }
            //управление цветом иконки Share
            allUsersShares = AppActivity.allUserShares
            println("$allUsersShares allUsersShares")
            if (allUsersShares != null) {
                val shareExists = allUsersShares.find {
                    println(it.recipeId)
                    println(recipe.id)
                    it.recipeId == recipe.id
                }
                if (shareExists != null) {
                    println("shareExists")
                    recipeCardBinding.shares.isChecked = recipe.countShares > 0
                    recipeCardBinding.shares.setIconResource(R.drawable.theshares)
                    println(recipe.countShares)
                    println(recipeCardBinding.shares.isChecked)
                } else {
                    recipeCardBinding.shares.isChecked = false
                }
            }
            //очистить переменные
            allUsersFavorites = null
            allUsersLikes = null
            allUsersDislikes = null
            allUsersShares = null

            //передать один рецепт
            recipeCardBinding.learnmore.setOnClickListener {
                onRecipeClicked(recipe)
            }

            //заполняем
            recipeCardBinding.title.setText(recipe.title)
            recipeCardBinding.date.setText(recipe.date)
            recipeCardBinding.author.setText(recipe.author)
            recipeCardBinding.category.setText(recipe.category.name)

            // эксперименты с картинкой
//                val uri = Uri.parse("android.resource://ru.book.recipes/drawable/peasoup")
//                val mIs: InputStream? = getApplicationContext<Context>().getContentResolver().openInputStream (uri)
//                val mIcon1 =
//                        BitmapFactory.decodeStream(mIs)
//                val theImage = Drawable.createFromPath(mIcon1.toString())
//                println(theImage)
//                val theImage: Int = getApplicationContext<Context>().getResources()
//                    .getIdentifier("peasoup.jpg", "drawable", "ru.book.recipes")
            recipeCardBinding.image.setImageResource(R.drawable.peasoup)


            recipeCardBinding.menuButton.setOnClickListener {
                recipeCardBinding.menuButton.isChecked = true
                popupMenu.show()
            }

            popupMenu.setOnDismissListener()
            {
                recipeCardBinding.menuButton.isChecked = false
            }
            println("end of recipe:  ${recipe.id}")
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.YELLOW)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }
}