package ru.book.recipes.fragment

import android.app.Dialog
import android.icu.text.StringSearch
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ru.book.recipes.R
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.fragment.filterFragment.Companion.filter
import ru.book.recipes.fragment.searchFragment.Companion.search
import ru.book.recipes.fragment.singleRecipeFragment.Companion.recipeId
import java.lang.StringBuilder

class FilterDialog : DialogFragment() {
    private lateinit var chipGroup: ChipGroup
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =

        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.chooseChips))
            .setView(R.layout.filter_layout)
            .setPositiveButton(getString(R.string.search)) { _, _ ->
                println("positive")
                chipGroup = dialog?.findViewById<ChipGroup>(R.id.chipGroup)!!
                val checkedChipIds = chipGroup.checkedChipIds

                val fullString = StringBuilder()
                println("$checkedChipIds checkedChipIds")
                checkedChipIds.map{
                    val thisChipText: String = chipGroup.findViewById<Chip>(it).text.toString()
                    fullString.append(thisChipText).append(";")
                }
                println(fullString)

                val theText: String = fullString.toString()

                //Выясняем с какого фрагмента пришел запрос
                val lastFragment: String = AppActivity.fragStack.lastOrNull().toString()
                println("$lastFragment lastFragment")
                when (lastFragment) {
                    "StartFragment" -> findNavController().navigate(
                        R.id.action_startFragment_to_filterFragment2,
                        Bundle().apply {
                            filter = theText
                        })
                    "FavoriteFragment" -> findNavController().navigate(
                        R.id.action_favoritesFragment_to_filterFragment2,
                        Bundle().apply {
                            filter = theText
                        })
                    "SingleFragment" -> findNavController().navigate(
                        R.id.action_singleRecipeFragment_to_filterFragment2,
                        Bundle().apply {
                            filter = theText
                        })
                }
                dismiss()
            }
            .create()


    companion object {
        const val TAG = "FilterDialog"
    }
}