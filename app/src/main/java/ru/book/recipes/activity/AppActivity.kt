package ru.book.recipes.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import ru.book.recipes.R
import ru.book.recipes.data.*
import ru.book.recipes.databinding.ActivityAppBinding
import ru.book.recipes.fragment.FilterDialog
import ru.book.recipes.fragment.SearchDialog


class AppActivity : AppCompatActivity() {
    //    private val viewModel: RecipeViewModel by viewModels()
//    private lateinit var favorites: MaterialButton
//    private lateinit var likes: MaterialButton
//    private lateinit var dislikes: MaterialButton
//    private lateinit var shares: MaterialButton
    private lateinit var binding: ActivityAppBinding
    private lateinit var theToolBar: Toolbar
    private lateinit var theDrawerLayout: DrawerLayout
    private lateinit var theNavigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var theHostFragment: NavHostFragment
    private lateinit var theNavController: NavController

    private lateinit var headerView: View
    private lateinit var thesignInView: MenuItem
    private lateinit var theauthInView: MenuItem

    private lateinit var locked: View
    private lateinit var unLocked: View
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        theToolBar = binding.topAppBar
        theDrawerLayout = binding.drawerLayout
        theHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        theNavigationView = binding.navigationView

        setSupportActionBar(theToolBar)
        bottomNavigation()
        drawerNavigation()

        theToolBar.setNavigationOnClickListener {
            theDrawerLayout.open()
        }

        //Регистрация Юзера
        thesignInView = theNavigationView.menu.findItem(R.id.signinFragment)
        theauthInView = theNavigationView.menu.findItem(R.id.authFragment)
        if (signedUser.userName != "" &&
            signedUser.userEmail != ""
        ) {
            theNavigationView = binding.navigationView
            headerView = theNavigationView.getHeaderView(0)
            thesignInView.setVisible(false)
            theauthInView.setVisible(false)
            locked = headerView.findViewById<View>(R.id.lock)
            unLocked = headerView.findViewById<View>(R.id.unlocked)
            userName = headerView.findViewById<TextView>(R.id.name)
            userEmail = headerView.findViewById<TextView>(R.id.email)
            locked.visibility = android.view.View.GONE
            unLocked.visibility = android.view.View.VISIBLE
            userName.setText(signedUser.userName)
            userEmail.setText(signedUser.userEmail)
        } else {
            println("null")
        }

        if (thesignInView.isChecked) {
            theDrawerLayout.close()
        }
        if (theauthInView.isChecked) {
            theDrawerLayout.close()
        }

        //Приветствие
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.firstDialogTitle))
            .setMessage(resources.getString(R.string.wholeText))
            .setPositiveButton(resources.getString(R.string.firstDialogDismiss)) { dialog, which ->
                dialog.dismiss()
            }
            .show()

//        //вызываем Search
//        binding.topAppBar.findViewById<View>(R.id.searchFragment).setOnClickListener {
//            SearchDialog().show(supportFragmentManager, SearchDialog.TAG)
//            println("pressed search")
//        }
//
//        //вызываем Filter
//        binding.topAppBar.findViewById<View>(R.id.filterFragment).setOnClickListener {
//            FilterDialog().show(supportFragmentManager, FilterDialog.TAG)
//            println("pressed filter")
//        }
//    }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchFragment -> {
                SearchDialog().show(supportFragmentManager, SearchDialog.TAG)
                true
            }
            R.id.filterFragment -> {
                FilterDialog().show(supportFragmentManager, FilterDialog.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Bottom Navigation
    fun bottomNavigation() {
        theNavController = theHostFragment.navController
        appBarConfiguration = AppBarConfiguration(theNavController.graph)
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(theNavController)
        setupActionBarWithNavController(theNavController)
    }

    //Drawer Navigation
    fun drawerNavigation() {
        theNavController = theHostFragment.navController
        appBarConfiguration = AppBarConfiguration(theNavController.graph, theDrawerLayout)
        findViewById<NavigationView>(R.id.navigationView)
            .setupWithNavController(theNavController)
        setupActionBarWithNavController(theNavController, appBarConfiguration)
    }

    //Идентификация Юзера
    companion object signedUser {
        var userId = ""
        var userName = ""
        var userEmail = ""
        var allUserFavors: List<Favorite>? = null
        var allUserLikes: List<Like>? = null
        var allUserDislikes: List<Dislike>? = null
        var allUserShares: List<Share>? = null
        var fragStack: List<String> = listOf()
    }
}


