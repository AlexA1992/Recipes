package ru.book.recipes.RecipeAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import ru.book.recipes.R
import ru.book.recipes.data.Recipe
import ru.book.recipes.databinding.RecipecardBinding
import java.io.InputStream
import kotlin.properties.Delegates


@SuppressLint("NotifyDataSetChanged")
internal class RecipeAdapter(
    val context: Context?,
    private val toFavoritesClicked: (Recipe) -> Unit,
    private val onLikeClicked: (Recipe) -> Unit,
    private val onDislikeClicked: (Recipe) -> Unit,
    private val onShareClicked: (Recipe) -> Unit,
    private val onDeleteClicked: (Recipe) -> Unit,
    private val onEditClicked: (Recipe) -> Unit,
    private val onRecipeClicked: (Recipe) -> Unit,

    ) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(Diffcallback) {
        var recipes: List<Recipe> by Delegates.observable(emptyList()) { _, _, _ ->
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RecipecardBinding.inflate(inflater, parent, false)
            return ViewHolder(
                binding,
                toFavoritesClicked,
                onLikeClicked,
                onDislikeClicked,
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

        private object Diffcallback : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
                oldItem.id != newItem.id

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
                oldItem != newItem
        }

        class ViewHolder(
            private val recipeCardBinding: RecipecardBinding,
            private val toFavoritesClicked: (Recipe) -> Unit,
            private val onLikeClicked: (Recipe) -> Unit,
            private val onDislikeClicked: (Recipe) -> Unit,
            private val onShareClicked: (Recipe) -> Unit,
            private val onDeleteClicked: (Recipe) -> Unit,
            private val onEditClicked: (Recipe) -> Unit,
            private val onRecipeClicked: (Recipe) -> Unit,
        ) :
            RecyclerView.ViewHolder(recipeCardBinding.root) {

            @SuppressLint("SetTextI18n")
            fun bind(recipe: Recipe) = with(recipeCardBinding) {
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

                //передать один рецепт
                recipeCardBinding.learnmore.setOnClickListener {
//                    println("in ViewHolder")
                    onRecipeClicked(recipe)
                }

                recipeCardBinding.menuButton.setOnClickListener {
                    recipeCardBinding.menuButton.isChecked = true
                    popupMenu.show()
                }

                popupMenu.setOnDismissListener() {
                    recipeCardBinding.menuButton.isChecked = false
                }
                recipeCardBinding.title.setText(recipe.title)
                recipeCardBinding.date.setText(recipe.date)
                recipeCardBinding.author.setText(recipe.author)
                recipeCardBinding.category.setText(recipe.category.name)

//                val theImage: Int = getApplicationContext<Context>().getResources()
//                    .getIdentifier("peasoup.jpg", "drawable", "ru.book.recipes")

                recipeCardBinding.image.setImageResource(R.drawable.peasoup)






//                recipeCardBinding.image.setImageBitmap()

                if(recipe.isMine) recipeCardBinding.mine.setVisibility(android.view.View.VISIBLE)

                recipeCardBinding.likes.setIconResource(R.drawable.ic_baseline_favorite_border_24)

                recipeCardBinding.date.setText(recipe.date)

//                if(recipeCardBinding.likes.isChecked) {
//                    recipe.isLikedByMe
//                }

//                recipeCardBinding.dislikes.isChecked = recipe.isDislikedByMe

                recipeCardBinding.shares.isChecked = recipe.countShares > 0
                recipeCardBinding.shares.setIconResource(R.drawable.ic_baseline_share_24)

                recipeCardBinding.favorites.setOnClickListener {
                    toFavoritesClicked(recipe)
                }

                recipeCardBinding.likes.setOnClickListener {
                    onLikeClicked(recipe)
                }

                recipeCardBinding.dislikes.setOnClickListener {
                    onDislikeClicked(recipe)
                }

                recipeCardBinding.shares.setOnClickListener {
                    onShareClicked(recipe)
                }

                recipeCardBinding.menuButton.setOnClickListener {
                    recipeCardBinding.menuButton.isChecked = true
                    popupMenu.show()
                }

                popupMenu.setOnDismissListener() {
                    recipeCardBinding.menuButton.isChecked = false
                }
            }
        }
}