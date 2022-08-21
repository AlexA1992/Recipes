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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.book.recipes.Draganddrop.SimpleItemTouchHelperCallback
import ru.book.recipes.R
import ru.book.recipes.RecipeAdapter.RecipeAdapter
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.databinding.FragmentStartBinding
import ru.book.recipes.fragment.newRecipeFragment.Companion.recipeId1
import ru.book.recipes.fragment.singleRecipeFragment.Companion.recipeId
import ru.book.recipes.viewModel.RecipeViewModel


class startFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
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
        val binding = FragmentStartBinding.inflate(inflater, container, false)
        recyclerView = binding.recipesRecyclerView
        fab = binding.fabCreate

        //вставляем меню
        setHasOptionsMenu(true)

        val adapter = RecipeAdapter(
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

        binding.recipesRecyclerView.adapter = adapter

        //Drag and Drop
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        val mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recyclerView)

        //Следим за Рецептами
        viewModel.dataRecipes.observe(viewLifecycleOwner) { recipes ->
            println("$recipes recipes")
            if (recipes.isNotEmpty()) {
                println("recipes not empty")
                binding.cooking.visibility = android.view.View.GONE
            }
            if (AppActivity.userId != "") {
                viewModel.findFavorites(AppActivity.userId.toLong())
                viewModel.findLikes(AppActivity.userId.toLong())
                viewModel.findDislikes(AppActivity.userId.toLong())
                viewModel.findShares(AppActivity.userId.toLong())
            }
            adapter.recipes = recipes
        }

        viewModel.dataUsers.observe(viewLifecycleOwner) {
            if (AppActivity.userId != "") {
                fab.isEnabled = true
                fab.backgroundTintList = ColorStateList.valueOf(Color.rgb(255, 64, 129))
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

        //создание поста
        binding.fabCreate.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_newRecipeFragment)
        }

        //редактим рецепт
        viewModel.currentRecipe.observe(viewLifecycleOwner) { currentPost ->
            if (currentPost != null) {
                findNavController().navigate(
                    R.id.action_startFragment_to_newRecipeFragment,
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
                    R.id.action_startFragment_to_singleRecipeFragment,
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
        const val TAG = "StartFragment"
    }
}