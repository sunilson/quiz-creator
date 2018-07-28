package com.sunilson.quizcreator.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sunilson.quizcreator.R
import kotlinx.android.synthetic.main.divider_headline.view.*

class DividerHeadline(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    var text: String = ""
        set(value) {
            field = value
            divider_text.text = field
        }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.divider_headline, this, true)
        val a = context.theme.obtainStyledAttributes(attributeSet, R.styleable.DividerHeadline, 0, 0)
        this.text = a.getString(R.styleable.DividerHeadline_text)
        a.recycle()
        orientation = LinearLayout.VERTICAL
    }

}