package ru.book.recipes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.book.recipes.databinding.FragmentContestBinding
import ru.book.recipes.databinding.RecipecardBinding

class contestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentContestBinding.inflate(inflater, container, false)
//        binding.cooking.setImageResource(
//            R.drawable.ic_baseline_dangerous_24)
        return binding.root
    }
}