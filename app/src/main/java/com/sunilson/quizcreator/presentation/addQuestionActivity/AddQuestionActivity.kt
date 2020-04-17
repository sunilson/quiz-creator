package com.sunilson.quizcreator.presentation.addQuestionActivity

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.presentation.addQuestionActivity.fragments.AddQuestionFragment
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseActivity
import kotlinx.android.synthetic.main.activity_add_question.*

class AddQuestionActivity : BaseActivity() {

    var type: QuestionType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.add_new_question)

        type = intent.getSerializableExtra("type") as QuestionType?
        val id: String? = intent.getStringExtra("id")

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.add_question_framelayout, AddQuestionFragment.newInstance(type, id))
            .commit()
    }

    fun restart(current: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
            .replace(R.id.add_question_framelayout, AddQuestionFragment.newInstance(type))
            .commit()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return false
    }
}