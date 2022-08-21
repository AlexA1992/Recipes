package ru.book.recipes.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import ru.book.recipes.R
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.data.Dislike
import ru.book.recipes.data.Favorite
import ru.book.recipes.data.Like
import ru.book.recipes.data.Share
import ru.book.recipes.databinding.FragmentSigninBinding
import ru.book.recipes.databinding.FragmentSingleRecipeBinding
import ru.book.recipes.fragment.newRecipeFragment.Companion.recipeId1
import ru.book.recipes.utils.StringArg
import ru.book.recipes.viewModel.RecipeViewModel

class singleRecipeFragment : Fragment() {
    val viewModel: RecipeViewModel by viewModels(
        ownerProducer =
        ::requireParentFragment
    )
    //вставляем меню
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    private lateinit var step1Field: TextView
    private lateinit var step2Field: TextView
    private lateinit var step3Field: TextView
    private lateinit var step4Field: TextView
    private lateinit var step5Field: TextView
    private lateinit var step6Field: TextView
    private lateinit var step7Field: TextView
    private lateinit var step8Field: TextView
    private lateinit var step9Field: TextView
    private lateinit var step10Field: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val binding = FragmentSingleRecipeBinding.inflate(inflater, container, false)

        val recipeToShow = arguments?.recipeId

        //вставляем меню
        setHasOptionsMenu(true)

        viewModel.dataRecipes.observe(viewLifecycleOwner) { recipes ->
            if(AppActivity.userId != ""){
                viewModel.findFavorites(AppActivity.userId.toLong())
                viewModel.findLikes(AppActivity.userId.toLong())
                viewModel.findDislikes(AppActivity.userId.toLong())
                viewModel.findShares(AppActivity.userId.toLong())
            }
            val theRecipe = recipes.find {
                recipeToShow?.toLong() == it.id
            }
            val title = binding.title
            val content =
                binding.content
            val category =
                binding.category
            val author =
                binding.author

            val favorites = binding.favorites
            val likes = binding.likes
            val dislikes = binding.dislikes
            val shares = binding.shares

            val favoritesInfo =
                binding.favoritesinfo
            val likesInfo =
                binding.likesinfo
            val dislikesInfo =
                binding.dislikesinfo
            val sharesInfo =
                binding.sharesinfo
            val date = binding.date
            val mine = binding.mine

            title.setText(theRecipe?.title)
            content.setText(theRecipe?.content)
            author.setText(theRecipe?.author)
            category.setText(theRecipe?.category.toString())
            date.setText(theRecipe?.date)
            binding.image.setImageResource(R.drawable.peasoup)

            favoritesInfo.setText(theRecipe?.countFavorites.toString())
            likesInfo.setText(theRecipe?.countLikes.toString())
            dislikesInfo.setText(theRecipe?.countDislikes.toString())
            sharesInfo.setText(theRecipe?.countShares.toString())

            //сделать иконки enabled при зареганном Юзере
            if (AppActivity.userId != "") {
                val userId: Long = AppActivity.userId.toLong()
                if (theRecipe != null) {
                    if (userId == theRecipe.authorId) {
                        mine.setVisibility(android.view.View.VISIBLE)
                    }
                }
                favorites.isEnabled = true
                //recipeCardBinding.favorites.iconTint = ColorStateList.valueOf(Color.WHITE)
                likes.isEnabled = true
                //recipeCardBinding.likes.iconTint = ColorStateList.valueOf(Color.WHITE)
                dislikes.isEnabled = true
                //recipeCardBinding.dislikes.iconTint = ColorStateList.valueOf(Color.WHITE)
                shares.isEnabled = true
                //recipeCardBinding.shares.iconTint = ColorStateList.valueOf(Color.WHITE)
            }

            //управление Избранным
            var allUsersFavorites: List<Favorite>?
            favorites.setOnClickListener {
                allUsersFavorites = AppActivity.allUserFavors
                println("$allUsersFavorites allUsersFavorites")
                if (allUsersFavorites != null) {
                    val favoriteExists = allUsersFavorites!!.find() {
                        println(it.recipeId)
                        if (theRecipe != null) {
                            println(theRecipe.id)
                        }
                        it.recipeId == theRecipe?.id
                    }
                    if (favoriteExists != null) {
                        println("favoriteExists $favoriteExists")
                        favorites.isChecked = false
                        val thisUserId = AppActivity.userId.toLong()
                        val thisRecipeId = theRecipe?.id
                        if (thisRecipeId != null) {
                            viewModel.fromFavorites(thisUserId, thisRecipeId)
                        }
                    } else {
                        println("no favorite")
                        val newFavorite =
                            theRecipe?.let { it1 -> Favorite(0, AppActivity.userId.toLong(), it1.id) }
                        println("$newFavorite newFavorite")
                        if (theRecipe != null) {
                            if (newFavorite != null) {
                                viewModel.toFavorites(newFavorite, theRecipe.id)
                            }
                        }
                        favorites.isChecked = true
                    }
                } else {
                    println("no favorite")
                    val newFavorite =
                        theRecipe?.let { it1 -> Favorite(0, AppActivity.userId.toLong(), it1.id) }
                    println("$newFavorite newFavorite")
                    if (theRecipe != null) {
                        if (newFavorite != null) {
                            viewModel.toFavorites(newFavorite, theRecipe.id)
                        }
                    }
                    println(favorites.isChecked)
                }
            }
            //управление цветом иконки Избранного
            allUsersFavorites = AppActivity.allUserFavors
            println("$allUsersFavorites allUsersFavorites")
            if (allUsersFavorites != null) {
                val favoriteExists = allUsersFavorites!!.find {
                    println(it.recipeId)
                    if (theRecipe != null) {
                        println(theRecipe.id)
                    }
                    it.recipeId == theRecipe?.id
                }
                if (favoriteExists != null) {
                    println("favoriteExists $favoriteExists")
                    if (theRecipe != null) {
                        favorites.isChecked = theRecipe.countFavorites > 0
                    }
                    favorites.setIconResource(R.drawable.thefavorites)
                    if (theRecipe != null) {
                        println(theRecipe.countFavorites)
                    }
                    println(favorites.isChecked)
                } else {
                    favorites.isChecked = false
                }
            }


//          //управление Лайком
            var allUsersLikes: List<Like>?
            likes.setOnClickListener {
                allUsersLikes = AppActivity.allUserLikes
                println("$allUsersLikes allUsersLikes")
                if (allUsersLikes != null) {
                    val likeExists = allUsersLikes!!.find {
                        println(it.recipeId)
                        if (theRecipe != null) {
                            println(theRecipe.id)
                        }
                        it.recipeId == theRecipe?.id
                    }
                    if (likeExists != null) {
                        println("likeExists")
                        likes.isChecked = false
                        val thisUserId = AppActivity.userId.toLong()
                        val thisRecipeId = theRecipe?.id
                        if (thisRecipeId != null) {
                            viewModel.fromLikes(thisUserId, thisRecipeId)
                        }
                    } else {
                        println("no favorite")
                        val newLike =
                            theRecipe?.let { it1 -> Like(0, AppActivity.userId.toLong(), it1.id) }
                        println("$newLike newLike")
                        if (theRecipe != null) {
                            if (newLike != null) {
                                viewModel.toLikes(newLike, theRecipe.id)
                            }
                        }
                        likes.isChecked = true
                    }
                } else {
                    println("no like")
                    val newLike = theRecipe?.let { it1 ->
                        Like(0, AppActivity.userId.toLong(), it1.id) }
                    println("$newLike newLike")
                    if (theRecipe != null) {
                        if (newLike != null) {
                            viewModel.toLikes(newLike, theRecipe.id)
                        }
                    }
                    println(likes.isChecked)
                }
            }
            //управление цветом иконки Лайка
            allUsersLikes = AppActivity.allUserLikes
            println("$allUsersLikes allUsersLikes")
            if (allUsersLikes != null) {
                val likeExists = allUsersLikes!!.find {
                    println(it.recipeId)
                    if (theRecipe != null) {
                        println(theRecipe.id)
                    }
                    it.recipeId == theRecipe?.id
                }
                if (likeExists != null) {
                    println("likeExists")
                    likes.isChecked = theRecipe?.countLikes!! > 0
                    likes.setIconResource(R.drawable.thelikes)
                    println(theRecipe.countLikes)
                    println(likes.isChecked)
                } else {
                    likes.isChecked = false
                }
            }

            //управление Дислайком
            var allUsersDislikes: List<Dislike>?
            dislikes.setOnClickListener {
                allUsersDislikes = AppActivity.allUserDislikes
                println("$allUsersDislikes allUsersDislikes")
                if (allUsersDislikes != null) {
                    val dislikeExists = allUsersDislikes!!.find {
                        println(it.recipeId)
                        println(theRecipe?.id)
                        it.recipeId == theRecipe?.id
                    }
                    if (dislikeExists != null) {
                        println("dislikeExists")
                        dislikes.isChecked = false
                        val thisUserId = AppActivity.userId.toLong()
                        val thisRecipeId = theRecipe?.id
                        if (thisRecipeId != null) {
                            viewModel.fromDislikes(thisUserId, thisRecipeId)
                        }
                    } else {
                        println("no dislike")
                        val newDislike =
                            theRecipe?.let { it1 -> Dislike(0, AppActivity.userId.toLong(), it1.id) }
                        println("$newDislike newDislike")
                        if (newDislike != null) {
                            theRecipe.id.let { it1 -> viewModel.toDislikes(newDislike, it1) }
                        }
                        dislikes.isChecked = true
                    }
                } else {
                    println("no favorite")
                    val newDislike =
                        theRecipe?.let { it1 -> Dislike(0, AppActivity.userId.toLong(), it1.id) }
                    println("$newDislike newDislike")
                    theRecipe?.id?.let { it1 ->
                        if (newDislike != null) {
                            viewModel.toDislikes(newDislike, it1)
                        }
                    }
                    println(dislikes.isChecked)
                }
            }
            //управление цветом иконки Дизлайка
            allUsersDislikes = AppActivity.allUserDislikes
            println("$allUsersDislikes allUsersDislikes")
            if (allUsersDislikes != null) {
                val dislikeExists = allUsersDislikes!!.find {
                    println(it.recipeId)
                    println(theRecipe?.id)
                    it.recipeId == theRecipe?.id
                }
                if (dislikeExists != null) {
                    println("dislikeExists")
                    dislikes.isChecked = theRecipe?.countDislikes!! > 0
                    dislikes.setIconResource(R.drawable.thedislikes)
                    println(theRecipe.countDislikes)
                    println(dislikes.isChecked)
                } else {
                    dislikes.isChecked = false
                }
            }

            //Sharing
            var allUsersShares: List<Share>?
            shares.setOnClickListener {
                val recipeToString = StringBuilder()
                recipeToString.append("Title: ").append(theRecipe?.title).append("\n")
                    .append("Category: ").append(theRecipe?.category).append("\n")
                    .append("Recipe itself: ").append(theRecipe?.content).append("\n")
                    .append("Steps: ").append(theRecipe?.steps)
                val theShare = theRecipe?.id?.let { it1 ->
                    Share(0, AppActivity.userId.toLong(),
                        it1
                    )
                }
                theRecipe?.id?.let { it1 ->
                    if (theShare != null) {
                        viewModel.shareClicked(recipeToString, it1, theShare)
                    }
                }
                shares.isChecked = theRecipe?.countShares!! > 0
                shares.setIconResource(R.drawable.theshares)
            }
            //Отслеживание Share
            viewModel.shareActionNeeded.observe(viewLifecycleOwner) { recipeToString ->
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, recipeToString.toString())
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(
                    intent, "Выберите приложение"
                )
                startActivity(shareIntent)
            }
            //управление цветом иконки Share
            allUsersShares = AppActivity.allUserShares
            println("$allUsersShares allUsersShares")
            if (allUsersShares != null) {
                val shareExists = allUsersShares.find {
                    println(it.recipeId)
                    println(theRecipe?.id)
                    it.recipeId == theRecipe?.id
                }
                if (shareExists != null) {
                    println("shareExists")
                    shares.isChecked = theRecipe?.countShares!! > 0
                    shares.setIconResource(R.drawable.theshares)
                    println(theRecipe.countShares)
                    println(shares.isChecked)
                } else {
                    shares.isChecked = false
                }
            }
            //очистить переменные
            allUsersFavorites = null
            allUsersLikes = null
            allUsersDislikes = null
            allUsersShares = null


            //вставляем шаги
            step1Field = binding.stepsContent1
            step2Field = binding.stepsContent2
            step3Field = binding.stepsContent3
            step4Field = binding.stepsContent4
            step5Field = binding.stepsContent5
            step6Field = binding.stepsContent6
            step7Field = binding.stepsContent7
            step8Field = binding.stepsContent8
            step9Field = binding.stepsContent9
            step10Field = binding.stepsContent10

            val stepFields = listOf<TextView>(
                step1Field,
                step2Field,
                step3Field,
                step4Field,
                step5Field,
                step6Field,
                step7Field,
                step8Field,
                step9Field,
                step10Field,
            )
            val allSteps: String? = theRecipe?.steps
            val stepAsList: MutableList<String> = allSteps?.split(";") as MutableList<String>
            //println(stepAsList)

            for (step in stepFields) {
//                println(stepAsList)
                step.visibility = android.view.View.VISIBLE
//                println(stepAsList.get(0))
                step.setText(stepAsList.get(0))
                stepAsList.removeAt(0)
//                println(stepAsList)
                if (stepAsList.isEmpty()) break
            }

            val menuButton: MaterialButton = binding.root.findViewById(R.id.menuButton)
            val popupMenu by lazy {
                //println(post)
                PopupMenu(this.context, menuButton).apply {
                    inflate(R.menu.options_recipe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                viewModel.delete(theRecipe)
                                findNavController().navigateUp()
                                true
                            }
                            R.id.edit -> {
                                viewModel.edit(theRecipe)
                                findNavController().navigate(
                                    R.id.action_singleRecipeFragment_to_newRecipeFragment,
                                    Bundle().apply {
                                        recipeId1 = theRecipe.id.toString()
                                    })
                                true
                            }
                            else -> false
                        }
                    }
                }
            }
            menuButton.setOnClickListener {
                menuButton.isChecked = true
                popupMenu.show()
            }
            popupMenu.setOnDismissListener() {
                menuButton.isChecked = false
            }

            //сохраняем все Избранное этого юзера
            viewModel.recipesInFavor.observe(viewLifecycleOwner) {
                println("recipesInFavor $it")
                AppActivity.allUserFavors = it
            }

            //сохраняем все Лайки этого юзера
            viewModel.recipesInLike.observe(viewLifecycleOwner) {
                println("recipesInLike $it")
                AppActivity.allUserLikes = it
            }

            //сохраняем все Дизлайки этого юзера
            viewModel.recipesInDislike.observe(viewLifecycleOwner) {
                println("recipesInDislike $it")
                AppActivity.allUserDislikes = it
            }

            //сохраняем все Share этого юзера
            viewModel.recipesInShare.observe(viewLifecycleOwner) {
                println("recipesInShare $it")
                AppActivity.allUserShares = it
            }
        }

        //Запихиваем имя фрагмента в свой стек
        AppActivity.fragStack = AppActivity.fragStack + TAG
        return binding.root
    }

    companion object {
        var Bundle.recipeId: String? by StringArg
        const val TAG = "SingleFragment"
    }
}