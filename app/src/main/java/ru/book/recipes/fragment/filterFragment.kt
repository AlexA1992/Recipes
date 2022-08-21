package ru.book.recipes.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.book.recipes.Draganddrop.SimpleItemTouchHelperCallback
import ru.book.recipes.R
import ru.book.recipes.RecipeAdapter.RecipeAdapter
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.data.Recipe
import ru.book.recipes.databinding.FragmentFilterBinding
import ru.book.recipes.databinding.FragmentSearchBinding
import ru.book.recipes.fragment.newRecipeFragment.Companion.recipeId1
import ru.book.recipes.fragment.singleRecipeFragment.Companion.recipeId
import ru.book.recipes.utils.StringArg
import ru.book.recipes.viewModel.RecipeViewModel

class filterFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    val viewModel: RecipeViewModel by viewModels(
        ownerProducer =
        ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val binding = FragmentFilterBinding.inflate(inflater, container, false)
        recyclerView = binding.recipesRecyclerView

        val adapter4 = RecipeAdapter(
//            context,
            viewModel::toFavorites,
            viewModel::fromFavorites,
            viewModel::toLikes,
            viewModel::fromLikes,
            viewModel::toDislikes,
            viewModel::fromDislikes,
            viewModel::shareClicked,
            viewModel::delete,
            viewModel::edit,
            viewModel::onRecipeClicked,
        )
        binding.recipesRecyclerView.adapter = adapter4

        //Drag and Drop
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter4)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recyclerView)

        //проверяем передан ли Рецепт для редактирования
        val stringToFilter = arguments?.filter.toString()

        viewModel.dataRecipes.observe(viewLifecycleOwner) { recipes ->
            val categoryList = stringToFilter.split(";")
            var filterResult: List<Recipe> = emptyList()
            categoryList.map {
                val categoryRecipes = recipes.filter { recipe ->
                    recipe.category.name == it
                }
                filterResult = filterResult + categoryRecipes
            }

            println("$filterResult filterResult")

            if (filterResult.isNotEmpty()) {
                println("recipes not empty")
                binding.cooking.visibility = android.view.View.GONE
                binding.recipesRecyclerView.visibility = android.view.View.VISIBLE
            }
            if (AppActivity.userId != "") {
                viewModel.findFavorites(AppActivity.userId.toLong())
                viewModel.findLikes(AppActivity.userId.toLong())
                viewModel.findDislikes(AppActivity.userId.toLong())
                viewModel.findShares(AppActivity.userId.toLong())
            }
            adapter4.recipes = filterResult
        }

        viewModel.dataUsers.observe(viewLifecycleOwner) {
            if (AppActivity.userId != "") {
                viewModel.findFavorites(AppActivity.userId.toLong())
                viewModel.findLikes(AppActivity.userId.toLong())
                viewModel.findDislikes(AppActivity.userId.toLong())
                viewModel.findShares(AppActivity.userId.toLong())
            }
        }

        //расшарить Рецепт (название, категория, контент, шаги)
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

        //редактим рецепт
        viewModel.currentRecipe.observe(viewLifecycleOwner) { currentPost ->
            if (currentPost != null) {
                findNavController().navigate(
                    R.id.action_filterFragment2_to_newRecipeFragment,
                    Bundle().apply {
                        println("in recipeCurrentRecipeObserver")
                        recipeId1 = currentPost.id.toString()
                    })
            }
        }

        //передать фрагмент с целым рецептом
        viewModel.recipeToTransfer.observe(viewLifecycleOwner) { recipeToTransfer ->
            if (recipeToTransfer != null) {
                findNavController().navigate(
                    R.id.action_filterFragment2_to_singleRecipeFragment,
                    Bundle().apply {
                        println("in recipeToTransferObserver")
                        recipeId = recipeToTransfer.id.toString()
                    })
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.filter: String? by StringArg
    }
}
