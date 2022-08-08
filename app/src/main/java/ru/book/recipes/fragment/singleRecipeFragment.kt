package ru.book.recipes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import ru.book.recipes.R
import ru.book.recipes.databinding.FragmentSigninBinding
import ru.book.recipes.databinding.FragmentSingleRecipeBinding
import ru.book.recipes.utils.StringArg
import ru.book.recipes.viewModel.RecipeViewModel

class singleRecipeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val binding = FragmentSingleRecipeBinding.inflate(inflater, container, false)
        val viewModel: RecipeViewModel by viewModels(
            ownerProducer =
            ::requireParentFragment
        )
        val recipeToShow = arguments?.recipeId

        viewModel.dataRecipes.observe(viewLifecycleOwner) { recipes ->
            val theRecipe = recipes.find {
                recipeToShow?.toLong() == it.id
            }
//            println(thePost)
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

            content.setText(theRecipe?.content)
            author.setText(theRecipe?.author)
            category.setText(theRecipe?.category.toString())

//            favoritesInfo.setText(theRecipe?.countFavorites.toString())
//            if (theRecipe != null) {
//                println("theRecipe.likes ${theRecipe.countLikes}")
//                favorites.isChecked = theRecipe.isFavorite
//            }

//            likesInfo.setText(theRecipe?.countLikes.toString())
//            if (theRecipe != null) {
//                println("theRecipe.likes ${theRecipe.countLikes}")
//                likes.isChecked = theRecipe.isLikedByMe
//            }

//            dislikesInfo.setText(theRecipe?.countDislikes.toString())
//            if (theRecipe != null) {
//                println("theRecipe.dislikes ${theRecipe.countDislikes}")
//                dislikes.isChecked = theRecipe.isDislikedByMe
//            }

            sharesInfo.setText(theRecipe?.countShares.toString())
            if (theRecipe != null) {
                println("theRecipe.share ${theRecipe.countShares}")
                shares.isChecked = theRecipe.countShares > 0
            }

            date.setText(theRecipe?.date)

            likes.setOnClickListener {
                if (theRecipe != null) {
                    viewModel.like(theRecipe)
                }
            }

            shares.setOnClickListener {
                if (theRecipe != null) {
                    viewModel.share(theRecipe)
                    shares.isChecked = theRecipe.countShares > 0
                }
            }
            //не получается создать экземпляр Вьюхолдера
//            val singleViewHolder = PostAdapter(
//                PostRepo().::likesChange(thePost)
//            ).ViewHolder(
//                binding
//            )
            val menuButton: MaterialButton = binding.root.findViewById(R.id.menuButton)
            val popupMenu by lazy {
                //println(post)
                PopupMenu(this.context, menuButton).apply {
                    inflate(R.menu.options_recipe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                if (theRecipe != null) {
                                    viewModel.delete(theRecipe)
                                }
                                findNavController().navigateUp()
                                true
                            }

                            R.id.edit -> {
                                if (theRecipe != null) {
                                    viewModel.edit(theRecipe)
                                }
                                findNavController().navigate(
                                    R.id.action_singleRecipeFragment_to_newRecipeFragment,
                                    Bundle().apply {
                                        if (theRecipe != null) {
                                            recipeId = theRecipe.id.toString()
                                        }
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
        }
        return binding.root
    }

    companion object {
        var Bundle.recipeId: String? by StringArg
    }
}