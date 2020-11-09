package com.sunilson.quizcreator.presentation.views.tutorialSlider

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.sunilson.quizcreator.R
import kotlinx.android.synthetic.main.tutorial_slider.view.*

class TutorialSlider(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {


    private var listener: TutorialSliderListener? = null
    var slides: List<TutorialSlide> = mutableListOf()
        set(value) {
            field = value
            pagination.itemAmount = field.size
            tutorial_viewpager.adapter = TutorialViewPagerAdapter(context, field)
        }

    fun setTutorialListener(listener: TutorialSliderListener) {
        this.listener = listener
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.tutorial_slider, this, true)
        tutorial_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(position: Int, offset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(p0: Int) {
                next_text.text = if (p0 == slides.size - 1) context.getString(R.string.done) else context.getString(R.string.next)
                back.visibility = if (p0 == 0) View.INVISIBLE else View.VISIBLE
                pagination.activeItem = p0
            }
        })

        pagination.setPaginationItemClickedListener { tutorial_viewpager.currentItem = it }

        next.setOnClickListener {
            if (tutorial_viewpager.currentItem == slides.size - 1) listener?.invoke()
            else tutorial_viewpager.currentItem += 1
        }
        back.setOnClickListener {
            tutorial_viewpager.currentItem -= 1
        }
    }
}
typealias  TutorialSliderListener = () -> Unit