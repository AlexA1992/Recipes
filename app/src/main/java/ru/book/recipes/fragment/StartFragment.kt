package ru.book.recipes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ru.book.recipes.R
import ru.book.recipes.databinding.ActivityMainBinding.inflate
import ru.book.recipes.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStartBinding.inflate(inflater, container, false)
//        binding.cooking.setImageResource(
//            R.drawable.ic_baseline_dangerous_24)
        return binding.root
    }
}