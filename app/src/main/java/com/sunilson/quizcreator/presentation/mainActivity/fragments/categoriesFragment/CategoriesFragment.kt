package com.sunilson.quizcreator.presentation.mainActivity.fragments.categoriesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentAllCategoriesBinding
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.EventBus
import com.sunilson.quizcreator.presentation.shared.EventChannel
import com.sunilson.quizcreator.presentation.shared.dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.dialogs.SimpleConfirmDialog
import com.sunilson.quizcreator.presentation.shared.dialogs.SimpleInputDialog
import com.sunilson.quizcreator.presentation.shared.kotlinExtensions.showToast
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator
import kotlinx.android.synthetic.main.fragment_all_categories.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.lifecycleScope

class CategoriesFragment : BaseFragment() {

    private val categoriesRecyclerAdapterFactory: CategoriesRecyclerAdapterFactory by lifecycleScope.inject()
    private val categoriesViewModel: CategoriesViewModel by lifecycleScope.inject()
    private val eventBus: EventBus by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAllCategoriesBinding.inflate(inflater, container, false)
        binding.viewModel = categoriesViewModel
        val view = binding.root

        view.category_recycler_view.layoutManager = LinearLayoutManager(context)
        view.category_recycler_view.itemAnimator = OvershootInLeftAnimator(1f)
        view.category_recycler_view.itemAnimator!!.addDuration = 300
        view.category_recycler_view.itemAnimator!!.removeDuration = 300
        view.category_recycler_view.itemAnimator!!.moveDuration = 300
        view.category_recycler_view.itemAnimator!!.changeDuration = 300
        view.category_recycler_view.adapter = categoriesRecyclerAdapterFactory.create({
            val dialog = SimpleConfirmDialog.newInstance(
                String.format(
                    getString(R.string.delete_category),
                    it.name
                ), getString(R.string.delete_category_confirmation)
            )
            dialog.listener = object : DialogListener<Boolean> {
                override fun onResult(result: Boolean?) {
                    if (result != null && result) {
                        categoriesViewModel.deleteCategory(it).subscribe({
                            eventBus.publishToChannel(EventChannel.RELOAD_QUESTIONS, true)
                        }, {

                        })
                    }
                }
            }
            dialog.show(childFragmentManager, "dialog")
        }, {
            val dialog = SimpleInputDialog.newInstance(getString(R.string.update_category), it.name)
            dialog.listener = object : DialogListener<String> {
                override fun onResult(result: String?) {
                    if (result != null && result.isNotEmpty()) {
                        it.name = result
                        categoriesViewModel.updateCategory(it).subscribe({}, {
                            context?.showToast(it.message)
                            eventBus.publishToChannel(EventChannel.RELOAD_QUESTIONS, null)
                        })
                    }
                }
            }
            dialog.show(childFragmentManager, "dialog")
        }, view.category_recycler_view)

        view.fab.setOnClickListener {
            val dialog = SimpleInputDialog.newInstance(getString(R.string.add_category))
            dialog.listener = object : DialogListener<String> {
                override fun onResult(result: String?) {
                    if (result != null && result.isNotEmpty()) {
                        categoriesViewModel.addCategory(result).subscribe({}, {
                            context?.showToast(it.message)
                        })
                    }
                }
            }
            dialog.show(childFragmentManager, "dialog")
        }

        return view
    }

    companion object {
        fun newInstance(): CategoriesFragment {
            return CategoriesFragment()
        }
    }
}