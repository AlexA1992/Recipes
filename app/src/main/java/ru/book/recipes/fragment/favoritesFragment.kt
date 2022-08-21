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
import ru.book.recipes.databinding.FragmentFavoritesBinding
import ru.book.recipes.databinding.FragmentStartBinding
import ru.book.recipes.fragment.singleRecipeFragment.Companion.recipeId
import ru.book.recipes.viewModel.RecipeViewModel
import kotlin.coroutines.EmptyCoroutineContext.plus

class favoritesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    val viewModel: RecipeViewModel by viewModels(
        ownerProducer =
        ::requireParentFragment
    )

    //вставляем меню
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        recyclerView = binding.recipesRecyclerView

        //вставляем меню
        setHasOptionsMenu(true)

        val adapter2 = RecipeAdapter(
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

        binding.recipesRecyclerView.adapter = adapter2


        //Drag and Drop
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter2)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recyclerView)

        //делаем чтобы функционал был доступен после регистрации
        viewModel.dataUsers.observe(viewLifecycleOwner) {
            if (AppActivity.userId != "") {
                viewModel.findFavorites(AppActivity.userId.toLong())
                viewModel.findLikes(AppActivity.userId.toLong())
                viewModel.findDislikes(AppActivity.userId.toLong())
                viewModel.findShares(AppActivity.userId.toLong())
            }
        }

        viewModel.dataRecipes.observe(viewLifecycleOwner) { recipes ->
//            println("$recipes recipes")
//            if (recipes.isNotEmpty()) {
//                println("recipes not empty")
//                binding.cooking.visibility = android.view.View.GONE
//            }
            if (AppActivity.userId != "") {
                viewModel.findFavorites(AppActivity.userId.toLong())
                viewModel.findLikes(AppActivity.userId.toLong())
                viewModel.findDislikes(AppActivity.userId.toLong())
                viewModel.findShares(AppActivity.userId.toLong())
            }

            val allFavoriteRecipeId: MutableList<String> = emptyList<String>().toMutableList()
            AppActivity.allUserFavors?.map { favorite ->
                allFavoriteRecipeId.add(favorite.recipeId.toString())
            }
            println("allFavoriteRecipeId $allFavoriteRecipeId")
            val allFavoriteRecipes = recipes.filter { recipe ->
                allFavoriteRecipeId.contains(recipe.id.toString())
            }
            println("allFavoriteRecipes $allFavoriteRecipes")
            if (allFavoriteRecipes.isNotEmpty()) {
                println("recipes not empty")
                binding.cooking.visibility = android.view.View.GONE
            }
            println("allFavoriteRecipes $allFavoriteRecipes")
            adapter2.recipes = allFavoriteRecipes
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

//        //создание поста
//        binding.fabCreate.setOnClickListener {
//            findNavController().navigate(R.id.action_startFragment_to_newRecipeFragment)
//        }

        //редактим рецепт
        viewModel.currentRecipe.observe(viewLifecycleOwner) { currentPost ->
            if (currentPost != null) {
                findNavController().navigate(
                    R.id.action_favoritesFragment_to_newRecipeFragment,
                    Bundle().apply {
                        println("in recipeCurrentRecipeObserver")
                        recipeId = currentPost.id.toString()
                    })
            }
        }

        //передать фрагмент с целым рецептом
        viewModel.recipeToTransfer.observe(viewLifecycleOwner) { recipeToTransfer ->
            if (recipeToTransfer != null) {
                findNavController().navigate(
                    R.id.action_favoritesFragment_to_singleRecipeFragment,
                    Bundle().apply {
                        println("in recipeToTransferObserver")
                        recipeId = recipeToTransfer.id.toString()
                    })
            }
        }

        //Запихиваем имя фрагмента в свой стек
        AppActivity.fragStack = AppActivity.fragStack + TAG
        return binding.root
    }
    companion object {
        const val TAG = "FavoriteFragment"
    }
}