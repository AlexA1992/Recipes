package ru.book.recipes.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.book.recipes.R
import ru.book.recipes.databinding.ActivityMainBinding


class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.allRecipes -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.favorites -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.contest -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }
    }
}