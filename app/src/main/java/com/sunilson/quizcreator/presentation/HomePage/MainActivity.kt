package com.sunilson.quizcreator.presentation.HomePage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sunilson.quizcreator.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton.setOnClickListener {
            stackedCardView.addCardBehind()
        }

        removeButton.setOnClickListener {
            stackedCardView.removeCard()
        }
    }
}
