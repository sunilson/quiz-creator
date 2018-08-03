package com.sunilson.quizcreator.presentation.MainActivity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

interface SpeechRecognizingActivity {
    fun checkAudioPermissions() : Boolean
    fun startListening(cb: (String) -> Unit)
    fun stopListening()
}

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager.adapter = ViewPagerAdapter(supportFragmentManager)
        viewpager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {
                navigation.menu.getItem(p0).isChecked = true
            }
        })
        viewpager.offscreenPageLimit = 2

        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.all_categories_fragment -> viewpager.currentItem = 0
                R.id.all_questions_fragment -> viewpager.currentItem = 1
                R.id.statistics_fragment -> viewpager.currentItem = 2
                R.id.quiz_fragment -> viewpager.currentItem = 3
            }

            true
        }

        //setSupportActionBar(toolbar)
        //title = getString(R.string.app_name)
        //replaceFragment(StatisticsFragment.newInstance())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector
}
