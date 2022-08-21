package ru.book.recipes.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Layout
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import ru.book.recipes.utils.hideKeyboard
import ru.book.recipes.utils.smthAmiss
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
            val name = nameField.text.toString()
            val password = passwordField.text.toString()
            val email = emailField.text.toString()
            val newUser = User(
                name = name,
                password = password,
                email = email
            )
            println("$newUser newUser")
            viewModel.addUser(newUser)

            Handler(Looper.getMainLooper()).postDelayed(
                {             //Следим за составом Юзеров
                    viewModel.dataUsers.observe(viewLifecycleOwner) { users ->
                        println("$users allusers in lambda")
                        println("${users.firstOrNull()} firstUser")
                        println("${users.firstOrNull()?.name.toString()} name")
                        println("${users.firstOrNull()?.password.toString()} password")
                        println("${users.firstOrNull()?.email.toString()} email")
                        if (name == users.firstOrNull()?.name.toString()
                            && password == users.firstOrNull()?.password.toString()
                            && email == users.firstOrNull()?.email.toString()
                        ) {
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
                            userName.setText(users.firstOrNull()?.name.toString())
                            userEmail.setText(users.firstOrNull()?.email.toString())
                            thesignInView = theNavigationView.menu.findItem(R.id.signinFragment)
                            theauthInView = theNavigationView.menu.findItem(R.id.authFragment)
                            thesignInView.setVisible(false)
                            theauthInView.setVisible(false)
                            AppActivity.userId = users.firstOrNull()?.id.toString()
                            AppActivity.userName = users.firstOrNull()?.name.toString()
                            AppActivity.userEmail = users.firstOrNull()?.email.toString()

                            Toast.makeText(
                                context,
                                "Congratulations! You are in",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            view?.hideKeyboard()
                            findNavController().navigateUp()

                        } else {
                            println("in else")
                            context?.let { it1 ->
                                smthAmiss(
                                    it1, AppActivity.userId.toString(),
                                    AppActivity.userName.toString(),
                                    AppActivity.userEmail.toString(),
                                    users.firstOrNull()?.id.toString(),
                                    users.firstOrNull()?.name.toString(),
                                    users.firstOrNull()?.email.toString()
                                )
                            }
                            view?.hideKeyboard()
                        }
                    }
                },
                500
            )
        }
        viewModel.dataUsers.observe(viewLifecycleOwner) { users ->
            println("$users allusers below lambda")
        }
        return binding.root
    }
}