package ru.book.recipes.fragment

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import ru.book.recipes.R
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.data.User
import ru.book.recipes.databinding.FragmentAuthBinding
import ru.book.recipes.databinding.FragmentSigninBinding
import ru.book.recipes.databinding.FragmentSingleRecipeBinding
import ru.book.recipes.viewModel.RecipeViewModel

class signInFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val binding = FragmentSigninBinding.inflate(inflater, container, false)
        val nameField = binding.name
        val passwordField = binding.password
        val emailField = binding.email
        val signButton = binding.signButton

        signButton.setOnClickListener {
            val name: String = nameField.text.toString()
            val password: String = passwordField.text.toString()
            val email: String = emailField.text.toString()
            val newUser = User(
                name = name,
                password = password,
                email = email
            )
            viewModel.addUser(newUser)

            viewModel.dataUsers.observe(viewLifecycleOwner) { users ->
                if (name == users.first().name.toString()
                    && password == users.first().password.toString()
                    && email == users.first().email.toString()
                ) {
                    println("added")
                    val user = users.first()
                    val appActivity = requireActivity() as AppActivity
                    theNavigationView =
                        appActivity.findViewById<NavigationView>(R.id.navigationView)
                    headerView = theNavigationView.getHeaderView(0)
                    locked = headerView.findViewById<View>(R.id.lock)
                    unLcked = headerView.findViewById<View>(R.id.unlocked)
                    userName = headerView.findViewById<TextView>(R.id.name)
                    userEmail = headerView.findViewById<TextView>(R.id.email)
                    locked.visibility = android.view.View.GONE
                    unLcked.visibility = android.view.View.VISIBLE
                    userName.setText(user.name)
                    userEmail.setText(user.email)
                    thesignInView = theNavigationView.menu.findItem(R.id.signinFragment)
                    theauthInView = theNavigationView.menu.findItem(R.id.authFragment)
                    thesignInView.setVisible(false)
                    theauthInView.setVisible(false)
                    AppActivity.signedUser.userName = user.name
                    AppActivity.userEmail = user.email

                    Toast.makeText(context, "Congratulations! You are in", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(
                        context,
                        "Something was amiss, turn to the coder",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return binding.root
    }
}