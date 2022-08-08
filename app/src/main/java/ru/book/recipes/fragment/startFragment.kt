package ru.book.recipes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.book.recipes.RecipeAdapter.RecipeAdapter
import ru.book.recipes.R
import ru.book.recipes.databinding.FragmentStartBinding
import ru.book.recipes.fragment.singleRecipeFragment.Companion.recipeId
import ru.book.recipes.viewModel.RecipeViewModel

class startFragment : Fragment() {
    val viewModel: RecipeViewModel by viewModels(
        ownerProducer =
        ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStartBinding.inflate(inflater, container, false)
        val adapter = RecipeAdapter(
            context,
            viewModel::toFavorites,
            viewModel::like,
            viewModel::dislike,
            viewModel::share,
            viewModel::delete,
            viewModel::edit,
            viewModel::onRecipeClicked,
        )

        binding.recipesRecyclerView.adapter = adapter

        viewModel.dataRecipes.observe(viewLifecycleOwner) { recipes ->
            adapter.recipes = recipes
            if (adapter.recipes.isEmpty()) {
//                println("empty")
                binding.cooking.visibility = android.view.View.VISIBLE
            } else {
                println(adapter.recipes)
            }
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
                        println("in postcurrentPostObserver")
                        recipeId = currentPost.id.toString()
                    })
            }
        }

        //передать фрагмент с целым рецептом
        viewModel.recipeToTransfer.observe(viewLifecycleOwner) { recipeToTransfer ->
            if (recipeToTransfer != null) {
                findNavController().navigate(
                    R.id.action_startFragment_to_singleRecipeFragment,
                    Bundle().apply {
                        println("in postToTransferObserver")
                        recipeId = recipeToTransfer.id.toString()
                    })
            }
        }
        return binding.root
    }
}