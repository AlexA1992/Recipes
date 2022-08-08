package ru.book.recipes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.book.recipes.databinding.FragmentAuthBinding
import ru.book.recipes.databinding.FragmentSearchBinding
import ru.book.recipes.databinding.FragmentStartBinding

class searchFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
}
