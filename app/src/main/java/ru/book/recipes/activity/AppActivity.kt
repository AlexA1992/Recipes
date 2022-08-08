package ru.book.recipes.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import ru.book.recipes.R
import android.content.Intent
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.book.recipes.data.User
import ru.book.recipes.databinding.ActivityAppBinding
import ru.book.recipes.fragment.signInFragment
import ru.book.recipes.viewModel.RecipeViewModel

class AppActivity : AppCompatActivity() {
    //private val viewModel: RecipeViewModel by viewModels()
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
    private lateinit var unLcked: View
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
        if (signedUser.userName != "" &&
            signedUser.userEmail != ""
        ) {
            println(signedUser.userEmail)
            theNavigationView = binding.navigationView
            headerView = theNavigationView.getHeaderView(0)
            thesignInView = theNavigationView.menu.findItem(R.id.signinFragment)
            theauthInView = theNavigationView.menu.findItem(R.id.authFragment)
            thesignInView.setVisible(false)
            theauthInView.setVisible(false)
            locked = headerView.findViewById<View>(R.id.lock)
            unLcked = headerView.findViewById<View>(R.id.unlocked)
            userName = headerView.findViewById<TextView>(R.id.name)
            userEmail = headerView.findViewById<TextView>(R.id.email)
            locked.visibility = android.view.View.GONE
            unLcked.visibility = android.view.View.VISIBLE
            userName.setText(signedUser.userName)
            userEmail.setText(signedUser.userEmail)
        } else {
            println("null")
        }

        //Приветствие
//        MaterialAlertDialogBuilder(this)
//            .setTitle(resources.getString(R.string.firstDialogTitle))
//            .setMessage(resources.getString(R.string.wholeText))
//            .setPositiveButton(resources.getString(R.string.firstDialogDismiss)) { dialog, which ->
//                // Respond to positive button press
//            }
//            .show()

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
    }

}

