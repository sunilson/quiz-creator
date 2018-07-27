package com.sunilson.quizcreator.presentation.MainActivity.fragments.StatisticsFragment

import android.os.Bundle
import android.view.*
import com.sunilson.quizcreator.databinding.FragmentStatisticsBinding
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }
}