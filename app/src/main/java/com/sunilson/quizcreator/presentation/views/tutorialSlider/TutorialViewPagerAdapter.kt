package com.sunilson.quizcreator.presentation.views.tutorialSlider

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.sunilson.quizcreator.R
import kotlinx.android.synthetic.main.tutorial_slide.view.*


class TutorialViewPagerAdapter(val context: Context, val slides: List<TutorialSlide>) : PagerAdapter() {

    override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1
    override fun getCount(): Int = slides.size

    override fun getPageTitle(position: Int): CharSequence? {
        return "Egal"
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.tutorial_slide, container, false) as ViewGroup
        layout.slide_text.text = slides[position].text
        Glide.with(context).load(slides[position].image).into(layout.slide_graphic)
        slides[position].size?.let {
            layout.slide_graphic.layoutParams.width = it.first
            layout.slide_graphic.layoutParams.height = it.second
            layout.slide_graphic.requestLayout()
        }
        container.addView(layout)
        return layout
    }

}