package com.sunilson.quizcreator.presentation.MainActivity.fragments

import android.os.Bundle
import android.view.*
import com.anychart.anychart.AnyChart
import com.anychart.anychart.DataEntry
import com.anychart.anychart.ValueDataEntry
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments.AddQuestionViewModel
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import javax.inject.Inject


class StatisticsFragment : BaseFragment() {

    @Inject
    lateinit var viewMOdel: AddQuestionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)


        val pie = AnyChart.pie()

        val data = mutableListOf<DataEntry>()
        data.add(ValueDataEntry("John", 10000))
        data.add(ValueDataEntry("Jake", 12000))
        data.add(ValueDataEntry("Peter", 18000))
        pie.setData(data)
        view.statistics_view.setChart(pie)
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