package ru.book.recipes.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import ru.book.recipes.R
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.data.User
import ru.book.recipes.databinding.FragmentAuthBinding
import ru.book.recipes.databinding.FragmentStartBinding
import ru.book.recipes.viewModel.RecipeViewModel

class authFragment : Fragment() {
    val viewModel: RecipeViewModel by viewModels(
        ownerProducer =
        ::requireParentFragment
    )
    private lateinit var theNavigationView: NavigationView
    private lateinit var headerView: View
    private lateinit var locked: View
    private lateinit var unLcked: View
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var thesignInView: MenuItem
    private lateinit var theauthInView: MenuItem

    private lateinit var nameField: View
    private lateinit var passwordField: View
    private lateinit var authButton: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAuthBinding.inflate(inflater, container, false)
        nameField = binding.name
        passwordField = binding.password
        authButton = binding.authButton

        //проверяем, возможно Юзер зарегался только что и уже авторизован
        val appActivity = requireActivity() as AppActivity
        theNavigationView =
            appActivity.findViewById<NavigationView>(R.id.navigationView)
        headerView = theNavigationView.getHeaderView(0)
        userName = headerView.findViewById<TextView>(R.id.name)
        userEmail = headerView.findViewById<TextView>(R.id.email)
        locked = headerView.findViewById<View>(R.id.lock)
        unLcked = headerView.findViewById<View>(R.id.unlocked)

        thesignInView = theNavigationView.menu.findItem(R.id.signinFragment)
        theauthInView = theNavigationView.menu.findItem(R.id.authFragment)
        if (AppActivity.userName != "" &&
            AppActivity.userEmail != ""
        ) {
            locked.visibility = android.view.View.GONE
            unLcked.visibility = android.view.View.VISIBLE
            thesignInView.setVisible(false)
            theauthInView.setVisible(false)
        }

        authButton.setOnClickListener {
            val userNameText = (nameField as EditText).text
            val userPasswordText = (passwordField as EditText).text
            if (!userNameText.isEmpty() && !userPasswordText.isEmpty()) {
                val userToAuth = User(0, userNameText.toString(), userPasswordText.toString(), "")

                var theUser: User? = null
                viewModel.findUser(userToAuth)

                viewModel.foundedUser.observe(viewLifecycleOwner) { foundedUser ->
                    println(foundedUser)
                    theUser = foundedUser
                    if (foundedUser != null) {
                        locked.visibility = android.view.View.GONE
                        unLcked.visibility = android.view.View.VISIBLE
                        userName = headerView.findViewById<TextView>(R.id.name)
                        userEmail = headerView.findViewById<TextView>(R.id.email)
                        userName.setText(foundedUser.name)
                        userEmail.setText(foundedUser.email)
                        AppActivity.signedUser.userId = foundedUser.id.toString()
                        AppActivity.signedUser.userName = foundedUser.name
                        AppActivity.signedUser.userEmail = foundedUser.email
                        thesignInView = theNavigationView.menu.findItem(R.id.signinFragment)
                        theauthInView = theNavigationView.menu.findItem(R.id.authFragment)
                        thesignInView.setVisible(false)
                        theauthInView.setVisible(false)
                        Toast.makeText(context, "Yes, you are", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigateUp()
                    }
                    if (theUser == null) {
                        context?.let { it1 ->
                            MaterialAlertDialogBuilder(context!!)
                                .setTitle(resources.getString(R.string.dialogtitle))
                                .setMessage(resources.getString(R.string.long_message))
                                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                    // Respond to positive button press
                                }
                                .show()
                        }
                    }
                }
            }
        }
        return binding.root
    }
}
