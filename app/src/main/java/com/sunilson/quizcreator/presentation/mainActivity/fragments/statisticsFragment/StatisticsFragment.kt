package com.sunilson.quizcreator.presentation.mainActivity.fragments.statisticsFragment

import android.os.Bundle
import android.view.*
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentStatisticsBinding
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.dialogs.SimpleConfirmDialog
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import javax.inject.Inject


class StatisticsFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: StatisticsFragmentViewModel

    lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val view = binding.root
        view.reset_statistics.setOnClickListener {
            val dialog = SimpleConfirmDialog.newInstance(getString(R.string.reset_statistics), getString(R.string.reset_statistics_question))
            dialog.listener = object : DialogListener<Boolean> {
                override fun onResult(result: Boolean?) {
                    if (result != null && result) viewModel.resetStatistics()
                }
            }
            dialog.show(childFragmentManager, "dialog")
        }

        /*
        val pie = AnyChart.pie()
        val data = mutableListOf<DataEntry>()
        data.add(ValueDataEntry("John", 10000))
        data.add(ValueDataEntry("Jake", 12000))
        data.add(ValueDataEntry("Peter", 18000))
        pie.setData(data)
        view.statistics_view.setChart(pie)
        */

        return view
    }

    companion object {
        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }
}