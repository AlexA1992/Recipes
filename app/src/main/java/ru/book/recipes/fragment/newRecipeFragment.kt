package ru.book.recipes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.book.recipes.R
import ru.book.recipes.activity.AppActivity
import ru.book.recipes.data.Category
import ru.book.recipes.data.Recipe
import ru.book.recipes.databinding.FragmentNewRecipeBinding
import ru.book.recipes.utils.StringArg
import ru.book.recipes.utils.checkRecipeFields
import ru.book.recipes.viewModel.RecipeViewModel
import ru.book.recipes.utils.hideKeyboard

class newRecipeFragment : Fragment() {
    private lateinit var recipeTitle: EditText
    private lateinit var imageReference: EditText
    private lateinit var recipeContent: EditText
    private lateinit var step1: EditText
    private lateinit var step2: EditText
    private lateinit var step3: EditText
    private lateinit var step4: EditText
    private lateinit var step5: EditText
    private lateinit var step6: EditText
    private lateinit var step7: EditText
    private lateinit var step8: EditText
    private lateinit var step9: EditText
    private lateinit var step10: EditText
    private lateinit var saveRecipeButton: MaterialButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var radio1: RadioButton
    private lateinit var radio2: RadioButton
    private lateinit var radio3: RadioButton
    private lateinit var radio4: RadioButton
    private lateinit var radio5: RadioButton
    private lateinit var radio6: RadioButton
    private lateinit var radio7: RadioButton
    private lateinit var theRecipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val binding = FragmentNewRecipeBinding.inflate(inflater, container, false)
        saveRecipeButton = binding.recipeCreateButton
        val viewModel: RecipeViewModel by viewModels(
            ownerProducer =
            ::requireParentFragment
        )

        //инициализируем всё
        recipeTitle = binding.givenTitle
        recipeContent = binding.givenContent
        imageReference = binding.givenReference
        step1 = binding.step1
        step2 = binding.step2
        step3 = binding.step3
        step4 = binding.step4
        step5 = binding.step5
        step6 = binding.step6
        step7 = binding.step7
        step8 = binding.step8
        step9 = binding.step9
        step10 = binding.step10
        val stepFieldList = listOf<EditText>(
            step1, step2, step3, step4, step5, step6, step7, step8, step9, step10
        )
        radioGroup = binding.radioGroup
        radio1 = binding.radioAMERICAN
        radio2 = binding.radioASIAN
        radio3 = binding.radioEASTERN
        radio4 = binding.radioMEDITERRANEAN
        radio5 = binding.radioPANASIAN
        radio6 = binding.radioRUSSIAN
        radio7 = binding.radioEUROPEAN
        val radioList = listOf<RadioButton>(
            radio1, radio2, radio3, radio4, radio5, radio6, radio7
        )

        //проверяем передан ли Рецепт для редактирования
        val recipeToShow = arguments?.recipeId1

        if (recipeToShow != null) {
            viewModel.dataRecipes.observe(viewLifecycleOwner) { recipes ->
                theRecipe = recipes.find {
                    recipeToShow.toLong() == it.id
                }!!

                //запихиваем
                recipeTitle.setText(theRecipe.title)
                imageReference.setText(theRecipe.image)
                recipeContent.setText(theRecipe.content)

                for (button in radioList) {
                    if (button.text.toString() == theRecipe.category.name) {
                        button.isChecked = true
                        break
                    }
                }
                val stepString = theRecipe.steps
                val stepList: MutableList<String> = stepString.split(";") as MutableList<String>

                for (stepField in stepFieldList) {
                    //println(stepList)
                    if (stepList.isNotEmpty()) {
                        println("stepList $stepList")
                        stepField.setText(stepList.get(0))
                        stepList.removeAt(0)
                        println("stepList $stepList")
                    } else {
                        break
                    }
                }
            }
        }

        var categoryString: String = ""
        var category: Category? = null

        saveRecipeButton.setOnClickListener {
            val title = recipeTitle.text.toString()
            val content = recipeContent.text.toString()
            val reference = imageReference.text.toString()
            for (radio in radioList) {
                if (radio.isChecked) {
                    category = Category.valueOf(radio.text.toString())
                    categoryString = category.toString()
                    break
                }
            }

            //делаем строку из шагов
            val step1Text = step1.text
            val step2Text = step2.text
            val step3Text = step3.text
            val step4Text = step4.text
            val step5Text = step5.text
            val step6Text = step6.text
            val step7Text = step7.text
            val step8Text = step8.text
            val step9Text = step9.text
            val step10Text = step10.text

            val steps = listOf(
                step1Text.toString(),
                step2Text.toString(),
                step3Text.toString(),
                step4Text.toString(),
                step5Text.toString(),
                step6Text.toString(),
                step7Text.toString(),
                step8Text.toString(),
                step9Text.toString(),
                step10Text.toString(),
            )
            var allSteps: String = ""

            for (stepString in steps) {
                if (stepString != "") {
                    allSteps = allSteps + stepString + ";"
                }
            }

            val recipeCheck: String =
                checkRecipeFields(title, reference, categoryString, content, allSteps)
            if (recipeCheck != "") {
                context?.let { it1 ->
                    MaterialAlertDialogBuilder(it1)
                        .setTitle(resources.getString(R.string.createReciprDialogError))
                        .setMessage(recipeCheck)
                        .setPositiveButton(resources.getString(R.string.undestood)) { dialog, which ->
                            // Respond to positive button press
                        }
                        .show()
                }
            } else {
                if (recipeToShow != null) {
                    val oldRecipe =
                        Recipe(
                            id = theRecipe.id,
                            title = title,
                            authorId = AppActivity.userId.toLong(),
                            author = AppActivity.userName,
                            image = reference,
                            category = Category.valueOf(categoryString),
                            content = content,
                            steps = allSteps,
                        )
                    viewModel.updateRecipe(oldRecipe)

                    context?.let { it1 ->
                        MaterialAlertDialogBuilder(it1)
                            .setTitle(resources.getString(R.string.createReciprDialogSuccess))
                            .setMessage(R.string.SuccessMessageUpdate)
                            .setPositiveButton(resources.getString(R.string.undestood)) { dialog, which ->
                                // Respond to positive button press
                            }
                            .show()
                        findNavController().navigateUp()
                    }
                } else {
                    val newRecipe = category?.let { it1 ->
                        Recipe(
                            title = title,
                            authorId = AppActivity.userId.toLong(),
                            author = AppActivity.userName,
                            image = reference,
                            category = it1,
                            content = content,
                            steps = allSteps,
                        )
                    }
                    if (newRecipe != null) {
                        viewModel.createRecipe(newRecipe)
                        context?.let { it1 ->
                            MaterialAlertDialogBuilder(it1)
                                .setTitle(resources.getString(R.string.createReciprDialogSuccess))
                                .setMessage(R.string.SuccessMessage)
                                .setPositiveButton(resources.getString(R.string.undestood)) { dialog, which ->
                                    // Respond to positive button press
                                }
                                .show()
                            view?.hideKeyboard()
                            findNavController().navigateUp()
                        }
                    }
                }
            }

        }
        return binding.root
    }

    companion object {
        var Bundle.recipeId1: String? by StringArg
        //var Bundle.recipeIdfromSearch: String? by StringArg
    }
}

