package com.sunilson.quizcreator.presentation.mainActivity.fragments.allQuestionsFragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.databinding.FragmentAllQuestionBinding
import com.sunilson.quizcreator.presentation.addQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.mainActivity.OnBackPressedListener
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.ADD_QUESTIONS_INTENT
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.shared.EventBus
import com.sunilson.quizcreator.presentation.shared.EventChannel
import com.sunilson.quizcreator.presentation.shared.dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.dialogs.SimpleConfirmDialog
import com.sunilson.quizcreator.presentation.shared.dialogs.importExportDialog.ImportExportDialog
import com.sunilson.quizcreator.presentation.shared.kotlinExtensions.convertToPx
import com.sunilson.quizcreator.presentation.shared.kotlinExtensions.showToast
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator
import kotlinx.android.synthetic.main.fragment_all_question.*
import kotlinx.android.synthetic.main.fragment_all_question.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.lifecycleScope

class AllQuestionsFragment : BaseFragment(), OnBackPressedListener {

    private val questionsRecyclerAdapterFactory: QuestionsRecyclerAdapterFactory by lifecycleScope.inject()
    private val categorySpinnerAdapter: CategorySpinnerAdapter by lifecycleScope.inject()
    private val allQuestionsViewModel: AllQuestionsViewModel by lifecycleScope.inject()
    private val repository: IQuizRepository by inject()
    private val eventBus: EventBus by inject()

    private var searchOpen: Boolean = false
    lateinit var questionsRecyclerAdapter: QuestionsRecyclerAdapter

    private val imm: InputMethodManager by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }


    @Suppress("LongMethod")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAllQuestionBinding>(
            inflater,
            R.layout.fragment_all_question,
            container,
            false
        )
        binding.viewModel = allQuestionsViewModel
        val view = binding.root

        questionsRecyclerAdapter = questionsRecyclerAdapterFactory.create({ question ->
            val dialog = SimpleConfirmDialog.newInstance(
                getString(R.string.delete_question),
                getString(R.string.delete_question_question)
            )
            dialog.listener = object : DialogListener<Boolean> {
                override fun onResult(result: Boolean?) {
                    result?.let {
                        if (it) {
                            disposable.add(
                                allQuestionsViewModel.deleteQuestion(question).subscribe(
                                    { questionsRecyclerAdapter.remove(question) },
                                    { context?.showToast(it.message) }
                                )
                            )
                        }
                    }
                }
            }
            dialog.show(childFragmentManager, "dialog")
        }, {
            disposable.add(
                allQuestionsViewModel.updateQuestion(it).subscribe(
                    { questionsRecyclerAdapter.update(it) },
                    { context?.showToast(it.message) }
                )
            )
        }, {
            val intent = Intent(context, AddQuestionActivity::class.java)
            intent.putExtra("id", it.id)
            startActivityForResult(intent, ADD_QUESTIONS_INTENT)
        }, recyclerView = view.question_recyclerview)

        view.category_spinner.adapter = categorySpinnerAdapter

        view.question_recyclerview.adapter = questionsRecyclerAdapter
        view.question_recyclerview.isNestedScrollingEnabled = true
        view.question_recyclerview.itemAnimator = OvershootInLeftAnimator(1f)
        view.question_recyclerview.itemAnimator!!.addDuration = 300
        view.question_recyclerview.itemAnimator!!.removeDuration = 300
        view.question_recyclerview.itemAnimator!!.moveDuration = 300
        view.question_recyclerview.itemAnimator!!.changeDuration = 300
        view.question_recyclerview.setHasFixedSize(true)
        view.question_recyclerview.layoutManager = LinearLayoutManager(context)

        view.fab.setOnFloatingActionsMenuUpdateListener(object :
            FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
            override fun onMenuCollapsed() {
                view.all_question_overlay.visibility = View.GONE
            }

            override fun onMenuExpanded() {
                view.all_question_overlay.visibility = View.VISIBLE
            }
        })

        view.fab_add_single.setOnClickListener {
            view.fab.collapse()
            val intent = Intent(context, AddQuestionActivity::class.java)
            intent.putExtra("type", QuestionType.SINGLE_CHOICE)
            startActivity(intent)
        }

        view.fab_add_multiple.setOnClickListener {
            view.fab.collapse()
            val intent = Intent(context, AddQuestionActivity::class.java)
            intent.putExtra("type", QuestionType.MULTIPLE_CHOICE)
            startActivity(intent)
        }

        view.fab_export_import.setOnClickListener {
            view.fab.collapse()
            val dialog = ImportExportDialog.newInstance()
            dialog.listener = object : DialogListener<Boolean> {
                override fun onResult(result: Boolean?) {
                    if (result != null && result) allQuestionsViewModel.loadQuestions()
                }
            }
            dialog.show(childFragmentManager, "dialog")
        }

        view.searchbar_background.alpha = 0f

        view.searchbar.setOnClickListener {
            if (searchOpen) closeSearch()
            else openSearch()
        }

        view.swipe_layout.setProgressViewOffset(true, 100, 200)

        view.close_searchbar.setOnClickListener {
            closeSearch()
        }

        view.open_searchbar.setOnClickListener {
            openSearch()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ADD_QUESTIONS_INTENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    allQuestionsViewModel.loadQuestions()
                    eventBus.publishToChannel(EventChannel.RELOAD_CATEGORIES, true)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        allQuestionsViewModel.loadQuestions()
        allQuestionsViewModel.loadCategories()
    }

    override fun onDestroy() {
        super.onDestroy()
        allQuestionsViewModel.onDestroy()
    }

    private fun openSearch() {
        val params = searchbar.layoutParams as LinearLayout.LayoutParams
        open_searchbar.visibility = View.GONE
        searchbar_tags.visibility = View.GONE

        val backgroundAnimator =
            ObjectAnimator.ofFloat(searchbar_background, "alpha", 0f, 1f).setDuration(300)
        backgroundAnimator.interpolator = DecelerateInterpolator()

        val animator = ValueAnimator.ofInt(45, 180).setDuration(300)
        animator.interpolator = AccelerateInterpolator()
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            params.height = value.convertToPx(requireContext())
            searchbar.layoutParams = params
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                close_searchbar.visibility = View.VISIBLE
                searchbar_edittext.visibility = View.VISIBLE
                category_spinner_container.visibility = View.VISIBLE
                searchOpen = true
            }
        })

        backgroundAnimator.start()
        animator.start()
    }

    private fun closeSearch() {
        searchOpen = false
        val params = searchbar.layoutParams as LinearLayout.LayoutParams
        close_searchbar.visibility = View.GONE
        searchbar_edittext.visibility = View.GONE
        category_spinner_container.visibility = View.GONE

        val backgroundAnimator =
            ObjectAnimator.ofFloat(searchbar_background, "alpha", 1f, 0f).setDuration(300)
        backgroundAnimator.interpolator = DecelerateInterpolator()

        val animator = ValueAnimator.ofInt(180, 45).setDuration(300)
        animator.interpolator = AccelerateInterpolator()
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            params.height = value.convertToPx(requireContext())
            searchbar.layoutParams = params
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                open_searchbar.visibility = View.VISIBLE
                searchbar_tags.visibility = View.VISIBLE
            }
        })

        imm.hideSoftInputFromWindow(
            searchbar_edittext.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        animator.start()
        backgroundAnimator.start()
    }

    override fun onBackPressed(): Boolean {
        return if (searchOpen) {
            closeSearch()
            false
        } else {
            true
        }
    }

    companion object {
        fun newInstance(): AllQuestionsFragment = AllQuestionsFragment()
    }

}