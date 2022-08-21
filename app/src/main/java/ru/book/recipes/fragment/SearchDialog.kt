package ru.book.recipes.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import ru.book.recipes.R
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.fragment.searchFragment.Companion.search

class SearchDialog : DialogFragment() {
    //вставляем меню
//    @Deprecated("Deprecated in Java")
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.top_app_bar, menu)
//    }

    private lateinit var searchString: SearchView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =

        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.searchWhat))
            .setView(R.layout.searching_layout)
            .setPositiveButton(getString(R.string.search)) { _, _ ->
                println("positive")
                searchString = dialog?.findViewById<SearchView>(R.id.stringToSearch)!!
                val theText: String = searchString.getQuery().toString()

                //Выясняем с какого фрагмента пришел запрос
                val lastFragment: String = AppActivity.fragStack.lastOrNull().toString()
                println("$lastFragment lastFragment")
                when (lastFragment) {
                    "StartFragment" -> findNavController().navigate(
                        R.id.action_startFragment_to_searchFragment3,
                        Bundle().apply {
                            search = theText
                        })
                    "FavoriteFragment" -> findNavController().navigate(
                        R.id.action_favoritesFragment_to_searchFragment3,
                        Bundle().apply {
                            search = theText
                        })
                    "SingleFragment" -> findNavController().navigate(
                        R.id.action_singleRecipeFragment_to_searchFragment3,
                        Bundle().apply {
                            search = theText
                        })
                }
                dismiss()
            }
            .create()


    companion object {
        const val TAG = "SearchDialog"
    }
}